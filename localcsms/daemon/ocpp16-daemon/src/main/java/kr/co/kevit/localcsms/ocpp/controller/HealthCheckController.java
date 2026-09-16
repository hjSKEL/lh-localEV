package kr.co.kevit.localcsms.ocpp.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 외부 헬스체크용 — {@code GET /checkHealth} 호출 시 {@code {"status":"OK"}} 를 반환한다.
 *
 * <p>
 * 메인 서버 포트(실행 인자 2번째, 기본 8091)는 Security Profile(SP0~SP3)에 따라 TLS/mTLS 가
 * 걸릴 수 있어 평문으로 접근 못 할 수 있다 — 그래서 이 엔드포인트는 별도 고정 평문 포트(기본
 * 8090, {@link kr.co.kevit.localcsms.ocpp.config.HealthCheckServerConfig} 가 추가하는 커넥터)
 * 로도 항상 응답한다.
 * </p>
 *
 * @author bckim
 */
@RestController
public class HealthCheckController {

    @GetMapping("/checkHealth")
    public Map<String, String> checkHealth() {
        return Map.of("status", "OK");
    }
}
