package kr.co.kevit.localcsms.eai.proxy.mq;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * LH 모드에서 충전기의 BootNotification.req 원문을 OCPP1.6 {@code DataTransfer.req}
 * (vendorId=kr.co.kevit, messageId=ChildBootNotification) 로 변환한다.
 *
 * <p>
 * {@code DataTransfer.req.data} 는 스펙상 string 타입이므로, 추출한 3개 필드는 중첩 객체가 아니라
 * JSON 문자열로 직렬화해서 담는다.
 * </p>
 *
 * @author bckim
 */
public final class ChildBootNotificationTransformer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String VENDOR_ID = "kr.co.kevit";
    private static final String MESSAGE_ID = "ChildBootNotification";

    private ChildBootNotificationTransformer() {
    }

    /**
     * @param rawBootNotificationCall 원문 OCPP CALL(2) 배열 JSON 문자열
     *                                 (예: {@code [2,"uid","BootNotification",{...}]})
     * @return 변환된 DataTransfer.req CALL(2) 배열 JSON 문자열
     */
    public static String transform(String rawBootNotificationCall) throws Exception {
        JsonNode callArray = MAPPER.readTree(rawBootNotificationCall);
        if (!callArray.isArray() || callArray.size() < 4) {
            throw new IllegalArgumentException("BootNotification CALL 형식 오류: " + rawBootNotificationCall);
        }
        JsonNode payload = callArray.get(3);

        Map<String, String> data = new LinkedHashMap<>();
        data.put("chargeBoxSerialNumber", textOrEmpty(payload, "chargeBoxSerialNumber"));
        data.put("chargePointModel", textOrEmpty(payload, "chargePointModel"));
        data.put("chargePointVendor", textOrEmpty(payload, "chargePointVendor"));

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
}
