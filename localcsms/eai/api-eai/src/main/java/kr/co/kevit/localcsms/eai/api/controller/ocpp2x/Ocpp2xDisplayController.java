package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessage;
import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessageContent;
import kr.co.kevit.localcsms.charger.process.DisplayMessageService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OCPP 2.0.1 Display Message 그룹 엔드포인트.
 *
 * POST     /ocpp2x/setDisplayMessage?chargingStationIdentity=  (body: message 중첩 구조)
 * GET/POST /ocpp2x/getDisplayMessages?chargingStationIdentity=&requestId=&id=&priority=&state=
 * GET/POST /ocpp2x/clearDisplayMessage?chargingStationIdentity=&id=
 */
@RestController
@RequestMapping("/ocpp2x")
public class Ocpp2xDisplayController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp2xDisplayController.class);

    /** OCPP MessageStateEnumType → DB 공통코드 DMST00 */
    private static final Map<String, String> STATE_CODE_MAP = new HashMap<>();
    /** OCPP MessageFormatEnumType → DB 공통코드 DMFM00 */
    private static final Map<String, String> FORMAT_CODE_MAP = new HashMap<>();

    static {
        STATE_CODE_MAP.put("Charging",    "DMST01");
        STATE_CODE_MAP.put("Faulted",     "DMST02");
        STATE_CODE_MAP.put("Idle",        "DMST03");
        STATE_CODE_MAP.put("Unavailable", "DMST04");
        STATE_CODE_MAP.put("Suspended",   "DMST05");
        STATE_CODE_MAP.put("Discharging", "DMST06");

        FORMAT_CODE_MAP.put("ASCII",   "DMFM01");
        FORMAT_CODE_MAP.put("HTML",    "DMFM02");
        FORMAT_CODE_MAP.put("URI",     "DMFM03");
        FORMAT_CODE_MAP.put("UTF8",    "DMFM04");
        FORMAT_CODE_MAP.put("QRCODE",  "DMFM05");
    }

    private final Daemon2xClient daemonClient;
    private final SequenceService sequenceService;
    private final DisplayMessageService displayMessageService;

    public Ocpp2xDisplayController(Daemon2xClient daemonClient,
                                   SequenceService sequenceService,
                                   DisplayMessageService displayMessageService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.displayMessageService = displayMessageService;
    }

    /**
     * body: { "message": { "priority": ..., "state": ..., "message": { "format": ..., "content": ... } } }
     * 1) messageId 자동 생성 → payload 에 주입
     * 2) DisplayMessage / DisplayMessageContent 빌드 후 DB 저장
     * 3) ocpp20-daemon 호출
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/setDisplayMessage")
    public ResponseEntity<ApiResult> setDisplayMessage(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> payload) {

        int idx = chargingStationIdentity.lastIndexOf('-');
        if (idx < 0) {
            return ResponseEntity.ok(ApiResult.rejected("chargingStationIdentity 형식 오류"));
        }
        // payload 에 id 주입
        Object msgObj = payload.get("message");
        if (!(msgObj instanceof Map)) {
            return ResponseEntity.ok(ApiResult.rejected("message 필드 누락"));
        }

        String cpId = chargingStationIdentity.substring(0, idx);
        String csId = chargingStationIdentity.substring(idx + 1);

        Map<String, Object> msgMap = (Map<String, Object>) msgObj;

        // id 전달 여부에 따라 신규/수정 분기
        Object existingId = msgMap.get("id");
        boolean isUpdate = (existingId instanceof Number);
        int messageId;
        if (isUpdate) {
            messageId = ((Number) existingId).intValue();
        } else {
            messageId = sequenceService.generateDisplayMessageSeq();
            msgMap.put("id", messageId);
        }

        // DisplayMessage 빌드
        DisplayMessage dm = new DisplayMessage();
        dm.setMessageId(messageId);
        dm.setCpId(cpId);
        dm.setCsId(csId);
        dm.setPriority((String) msgMap.get("priority"));
        dm.setStatus(STATE_CODE_MAP.get(msgMap.get("state")));
        dm.setRcId((String) msgMap.get("transactionId"));

        // 날짜 파싱 (ISO-8601 문자열)
        dm.setStartDate(parseDate((String) msgMap.get("startDateTime")));
        dm.setEndDate(parseDate((String) msgMap.get("endDateTime")));

        // display (ComponentType) - 선택 필드
        Object dispObj = msgMap.get("display");
        if (dispObj instanceof Map) {
            Map<String, Object> disp = (Map<String, Object>) dispObj;
            dm.setDisplayName((String) disp.get("name"));
            dm.setDisplayInstance((String) disp.get("instance"));
            Object evseObj = disp.get("evse");
            if (evseObj instanceof Map) {
                Map<String, Object> evse = (Map<String, Object>) evseObj;
                if (evse.get("id") instanceof Number)
                    dm.setDisplayEvseId(((Number) evse.get("id")).intValue());
                if (evse.get("connectorId") instanceof Number)
                    dm.setDisplayConnId(((Number) evse.get("connectorId")).intValue());
            }
        }

        // DisplayMessageContent 빌드 (단일 message → 1건)
        Object innerMsgObj = msgMap.get("message");
        if (innerMsgObj instanceof Map) {
            Map<String, Object> inner = (Map<String, Object>) innerMsgObj;
            DisplayMessageContent content = new DisplayMessageContent();
            content.setMessageId(messageId);
            String lang = (String) inner.get("language");
            content.setMsgLanguage(lang != null ? lang.toLowerCase() : "ko");
            content.setMsgFormat(FORMAT_CODE_MAP.getOrDefault((String) inner.get("format"), "DMFM04"));
            content.setMsgContent((String) inner.get("content"));

            List<DisplayMessageContent> contents = new ArrayList<>();
            contents.add(content);
            dm.setContents(contents);
        }

        // DB 저장
        try {
            dm.setWriter(new Writer(StringConstants.SYSTEM_EMPLOYEE));
            if (isUpdate) {
                displayMessageService.updateMessage(dm);
                if (dm.getContents() != null) {
                    for (DisplayMessageContent c : dm.getContents()) {
                        displayMessageService.saveContent(c);
                    }
                }
                log.info("[SetDisplayMessage] DB 수정 완료: cpId={} csId={} messageId={}", cpId, csId, messageId);
            } else {
                displayMessageService.saveMessageWithContents(dm);
                log.info("[SetDisplayMessage] DB 저장 완료: cpId={} csId={} messageId={}", cpId, csId, messageId);
            }
        } catch (Exception e) {
            log.error("[SetDisplayMessage] DB 저장 실패: cpId={} csId={} error={}", cpId, csId, e.getMessage());
        }

        // ocpp20-daemon 호출
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "SetDisplayMessage", payload, null));
    }

    private java.util.Date parseDate(String isoStr) {
        if (isoStr == null || isoStr.isEmpty()) return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(isoStr);
        } catch (Exception e) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(isoStr);
            } catch (Exception ex) {
                log.warn("[SetDisplayMessage] 날짜 파싱 실패: {}", isoStr);
                return null;
            }
        }
    }

    /** requestId (required), id (optional, 복수: &id=1&id=2), priority (optional), state (optional) */
    @RequestMapping(value = "/getDisplayMessages", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> getDisplayMessages(
            @RequestParam String chargingStationIdentity,
            @RequestParam(required = false) List<Integer> id,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String state) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("requestId", sequenceService.generateDisplayMessagesSeq());
        if (id != null && !id.isEmpty()) payload.put("id", id);
        if (priority != null) payload.put("priority", priority);
        if (state != null) payload.put("state", state);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetDisplayMessages", payload, null));
    }

    /** id (required) */
    @RequestMapping(value = "/clearDisplayMessage", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> clearDisplayMessage(
            @RequestParam String chargingStationIdentity,
            @RequestParam Integer id) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ClearDisplayMessage", payload, null));
    }
}
