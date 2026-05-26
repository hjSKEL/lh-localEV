package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.system.entity.domain.DaemonAccess;
import kr.co.kevit.localcsms.system.process.DaemonAccessService;
import kr.co.kevit.ocpp201.request.RequestStartTransaction;
import kr.co.kevit.ocpp201.request.ReserveNow;
import kr.co.kevit.ocpp201.request.GetLog;
import kr.co.kevit.ocpp201.request.UpdateFirmware;
import kr.co.kevit.ocpp201.request.CustomerInformation;
import kr.co.kevit.ocpp201.request.GetDisplayMessages;
import kr.co.kevit.ocpp201.request.SetChargingProfile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * OCPP 2.x Bypass Controller.
 * admin-web 에서 호출하여 ocpp20-daemon 으로 그대로 전달한다.
 *
 * POST /ocpp2x/bypass/{cpCsId} { "action": "Reset", "payload": { "type":
 * "Immediate" } }
 * GET /ocpp2x/bypass/sessions
 */
@RestController
@RequestMapping("/ocpp2x/bypass")
public class Ocpp2xBypassController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp2xBypassController.class);

    private final Daemon2xClient daemonClient;
    private final DaemonAccessService daemonAccessService;
    private final SequenceService sequenceService;

    /**
     * OCPP 2.1 schema 호환을 위해 다음 설정 적용:
     * - FAIL_ON_UNKNOWN_PROPERTIES=false : OCPP 1.6 비표준 필드(chargingProfileId 등) 자동 무시
     * - NON_NULL : null 필드 직렬화 제외 — 2.1 전용 boolean(useLocalTime/evseSleep/preconditioningRequest) 은
     *             도메인이 Boolean 으로 선언되어 미지정 시 null → omit, true/false 명시 시 그대로 전송
     */
    private final ObjectMapper objectMapper = buildObjectMapper();

    private static ObjectMapper buildObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public Ocpp2xBypassController(Daemon2xClient daemonClient,
            SequenceService sequenceService,
            DaemonAccessService daemonAccessService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.daemonAccessService = daemonAccessService;
    }

    /**
     * 명령 bypass — ocpp20-daemon 으로 그대로 전달.
     */
    @PostMapping("/{cpCsId}")
    public ResponseEntity<ApiResult> bypassCommand(
            @PathVariable String cpCsId,
            @RequestBody Map<String, Object> request) {

        String action = (String) request.get("action");
        Object payload = request.get("payload");

        log.info("[OCPP2X BYPASS] cpCsId={} action={}", cpCsId, action);

        // payload 는 Jackson 이 LinkedHashMap 으로 역직렬화한 상태이므로
        // 시퀀스 자동발급이 필요한 액션에 한해 타입 변환 + 시퀀스 채번 후
        // writeValueAsString → readValue(Map) 패턴으로 NON_NULL/NON_DEFAULT/MixIn 모두 강제 적용된
        // 청결한 Map 으로 재구성하여 daemon 으로 전달.
        try {
            switch (action) {
                case "RequestStartTransaction": {
                    RequestStartTransaction req = objectMapper.convertValue(payload, RequestStartTransaction.class);
                    if (req.getRemoteStartId() == 0) {
                        req.setRemoteStartId(sequenceService.generateRemoteStartSeq());
                    }
                    payload = toCleanMap(req);
                    break;
                }
                case "ReserveNow": {
                    ReserveNow req2 = objectMapper.convertValue(payload, ReserveNow.class);
                    if (req2.getId() == 0) {
                        req2.setId(sequenceService.generateReservSeq());
                    }
                    payload = toCleanMap(req2);
                    break;
                }
                case "GetLog": {
                    GetLog req3 = objectMapper.convertValue(payload, GetLog.class);
                    if (req3.getRequestId() == 0) {
                        req3.setRequestId(sequenceService.generateLogSeq());
                    }
                    payload = toCleanMap(req3);
                    break;
                }
                case "UpdateFirmware": {
                    UpdateFirmware req4 = objectMapper.convertValue(payload, UpdateFirmware.class);
                    if (req4.getRequestId() == 0) {
                        req4.setRequestId(sequenceService.generateFirmwareSeq());
                    }
                    payload = toCleanMap(req4);
                    break;
                }
                case "CustomerInformation": {
                    CustomerInformation req5 = objectMapper.convertValue(payload, CustomerInformation.class);
                    if (req5.getRequestId() == 0) {
                        req5.setRequestId(sequenceService.generateCustomerInformationSeq());
                    }
                    payload = toCleanMap(req5);
                    break;
                }
                case "GetDisplayMessages": {
                    GetDisplayMessages req6 = objectMapper.convertValue(payload, GetDisplayMessages.class);
                    if (req6.getRequestId() == 0) {
                        req6.setRequestId(sequenceService.generateDisplayMessagesSeq());
                    }
                    payload = toCleanMap(req6);
                    break;
                }
                case "SetChargingProfile": {
                    SetChargingProfile req7 = objectMapper.convertValue(payload, SetChargingProfile.class);
                    if (req7.getChargingProfile() != null && req7.getChargingProfile().getId() == 0) {
                        req7.getChargingProfile().setId(sequenceService.generateChargingProfileSeq());
                    }
                    payload = toCleanMap(req7);
                    break;
                }
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("[OCPP2X BYPASS] payload 정리 실패: action={} error={}", action, e.getMessage(), e);
            return ResponseEntity.ok(ApiResult.rejected("payload 형식 오류: " + e.getMessage()));
        }

        ApiResult result = daemonClient.send(cpCsId, action, payload, null);
        return ResponseEntity.ok(result);
    }

    /**
     * 등록된 충전기 목록 (DaemonAccess 기반).
     */
    @GetMapping("/sessions")
    public ResponseEntity<Map<String, Object>> getSessions() {
        List<DaemonAccess> list = daemonAccessService.retrieveAllDaemonAccess();
        List<String> cpCsIds = list.stream()
                .map(DaemonAccess::getCpCsId)
                .collect(Collectors.toList());

        log.debug("[OCPP2X BYPASS] sessions count={}", cpCsIds.size());
        return ResponseEntity.ok(Map.of("connected", cpCsIds));
    }

    /**
     * typed 객체 → JSON String → Map 으로 2단계 변환.
     * <p>1단계 writeValueAsString: NON_NULL + MixIn 적용되어 null 필드와 boolean primitive(false) 필드 제거.</p>
     * <p>2단계 readValue(Map): daemon 으로 보낼 일반 Map 페이로드로 환원.</p>
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> toCleanMap(Object typedReq) throws Exception {
        String json = objectMapper.writeValueAsString(typedReq);
        return objectMapper.readValue(json, Map.class);
    }
}
