package kr.co.kevit.localcsms.ocpp20.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.List;

/**
 * OCPP 2.x 서브프로토콜 협상 핸들러.
 *
 * 지원 프로토콜: ocpp2.1, ocpp2.0.1
 * 우선순위    : ocpp2.1 > ocpp2.0.1 > fallback(부모 위임)
 *
 * 선택 규칙:
 *   - 클라이언트 목록에 "ocpp2.1"   이 있으면 → "ocpp2.1"   반환
 *   - 클라이언트 목록에 "ocpp2.0.1" 이 있으면 → "ocpp2.0.1" 반환
 *   - 그 외 → 부모 클래스(기본 협상 로직) 위임
 *
 * 예시:
 *   ocpp2.0.1,ocpp2.1  → ocpp2.1
 *   ocpp1.6,ocpp2.0.1  → ocpp2.0.1
 *   ocpp1.7,ocpp2.1    → ocpp2.1
 */
public class Ocpp20HandshakeHandler extends DefaultHandshakeHandler {

    private static final Logger log   = LoggerFactory.getLogger(Ocpp20HandshakeHandler.class);
    private static final String V21   = "ocpp2.1";
    private static final String V201  = "ocpp2.0.1";

    @Override
    protected String selectProtocol(List<String> requestedProtocols, WebSocketHandler wsHandler) {
        if (requestedProtocols == null) {
            return super.selectProtocol(null, wsHandler);
        }
        if (requestedProtocols.contains(V21)) {
            log.debug("[OCPP20] 서브프로토콜 협상: requested={} → selected={}", requestedProtocols, V21);
            return V21;
        }
        if (requestedProtocols.contains(V201)) {
            log.debug("[OCPP20] 서브프로토콜 협상: requested={} → selected={}", requestedProtocols, V201);
            return V201;
        }
        String selected = super.selectProtocol(requestedProtocols, wsHandler);
        log.debug("[OCPP20] 서브프로토콜 협상(fallback): requested={} → selected={}", requestedProtocols, selected);
        return selected;
    }
}
