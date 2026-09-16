package kr.co.kevit.localcsms.eai.proxy.session;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import kr.co.kevit.localcsms.eai.proxy.config.ProxyProperties;
import kr.co.kevit.localcsms.eai.proxy.mq.ChargerRequestConsumer;
import kr.co.kevit.localcsms.eai.proxy.mq.ChargerResponsePublisher;
import kr.co.kevit.localcsms.eai.proxy.ws.Ocpp16WsClient;

/**
 * 충전기 1대(cpId-csId) 당 1개 생성되는 프록시 세션.
 *
 * <p>
 * 기동 시 RabbitMQ 요청 큐 리스너를 시작해두고, mTLS WS 연결/재연결 루프를 돈다.
 * WS 로 받은 응답은 파싱 없이 그대로 응답 큐로 재발행한다.
 * </p>
 *
 * @author bckim
 */
public class ChargerSession implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerSession.class);

    private final String cpCsId;
    private final String wsUrl;
    private final ProxyProperties props;
    private final SSLSocketFactory sslSocketFactory;
    private final ConnectionFactory rabbitConnectionFactory;
    private final RabbitTemplate rabbitTemplate;

    private volatile boolean running = true;
    private volatile Ocpp16WsClient wsClient;
    private SimpleMessageListenerContainer listenerContainer;

    /**
     * @param cpCsId 충전기 식별자(cpId-csId) — req./res. 큐 이름에 쓰임
     * @param wsUrl  접속 완성 URL (base + "/" + cpCsId, {@link ChargerSessionManager} 가 조립해서 전달)
     */
    public ChargerSession(String cpCsId, String wsUrl, ProxyProperties props, SSLSocketFactory sslSocketFactory,
            ConnectionFactory rabbitConnectionFactory, RabbitTemplate rabbitTemplate) {
        this.cpCsId = cpCsId;
        this.wsUrl = wsUrl;
        this.props = props;
        this.sslSocketFactory = sslSocketFactory;
        this.rabbitConnectionFactory = rabbitConnectionFactory;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run() {
        String requestQueue = props.getQueue().getRequestPrefix() + cpCsId;
        String responseQueue = props.getQueue().getResponsePrefix() + cpCsId;
        ChargerResponsePublisher publisher = new ChargerResponsePublisher(rabbitTemplate, responseQueue);

        listenerContainer = new SimpleMessageListenerContainer(rabbitConnectionFactory);
        listenerContainer.setQueueNames(requestQueue);
        listenerContainer.setMessageListener(new ChargerRequestConsumer(cpCsId, () -> wsClient));
        // WS 미연결 시 요청은 requeue 되므로, 짧은 간격으로 재시도되도록 recovery interval 조정
        listenerContainer.setRecoveryInterval(props.getTarget().getReconnectIntervalMs());
        listenerContainer.start();

        try {
            while (running) {
                try {
                    connectAndAwaitClose(publisher);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    LOGGER.warn("[proxy-eai] 세션 오류 cpCsId={}: {}", cpCsId, e.getMessage());
                }
                if (running) {
                    sleepQuietly(props.getTarget().getReconnectIntervalMs());
                }
            }
        } finally {
            listenerContainer.stop();
        }
    }

    private void connectAndAwaitClose(ChargerResponsePublisher publisher) throws Exception {
        URI uri = URI.create(wsUrl);
        wsClient = new Ocpp16WsClient(cpCsId, uri, sslSocketFactory, publisher::publish);
        boolean connected = wsClient.connectBlocking(props.getTarget().getConnectTimeoutMs(), TimeUnit.MILLISECONDS);
        if (!connected) {
            throw new IllegalStateException("WS 연결 실패 cpCsId=" + cpCsId + " url=" + wsUrl);
        }
        wsClient.awaitClose();
    }

    /** 세션 종료 — {@link ChargerSessionManager} 셧다운 시 호출. */
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
