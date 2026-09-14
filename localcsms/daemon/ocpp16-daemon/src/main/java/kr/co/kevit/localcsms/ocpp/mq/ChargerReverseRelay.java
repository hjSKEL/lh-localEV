package kr.co.kevit.localcsms.ocpp.mq;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * CPO 모드 — 대상서버(외부 CSMS)가 보낸 원문 메시지를 충전기에 그대로 릴레이(CSMS→CS 방향).
 *
 * <p>
 * proxy-eai 가 {@code res.<cpCsId>} 로 발행한 원문(대상서버가 보낸 CALL 이든, 우리 충전기가 보낸 CALL 에
 * 대한 CALLRESULT 든 구분 없이 전부)을 소비해 실제 충전기 WS 세션으로 그대로 전달한다.
 * </p>
 *
 * <p>
 * 충전기는 실시간으로 연결/해제되므로, proxy-eai 처럼 기동 시 고정 세트를 구독하는 게 아니라
 * WS 연결 생명주기(연결 시 {@link #start}, 해제 시 {@link #stop})에 맞춰 컨슈머를 동적으로 생성/해제한다.
 * </p>
 *
 * @author bckim
 */
@Component
public class ChargerReverseRelay implements DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerReverseRelay.class);

    private final ConnectionFactory rabbitConnectionFactory;
    private final AmqpAdmin amqpAdmin;

    @Value("${daemon.relay.queue.response-prefix:res.}")
    private String responsePrefix;

    @Value("${daemon.relay.queue.durable:true}")
    private boolean queueDurable;

    private final Map<String, SimpleMessageListenerContainer> containers = new ConcurrentHashMap<>();

    public ChargerReverseRelay(ConnectionFactory rabbitConnectionFactory, AmqpAdmin amqpAdmin) {
        this.rabbitConnectionFactory = rabbitConnectionFactory;
        this.amqpAdmin = amqpAdmin;
    }

    /** 충전기 WS 연결 시 호출 — {@code res.<cpCsId>} 구독 시작. onMessage 는 반드시 스레드 안전해야 함. */
    public void start(String cpCsId, Consumer<String> onMessage) {
        stop(cpCsId); // 재연결 등으로 기존 컨슈머가 남아있으면 먼저 정리 (중복 소비 방지)

        String queueName = responsePrefix + cpCsId;
        amqpAdmin.declareQueue(new Queue(queueName, queueDurable));

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(rabbitConnectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener((Message message) -> {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            try {
                onMessage.accept(body);
            } catch (Exception e) {
                LOGGER.warn("[proxy-relay] res 처리 실패 cpCsId={}: {}", cpCsId, e.getMessage(), e);
                throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
            }
        });
        container.start();
        containers.put(cpCsId, container);
        LOGGER.info("[proxy-relay] res 구독 시작 cpCsId={} queue={}", cpCsId, queueName);
    }

    /** 충전기 WS 연결 해제 시 호출 — 구독 정지(재연결 시 컨슈머 중복 방지). */
    public void stop(String cpCsId) {
        SimpleMessageListenerContainer container = containers.remove(cpCsId);
        if (container != null) {
            container.stop();
            container.destroy();
            LOGGER.info("[proxy-relay] res 구독 종료 cpCsId={}", cpCsId);
        }
    }

    @Override
    public void destroy() {
        containers.keySet().forEach(this::stop);
    }
}
