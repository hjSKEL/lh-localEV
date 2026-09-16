package kr.co.kevit.localcsms.eai.proxy.mq;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * LH 모드에서 충전기의 StatusNotification.req 원문을 OCPP1.6 {@code DataTransfer.req}
 * (vendorId=kr.co.kevit, messageId=ChildStatusRpt) 로 변환한다.
 *
 * <p>
 * {@code chargeBoxSerialNumber} 는 StatusNotification.req 페이로드에 없는 필드라 호출부에서
 * 전달받은 {@code cpCsId} 를 그대로 사용한다. {@code chargeStatus} 는 원본 OCPP status 값을
 * 매핑 없이 그대로 전달한다.
 * </p>
 *
 * @author bckim
 */
public final class ChildStatusRptTransformer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String VENDOR_ID = "kr.co.kevit";
    private static final String MESSAGE_ID = "ChildStatusRpt";

    private ChildStatusRptTransformer() {
    }

    /**
     * @param rawStatusNotificationCall 원문 OCPP CALL(2) 배열 JSON 문자열
     *                                    (예: {@code [2,"uid","StatusNotification",{...}]})
     * @param cpCsId                     충전기 식별자(cpId-csId) — chargeBoxSerialNumber 로 사용
     * @return 변환된 DataTransfer.req CALL(2) 배열 JSON 문자열
     */
    public static String transform(String rawStatusNotificationCall, String cpCsId) throws Exception {
        JsonNode callArray = MAPPER.readTree(rawStatusNotificationCall);
        if (!callArray.isArray() || callArray.size() < 4) {
            throw new IllegalArgumentException("StatusNotification CALL 형식 오류: " + rawStatusNotificationCall);
        }
        JsonNode payload = callArray.get(3);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("chargeBoxSerialNumber", cpCsId);
        data.put("connectorId", intOrZero(payload, "connectorId"));
        data.put("chrgChngDttm", textOrEmpty(payload, "timestamp"));
        data.put("chargeStatus", textOrEmpty(payload, "status"));

        Map<String, Object> dataTransferPayload = new LinkedHashMap<>();
        dataTransferPayload.put("vendorId", VENDOR_ID);
        dataTransferPayload.put("messageId", MESSAGE_ID);
        dataTransferPayload.put("data", MAPPER.writeValueAsString(data));

        List<Object> call = List.of(2, UUID.randomUUID().toString(), "DataTransfer", dataTransferPayload);
        return MAPPER.writeValueAsString(call);
    }

    private static String textOrEmpty(JsonNode payload, String field) {
        JsonNode node = payload.get(field);
        return node != null && !node.isNull() ? node.asText() : "";
    }

    private static int intOrZero(JsonNode payload, String field) {
        JsonNode node = payload.get(field);
        return node != null && node.isInt() ? node.asInt() : 0;
    }
}
