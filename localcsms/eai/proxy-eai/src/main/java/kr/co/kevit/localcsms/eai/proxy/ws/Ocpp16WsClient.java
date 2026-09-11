package kr.co.kevit.localcsms.eai.proxy.ws;

import java.net.URI;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import javax.net.ssl.SSLSocketFactory;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.protocols.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OCPP1.6 원문(pass-through) 전용 mTLS WebSocket 클라이언트.
 *
 * <p>
 * 메시지 파싱을 전혀 하지 않는다 — 수신한 텍스트 프레임을 그대로 콜백으로 넘긴다.
 * {@code Sec-WebSocket-Protocol: ocpp1.6} 서브프로토콜을 사용한다.
 * </p>
 *
 * @author bckim
 */
public class Ocpp16WsClient extends WebSocketClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ocpp16WsClient.class);

    private final String cpCsId;
    private final Consumer<String> onMessage;
    private final CountDownLatch closeLatch = new CountDownLatch(1);

    public Ocpp16WsClient(String cpCsId, URI serverUri, SSLSocketFactory sslSocketFactory,
            Consumer<String> onMessage) {
        super(serverUri, new Draft_6455(Collections.emptyList(),
                Collections.<IProtocol>singletonList(new Protocol("ocpp1.6"))));
        this.cpCsId = cpCsId;
        this.onMessage = onMessage;
        if ("wss".equalsIgnoreCase(serverUri.getScheme()) && sslSocketFactory != null) {
            this.setSocketFactory(sslSocketFactory);
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        LOGGER.info("[proxy-eai] WS 연결 성공 cpCsId={}", cpCsId);
    }

    @Override
    public void onMessage(String message) {
        // 파싱 없이 그대로 전달 — 호출측(ChargerSession)이 RabbitMQ 응답 큐로 재발행한다.
        onMessage.accept(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.warn("[proxy-eai] WS 연결 종료 cpCsId={} code={} reason={} remote={}", cpCsId, code, reason, remote);
        closeLatch.countDown();
    }

    @Override
    public void onError(Exception ex) {
        LOGGER.warn("[proxy-eai] WS 오류 cpCsId={}: {}", cpCsId, ex.getMessage());
    }

    /** 연결이 끊길 때까지 대기 (세션 스레드의 메인 루프에서 사용). */
    public void awaitClose() throws InterruptedException {
        closeLatch.await();
    }

    public boolean isOpenNow() {
        return this.getReadyState() == ReadyState.OPEN;
    }
}
