package kr.co.kevit.localcsms.eai.proxy.mq;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import kr.co.kevit.localcsms.eai.proxy.ws.Ocpp16WsClient;

/**
 * 요청 큐({@code req.<cpCsId>})에서 받은 OCPP1.6 원문 메시지를 그대로 대상 서버로 전송.
 *
 * <p>
 * WS 가 연결돼 있지 않으면 예외를 던져 메시지를 requeue 시킨다(재연결 후 재시도).
 * </p>
 *
 * @author bckim
 */
public class ChargerRequestConsumer implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerRequestConsumer.class);

    private final String cpCsId;
    private final Supplier<Ocpp16WsClient> wsClientSupplier;

    public ChargerRequestConsumer(String cpCsId, Supplier<Ocpp16WsClient> wsClientSupplier) {
        this.cpCsId = cpCsId;
        this.wsClientSupplier = wsClientSupplier;
    }

    @Override
    public void onMessage(Message message) {
        Ocpp16WsClient ws = wsClientSupplier.get();
        if (ws == null || !ws.isOpenNow()) {
            // WS 미연결 — 예외를 던져 기본 정책(requeue)에 따라 재시도되도록 함
            throw new IllegalStateException("WS 미연결 상태 — 요청 requeue cpCsId=" + cpCsId);
        }
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        LOGGER.debug("[proxy-eai] req → target cpCsId={} body={}", cpCsId, body);
        ws.send(body);
    }
}
