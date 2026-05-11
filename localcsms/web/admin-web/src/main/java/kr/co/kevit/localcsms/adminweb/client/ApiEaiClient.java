package kr.co.kevit.localcsms.adminweb.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * api-eai bypass 클라이언트.
 * api-eai 의 /ocpp16/bypass, /ocpp2x/bypass 엔드포인트를 호출한다.
 */
@Component
public class ApiEaiClient {

    private static final Logger log = LoggerFactory.getLogger(ApiEaiClient.class);

    private final RestTemplate restTemplate;

    @Value("${api-eai.url}")
    private String apiEaiUrl;

    @Value("${api-eai.auth-token}")
    private String authToken;

    public ApiEaiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * ocpp16-daemon 으로 명령 bypass 전송.
     *
     * @param cpCsId  충전기 ID
     * @param action  OCPP 액션명
     * @param payload 페이로드
     * @return 응답 Map (status, data, message)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> send(String cpCsId, String action, Object payload) {
        String url = apiEaiUrl + "/ocpp16/bypass/" + cpCsId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authToken);

        Map<String, Object> body = Map.of(
                "action", action,
                "payload", payload != null ? payload : Map.of());

        log.info("[EAI] bypass 호출: cpCsId={} action={} url={}", cpCsId, action, url);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    Map.class);

            Map<String, Object> result = response.getBody();
            if (result == null) {
                return Map.of("status", "rejected", "message", "응답 없음");
            }
            log.info("[EAI] bypass 결과: cpCsId={} status={}", cpCsId, result.get("status"));
            return result;
        } catch (Exception e) {
            log.error("[EAI] bypass 호출 실패: cpCsId={} action={} error={}", cpCsId, action, e.getMessage());
            return Map.of("status", "rejected", "message", e.getMessage());
        }
    }

    /**
     * ocpp20-daemon 으로 명령 bypass 전송.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> send2x(String cpCsId, String action, Object payload) {
        String url = apiEaiUrl + "/ocpp2x/bypass/" + cpCsId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authToken);

        Map<String, Object> body = Map.of(
                "action", action,
                "payload", payload != null ? payload : Map.of());

        log.info("[EAI] 2x bypass 호출: cpCsId={} action={} url={}", cpCsId, action, url);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    Map.class);

            Map<String, Object> result = response.getBody();
            if (result == null) {
                return Map.of("status", "rejected", "message", "응답 없음");
            }
            log.info("[EAI] 2x bypass 결과: cpCsId={} status={}", cpCsId, result.get("status"));
            return result;
        } catch (Exception e) {
            log.error("[EAI] 2x bypass 호출 실패: cpCsId={} action={} error={}", cpCsId, action, e.getMessage());
            return Map.of("status", "rejected", "message", e.getMessage());
        }
    }

    /**
     * 등록된 충전기 세션 목록 조회 (OCPP 1.6).
     */
    @SuppressWarnings("unchecked")
    public List<String> getSessions() {
        String url = apiEaiUrl + "/ocpp/bypass/sessions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class);

            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("connected")) {
                return (List<String>) body.get("connected");
            }
            return List.of();
        } catch (Exception e) {
            log.error("[EAI] sessions 조회 실패: error={}", e.getMessage());
            return List.of();
        }
    }
}
