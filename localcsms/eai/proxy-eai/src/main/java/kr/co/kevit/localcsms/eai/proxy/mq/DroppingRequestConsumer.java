package kr.co.kevit.localcsms.eai.proxy.mq;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * CPO 모드에서 {@code cpoCpCsId} 매핑이 없는 충전기용 — {@code req.<cpCsId>} 큐를 소비만 하고 버린다.
 *
 * <p>
 * daemon 은 proxy-eai 의 매핑 여부를 모른 채 충전기가 붙으면 그대로 이 큐에 원문을 발행할 수 있으므로,
 * 큐 자체는 선언해 두되 실제 WS 세션(대상 서버 접속)은 만들지 않고, 쌓이지 않도록 이 컨슈머로만
 * 소비해서 버린다(예외를 던지지 않아 requeue 되지 않음 — 영구 드롭).
 * </p>
 *
 * @author bckim
 */
public class DroppingRequestConsumer implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DroppingRequestConsumer.class);

    private final String cpCsId;

    public DroppingRequestConsumer(String cpCsId) {
        this.cpCsId = cpCsId;
    }

    @Override
    public void onMessage(Message message) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        LOGGER.warn("[proxy-eai] cpoCpCsId 매핑 없음 — req.{} 메시지 드롭: {}", cpCsId, body);
    }
}
