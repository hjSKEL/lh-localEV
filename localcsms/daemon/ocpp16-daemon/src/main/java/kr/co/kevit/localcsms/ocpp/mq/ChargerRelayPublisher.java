package kr.co.kevit.localcsms.ocpp.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 발행 담당.
 *
 * <ul>
 * <li><b>CPO 모드</b>: 충전기가 보낸 OCPP1.6 원문(메시지 종류 불문 — CALL/CALLRESULT/CALLERROR 전부)을
 * {@code req.<cpCsId>} 큐로 그대로 발행 — proxy-eai 가 소비해 대상서버로 전달.</li>
 * <li><b>LH 모드</b>: BootNotification/StatusNotification 원문을 topic exchange
 * ({@code daemon.relay.notify.exchange})로 단방향 발행(fire-and-forget, 응답 소비 없음). 라우팅키는
 * {@code <cpCsId>.<action>}.</li>
 * </ul>
 *
 * @author bckim
 */
@Component
public class ChargerRelayPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerRelayPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;

    @Value("${daemon.relay.queue.request-prefix:req.}")
    private String requestPrefix;

    @Value("${daemon.relay.queue.durable:true}")
    private boolean queueDurable;

    @Value("${daemon.relay.notify.exchange:ocpp16.notify}")
    private String notifyExchangeName;

    public ChargerRelayPublisher(RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
    }

    /** CPO 모드 — 충전기 원문을 {@code req.<cpCsId>} 로 그대로 발행 (파싱 없음). */
    public void publishToTarget(String cpCsId, String rawText) {
        String queueName = requestPrefix + cpCsId;
        amqpAdmin.declareQueue(new Queue(queueName, queueDurable));
        rabbitTemplate.convertAndSend(queueName, rawText);
        LOGGER.debug("[proxy-relay] req → {} : {}", queueName, rawText);
    }

    /** LH 모드 — Boot/StatusNotification 원문을 notify exchange 로 단방향 발행. */
    public void publishNotify(String cpCsId, String action, String rawText) {
        TopicExchange exchange = new TopicExchange(notifyExchangeName, queueDurable, false);
        amqpAdmin.declareExchange(exchange);
        String routingKey = cpCsId + "." + action;
        rabbitTemplate.convertAndSend(notifyExchangeName, routingKey, rawText);
        LOGGER.debug("[notify] {} routingKey={} : {}", notifyExchangeName, routingKey, rawText);
    }
}
