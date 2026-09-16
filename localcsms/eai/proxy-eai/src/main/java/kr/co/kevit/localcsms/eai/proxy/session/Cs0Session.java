package kr.co.kevit.localcsms.eai.proxy.session;

import java.net.URI;
import java.nio.charset.StandardCharsets;
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
 * @author bckim
 */
public class Cs0Session implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Cs0Session.class);

    private static final String SESSION_ID = "CS0";

    private final String wsUrl;
    private final ProxyProperties props;
    private final SSLSocketFactory sslSocketFactory;
    private final ConnectionFactory rabbitConnectionFactory;
    private final AmqpAdmin amqpAdmin;

    private volatile boolean running = true;
    private volatile Ocpp16WsClient wsClient;
    private SimpleMessageListenerContainer listenerContainer;

    public Cs0Session(String wsUrl, ProxyProperties props, SSLSocketFactory sslSocketFactory,
            ConnectionFactory rabbitConnectionFactory, AmqpAdmin amqpAdmin) {
        this.wsUrl = wsUrl;
        this.props = props;
        this.sslSocketFactory = sslSocketFactory;
        this.rabbitConnectionFactory = rabbitConnectionFactory;
        this.amqpAdmin = amqpAdmin;
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
        wsClient.awaitClose();
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
