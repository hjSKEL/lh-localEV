package kr.co.kevit.localcsms.eai.proxy.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 대상 서버로부터 받은 OCPP1.6 원문 메시지를 그대로 응답 큐로 재발행.
 *
 * @author bckim
 */
public class ChargerResponsePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String responseQueue;

    public ChargerResponsePublisher(RabbitTemplate rabbitTemplate, String responseQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.responseQueue = responseQueue;
    }

    /** 파싱 없이 원문 그대로 발행. */
    public void publish(String rawMessage) {
        rabbitTemplate.convertAndSend(responseQueue, rawMessage);
    }
}
