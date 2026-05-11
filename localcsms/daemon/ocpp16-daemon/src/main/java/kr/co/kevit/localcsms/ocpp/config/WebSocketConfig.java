package kr.co.kevit.localcsms.ocpp.config;

import kr.co.kevit.localcsms.ocpp.handler.OcppProtocolHandshakeInterceptor;
import kr.co.kevit.localcsms.ocpp.handler.OcppWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final OcppWebSocketHandler ocppWebSocketHandler;
    private final OcppProtocolHandshakeInterceptor protocolInterceptor;

    public WebSocketConfig(OcppWebSocketHandler ocppWebSocketHandler,
                           OcppProtocolHandshakeInterceptor protocolInterceptor) {
        this.ocppWebSocketHandler = ocppWebSocketHandler;
        this.protocolInterceptor = protocolInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ocppWebSocketHandler, "/ocpp16/{cpId}")
                .setAllowedOrigins("*")
                .addInterceptors(protocolInterceptor);
    }
}
