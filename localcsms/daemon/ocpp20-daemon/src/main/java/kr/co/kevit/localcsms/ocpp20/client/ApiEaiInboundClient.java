package kr.co.kevit.localcsms.ocpp20.client;

import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;

/**
 * ocpp20-daemon → api-eai 인바운드 콜백 클라이언트.
 *
 * <p>
 * CS 로부터 받은 OCPP CALL 중 자동 후속 push 결정이 필요한 메시지(예: NotifyEVChargingNeeds)
 * 를 api-eai 의 {@code /ocpp2x/inbound/{cpCsId}/{action}} 으로 forward 한다.
 * </p>
 *
 * <p>
 * <b>Fire-and-forget</b>: HTTP 응답을 기다리지 않는다. daemon 의 OCPP 응답 시퀀스
 * (즉시 CALLRESULT 송신)를 막지 않기 위함. 처리 결과(SetChargingProfile 등) 는
 * api-eai 가 별도 REST 호출(Daemon2xClient → Ocpp20CommandController) 로 다시 보낸다.
 * </p>
 */
@Component
public class ApiEaiInboundClient {

    private static final Logger log = LoggerFactory.getLogger(ApiEaiInboundClient.class);

    private final String baseUrl;
    private final RestTemplate restTemplate;
    private final ExecutorService executor;

    public ApiEaiInboundClient(@Value("${api.eai.base-url:http://127.0.0.1:9000}") String baseUrl,
            @Value("${api.eai.inbound-timeout-ms:3000}") int timeoutMs) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(timeoutMs))
                .setReadTimeout(Duration.ofMillis(timeoutMs))
                .build();
        ThreadFactory tf = new ThreadFactory() {
            private final AtomicInteger n = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "api-eai-inbound-" + n.incrementAndGet());
                t.setDaemon(true);
                return t;
            }
        };
        this.executor = Executors.newCachedThreadPool(tf);
        log.info("[OCPP20] ApiEaiInboundClient init baseUrl={} timeoutMs={}", baseUrl, timeoutMs);
    }

    /**
     * api-eai 로 인바운드 메시지 비동기 forward.
     *
     * @param cpCsId  "cpId-csId" 결합 식별자
     * @param action  OCPP action 이름 (예: NotifyEVChargingNeeds)
     * @param payload OCPP 메시지 payload (JsonNode 그대로)
     */
    public void forward(String cpCsId, String action, JsonNode payload) {
        executor.submit(() -> doForward(cpCsId, action, payload));
    }

    private void doForward(String cpCsId, String action, JsonNode payload) {
        try {
            String url = baseUrl + "/ocpp2x/inbound/" + cpCsId + "/" + action;
            Map<String, Object> body = new HashMap<>(2);
            body.put("cpCsId", cpCsId);
            body.put("payload", payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(url, entity, String.class);
            log.info("[OCPP20] api-eai inbound forward 성공 cpCsId={} action={}", cpCsId, action);
        } catch (Exception e) {
            log.warn("[OCPP20] api-eai inbound forward 실패 cpCsId={} action={} error={}",
                    cpCsId, action, e.getMessage());
        }
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }
}
