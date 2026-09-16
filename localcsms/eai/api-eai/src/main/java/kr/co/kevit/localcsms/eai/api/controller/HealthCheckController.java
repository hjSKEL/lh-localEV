package kr.co.kevit.localcsms.eai.api.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 외부 헬스체크용 — {@code GET /checkHealth} 호출 시 {@code {"status":"OK"}} 를 반환한다.
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
