package kr.co.kevit.localcsms.ocpp20.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * WebSocket 핸드셰이크 전 Sec-WebSocket-Protocol 헤더 검증.
 * ocpp2.0.1 또는 ocpp2.1 이 포함되지 않으면 HTTP 403 을 반환하고 연결을 차단한다.
 */
@Component
public class Ocpp20ProtocolHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(Ocpp20ProtocolHandshakeInterceptor.class);
    private static final List<String> SUPPORTED_PROTOCOLS = List.of("ocpp2.0.1", "ocpp2.1");

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        List<String> headerValues = request.getHeaders().get("Sec-WebSocket-Protocol");

        if (headerValues == null || headerValues.isEmpty()) {
            log.warn("[OCPP20] 연결 거부 — Sec-WebSocket-Protocol 헤더 없음: uri={}", request.getURI());
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }

        // 헤더 값은 쉼표 구분 목록일 수 있음: "ocpp0.1, ocpp2.0.1"
        boolean hasSupported = headerValues.stream()
                .flatMap(v -> Arrays.stream(v.split(",")))
                .map(String::trim)
                .anyMatch(SUPPORTED_PROTOCOLS::contains);

        if (!hasSupported) {
            log.warn("[OCPP20] 연결 거부 — ocpp2.0.1/ocpp2.1 미포함: uri={} protocols={}", request.getURI(), headerValues);
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }

        log.debug("[OCPP20] 프로토콜 검증 통과: uri={} protocols={}", request.getURI(), headerValues);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, @Nullable Exception exception) {
    }
}
