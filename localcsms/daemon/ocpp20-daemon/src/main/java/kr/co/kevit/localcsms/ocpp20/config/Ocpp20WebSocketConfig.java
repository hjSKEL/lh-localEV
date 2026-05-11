package kr.co.kevit.localcsms.ocpp20.config;

import kr.co.kevit.localcsms.ocpp20.handler.Ocpp20ProtocolHandshakeInterceptor;
import kr.co.kevit.localcsms.ocpp20.handler.Ocpp20WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * OCPP 2.x WebSocket 엔드포인트 등록.
 *
 * 충전기 접속 URL: ws://host:9001/ocpp20/{cpId}
 * 서브프로토콜 : ocpp2.x
 *
 * 클라이언트가 Sec-WebSocket-Protocol 에 여러 프로토콜을 나열하더라도
 * "ocpp2.x" 이 포함되어 있으면 반드시 "ocpp2.x" 로 응답합니다.
 * → Ocpp20HandshakeHandler 참조
 */
@Configuration
@EnableWebSocket
public class Ocpp20WebSocketConfig implements WebSocketConfigurer {

    private final Ocpp20WebSocketHandler ocpp20WebSocketHandler;
    private final Ocpp20ProtocolHandshakeInterceptor protocolInterceptor;

    public Ocpp20WebSocketConfig(Ocpp20WebSocketHandler ocpp20WebSocketHandler,
            Ocpp20ProtocolHandshakeInterceptor protocolInterceptor) {
        this.ocpp20WebSocketHandler = ocpp20WebSocketHandler;
        this.protocolInterceptor = protocolInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ocpp20WebSocketHandler, "/ocpp20/{cpId}")
                .setAllowedOrigins("*")
                .setHandshakeHandler(new Ocpp20HandshakeHandler())
                .addInterceptors(protocolInterceptor);
    }
}
