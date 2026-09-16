package kr.co.kevit.localcsms.eai.proxy.session;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kevit.localcsms.system.entity.domain.ConnConfig;
import kr.co.kevit.localcsms.system.process.ConnConfigService;

/**
 * CS0 가 LH/CPO 로부터 받은 {@code ChangeConfiguration.req} 를 처리해 {@code ConnConfig}(TB_SYCN001)
 * 에 반영하고 {@code ChangeConfiguration.conf} 를 조립한다.
 *
 * <p>
 * 지원 key:
 * </p>
 * <ul>
 * <li>{@code LocalPreAuthorize} — value가 {@code "true"}면 LH 모드(OPMD01), {@code "false"}면
 * CPO 모드(OPMD02)로 {@code localOperationType} 반영</li>
 * <li>{@code CPOCSMSUrl} — {@code cpoCsmsAddress} 에 반영</li>
 * </ul>
 *
 * <p>
 * 다른 세션/충전기 기동 여부는 재기동으로만 반영된다(proxy-eai 의 기존 정책과 동일 — 동적 재구성 없음).
 * </p>
 *
 * @author bckim
 */
public final class ChangeConfigurationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeConfigurationHandler.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String KEY_LOCAL_PRE_AUTHORIZE = "LocalPreAuthorize";
    private static final String KEY_CPO_CSMS_URL = "CPOCSMSUrl";

    private static final String OPERATION_TYPE_LH = "OPMD01";
    private static final String OPERATION_TYPE_CPO = "OPMD02";

    private static final String UPD_ID = "CS0";

    private ChangeConfigurationHandler() {
    }

    /**
     * @param rawChangeConfigurationCall 원문 OCPP CALL(2) 배열 JSON 문자열
     *                                    (예: {@code [2,"uid","ChangeConfiguration",{"key":..,"value":..}]})
     * @return 조립된 ChangeConfiguration.conf CALLRESULT(3) 배열 JSON 문자열
     */
    public static String handle(String rawChangeConfigurationCall, ConnConfigService connConfigService)
            throws Exception {
        JsonNode callArray = MAPPER.readTree(rawChangeConfigurationCall);
        String uniqueId = callArray.get(1).asText();
        JsonNode payload = callArray.get(3);
        String key = payload.has("key") ? payload.get("key").asText() : "";
        String value = payload.has("value") ? payload.get("value").asText() : "";

        String status;
        try {
            applyConfiguration(key, value, connConfigService);
            status = "Accepted";
        } catch (Exception e) {
            LOGGER.warn("[proxy-eai][CS0] ChangeConfiguration 반영 실패 key={} value={}: {}", key, value, e.getMessage());
            status = "Rejected";
        }

        List<Object> call = List.of(3, uniqueId, Map.of("status", status));
        return MAPPER.writeValueAsString(call);
    }

    private static void applyConfiguration(String key, String value, ConnConfigService connConfigService) {
        ConnConfig connConfig = connConfigService.retrieveConnConfig();
        if (connConfig == null) {
            throw new IllegalStateException("ConnConfig(TB_SYCN001) 조회 실패");
        }

        if (KEY_LOCAL_PRE_AUTHORIZE.equalsIgnoreCase(key)) {
            String operationType;
            if ("true".equalsIgnoreCase(value)) {
                operationType = OPERATION_TYPE_LH;
            } else if ("false".equalsIgnoreCase(value)) {
                operationType = OPERATION_TYPE_CPO;
            } else {
                throw new IllegalArgumentException("LocalPreAuthorize 값이 true/false 가 아님: " + value);
            }
            connConfig.setLocalOperationType(operationType);
        } else if (KEY_CPO_CSMS_URL.equalsIgnoreCase(key)) {
            connConfig.setCpoCsmsAddress(value);
        } else {
            throw new IllegalArgumentException("지원하지 않는 key: " + key);
        }

        connConfig.setUpdId(UPD_ID);
        connConfigService.modifyConnConfig(connConfig);
        LOGGER.info("[proxy-eai][CS0] ConnConfig 반영 완료 key={} value={}", key, value);
    }
}
