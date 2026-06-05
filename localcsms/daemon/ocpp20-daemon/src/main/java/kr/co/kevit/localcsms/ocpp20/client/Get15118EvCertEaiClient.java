package kr.co.kevit.localcsms.ocpp20.client;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * ocpp20-daemon → api-eai Get15118EVCertificate(ISO 15118 PnC) 전달 클라이언트.
 *
 * <p>
 * daemon 은 비즈니스 로직 없이 <b>OCPP 2.1 Get15118EVCertificate 요청 객체를 그대로</b> api-eai 로 보내고,
 * api-eai 가 만들어 준 <b>응답 객체를 그대로</b> 받아 반환한다. (Hubject/계약인증서 풀 호출,
 * exiResponse·remainingContracts·status 결정 등 모든 처리는 api-eai 가 소유)
 * </p>
 *
 * <p>호출 URL 에 충전기 식별자(cpCsId)를 path 로 포함한다: {@code .../get15118EVCertificate/{cpCsId}}</p>
 */
@Component
public class Get15118EvCertEaiClient {

    private static final Logger log = LoggerFactory.getLogger(Get15118EvCertEaiClient.class);

    /** api-eai Get15118EVCertificate 수신(daemon → api-eai inbound) 엔드포인트 */
    private static final String PATH = "/ocpp2x/inbound/certificate/get15118EVCertificate";

    private final String urlPrefix;
    private final RestTemplate restTemplate;

    public Get15118EvCertEaiClient(@Value("${api.eai.base-url:http://127.0.0.1:9000}") String baseUrl,
            @Value("${api.eai.contract-cert-timeout-ms:5000}") int timeoutMs) {
        this.urlPrefix = baseUrl + PATH;
        this.restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(timeoutMs))
                .setReadTimeout(Duration.ofMillis(timeoutMs))
                .build();
        log.info("[OCPP20] Get15118EvCertEaiClient init urlPrefix={} timeoutMs={}", urlPrefix, timeoutMs);
    }

    /**
     * Get15118EVCertificate 요청을 api-eai 로 그대로 전달하고 응답을 그대로 받아온다.
     *
     * @param cpCsId  충전기 식별자("cpId-csId"). 호출 URL 에 path 로 포함된다.
     * @param request OCPP 2.1 Get15118EVCertificate 요청 객체
     * @return api-eai 가 반환한 Get15118EVCertificate 응답 객체. 호출 실패 시 null
     */
    public kr.co.kevit.ocpp201.response.Get15118EVCertificate getEVCertificate(String cpCsId,
            kr.co.kevit.ocpp201.request.Get15118EVCertificate request) {
        String url = urlPrefix + "/" + cpCsId;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<kr.co.kevit.ocpp201.request.Get15118EVCertificate> entity = new HttpEntity<>(request, headers);
            return restTemplate.postForObject(url, entity, kr.co.kevit.ocpp201.response.Get15118EVCertificate.class);
        } catch (Exception e) {
            log.error("[OCPP20] api-eai Get15118EVCertificate 호출 실패 url={} error={}", url, e.getMessage(), e);
            return null;
        }
    }
}
