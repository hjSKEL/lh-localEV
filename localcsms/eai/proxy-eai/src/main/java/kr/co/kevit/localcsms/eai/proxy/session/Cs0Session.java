package kr.co.kevit.localcsms.eai.proxy.session;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kevit.localcsms.eai.proxy.config.ProxyProperties;
import kr.co.kevit.localcsms.eai.proxy.ws.Ocpp16WsClient;

/**
 * CS0 — 로컬 시스템 자체를 대표하는 단일 세션. LH/CPO 모드 무관하게 항상 정확히 1개만 기동된다.
 *
 * <p>
 * ocpp16-daemon 이 LH 모드에서 Boot/StatusNotification 원문을 발행하는
 * {@code ocpp16.notify} topic exchange 를 구독해서(라우팅키 전체 {@code #}), 받은 원문을
 * 파싱 없이 그대로 이 세션의 WS 연결로 전송한다. {@link ChargerSession} 과 달리 요청 큐 소비/응답
 * 재발행이 없는 완전한 단방향(fire-and-forget) 릴레이다.
 * </p>
 *
 * <p>
 * LH/CPO 모드 무관하게 CS0 자신이 하나의 OCPP1.6 충전기처럼 행동해야 한다 — 부팅 후 최초 연결 성공
 * 시에만 BootNotification.req 를 1회 전송하고(재연결 시에는 다시 보내지 않음), 이후 연결이 유지되는
 * 동안 5분 간격으로 Heartbeat.req 를 전송한다.
 * </p>
 *
 * @author bckim
 */
public class Cs0Session implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Cs0Session.class);

    private static final String SESSION_ID = "CS0";
    private static final String CHARGE_POINT_VENDOR = "kr.co.kevit";
    private static final long HEARTBEAT_INTERVAL_MINUTES = 5L;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String wsUrl;
    private final ProxyProperties props;
    private final SSLSocketFactory sslSocketFactory;
    private final ConnectionFactory rabbitConnectionFactory;
    private final AmqpAdmin amqpAdmin;
    private final String systemId;
    private final String localSystemSn;

    private volatile boolean running = true;
    private volatile Ocpp16WsClient wsClient;
    private SimpleMessageListenerContainer listenerContainer;
    private volatile boolean bootNotificationSent = false;
    private ScheduledExecutorService heartbeatScheduler;

    public Cs0Session(String wsUrl, ProxyProperties props, SSLSocketFactory sslSocketFactory,
            ConnectionFactory rabbitConnectionFactory, AmqpAdmin amqpAdmin, String systemId,
            String localSystemSn) {
        this.wsUrl = wsUrl;
        this.props = props;
        this.sslSocketFactory = sslSocketFactory;
        this.rabbitConnectionFactory = rabbitConnectionFactory;
        this.amqpAdmin = amqpAdmin;
        this.systemId = systemId;
        this.localSystemSn = localSystemSn;
    }

    @Override
    public void run() {
        ProxyProperties.Notify notifyCfg = props.getNotify();

        TopicExchange exchange = new TopicExchange(notifyCfg.getExchange(), notifyCfg.isDurable(), false);
        Queue queue = new Queue(notifyCfg.getQueueName(), notifyCfg.isDurable());
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("#"));

        listenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory);
        listenerContainer.setQueueNames(notifyCfg.getQueueName());
        listenerContainer.setMessageListener(
                message -> send(new String(message.getBody(), StandardCharsets.UTF_8)));
        listenerContainer.setRecoveryInterval(props.getTarget().getReconnectIntervalMs());
        listenerContainer.start();

        try {
            while (running) {
                try {
                    connectAndAwaitClose();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    LOGGER.warn("[proxy-eai][CS0] 세션 오류: {}", e.getMessage());
                }
                if (running) {
                    sleepQuietly(props.getTarget().getReconnectIntervalMs());
                }
            }
        } finally {
            listenerContainer.stop();
        }
    }

    private void connectAndAwaitClose() throws Exception {
        URI uri = URI.create(wsUrl);
        wsClient = new Ocpp16WsClient(SESSION_ID, uri, sslSocketFactory, msg -> {
            // CS0 은 단방향 발신 전용 — 대상서버가 뭔가 보내와도 처리하지 않는다(로그만).
            LOGGER.debug("[proxy-eai][CS0] 수신(미처리): {}", msg);
        });
        boolean connected = wsClient.connectBlocking(props.getTarget().getConnectTimeoutMs(), TimeUnit.MILLISECONDS);
        if (!connected) {
            throw new IllegalStateException("WS 연결 실패 CS0 url=" + wsUrl);
        }
        LOGGER.info("[proxy-eai][CS0] 연결 성공 url={}", wsUrl);

        onConnected();

        try {
            wsClient.awaitClose();
        } finally {
            stopHeartbeatScheduler();
        }
    }

    /**
     * 부팅 후 최초 연결 성공 시에만 BootNotification.req 를 1회 전송(재연결 시엔 다시 안 보냄),
     * 이후 연결이 유지되는 동안 5분 간격 Heartbeat.req 전송을 시작한다.
     */
    private void onConnected() {
        if (!bootNotificationSent) {
            try {
                send(buildBootNotificationCall());
                bootNotificationSent = true;
                LOGGER.info("[proxy-eai][CS0] BootNotification 전송(최초 1회) vendor={} model={} serial={}",
                        CHARGE_POINT_VENDOR, systemId, localSystemSn);
            } catch (Exception e) {
                LOGGER.warn("[proxy-eai][CS0] BootNotification 전송 실패: {}", e.getMessage(), e);
            }
        }
        startHeartbeatScheduler();
    }

    private void startHeartbeatScheduler() {
        stopHeartbeatScheduler();
        heartbeatScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "proxy-eai-CS0-heartbeat");
            t.setDaemon(true);
            return t;
        });
        heartbeatScheduler.scheduleAtFixedRate(() -> {
            try {
                send(buildHeartbeatCall());
            } catch (Exception e) {
                LOGGER.warn("[proxy-eai][CS0] Heartbeat 전송 실패: {}", e.getMessage(), e);
            }
        }, HEARTBEAT_INTERVAL_MINUTES, HEARTBEAT_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    private void stopHeartbeatScheduler() {
        if (heartbeatScheduler != null) {
            heartbeatScheduler.shutdownNow();
            heartbeatScheduler = null;
        }
    }

    private String buildBootNotificationCall() throws Exception {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("chargePointVendor", CHARGE_POINT_VENDOR);
        payload.put("chargePointModel", systemId);
        if (StringUtils.hasText(localSystemSn)) {
            payload.put("chargeBoxSerialNumber", localSystemSn);
        }
        List<Object> call = List.of(2, UUID.randomUUID().toString(), "BootNotification", payload);
        return MAPPER.writeValueAsString(call);
    }

    private String buildHeartbeatCall() throws Exception {
        List<Object> call = List.of(2, UUID.randomUUID().toString(), "Heartbeat", Map.of());
        return MAPPER.writeValueAsString(call);
    }

    /**
     * CS0 WS 연결로 원문을 그대로 전송. WS 미연결이면 로그만 남기고 드롭한다(단방향, requeue 대상 아님).
     * {@link kr.co.kevit.localcsms.eai.proxy.mq.LhChargerNotifyConsumer} 등 다른 소스에서도 재사용.
     */
    public void send(String raw) {
        if (wsClient == null || !wsClient.isOpenNow()) {
            LOGGER.warn("[proxy-eai][CS0] WS 미연결 — 메시지 드롭: {}", raw);
            return;
        }
        LOGGER.debug("[proxy-eai][CS0] → target : {}", raw);
        wsClient.send(raw);
    }

    public void stop() {
        running = false;
        stopHeartbeatScheduler();
        if (wsClient != null) {
            wsClient.close();
        }
        if (listenerContainer != null) {
            listenerContainer.stop();
        }
    }

    private void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
