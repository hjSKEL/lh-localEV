package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import kr.co.kevit.localcsms.smartcharging.entity.domain.CsConfig;
import kr.co.kevit.localcsms.smartcharging.process.ChargingProfileService;
import kr.co.kevit.localcsms.smartcharging.process.SmartChargingService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
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
    private final ChargingProfileService chargingProfileService;
    private final SmartChargingService smartChargingService;

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
            DaemonAccessService daemonAccessService,
            ChargingProfileService chargingProfileService,
            SmartChargingService smartChargingService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.daemonAccessService = daemonAccessService;
        this.chargingProfileService = chargingProfileService;
        this.smartChargingService = smartChargingService;
    }

    /**
     * SetVariables 송신 시 SmartChargingCtrlr.MaxExternalConstraintsId 캡처 → CsConfig 저장.
     * 이후 ExternalConstraints 프로파일 등록 시 SmartChargingService 가 id 범위 검증에 사용.
     */
    @SuppressWarnings("unchecked")
    private void captureMaxExternalConstraintsId(String cpCsId, Object payload) {
        if (!(payload instanceof Map)) return;
        Object data = ((Map<String, Object>) payload).get("setVariableData");
        if (!(data instanceof List)) return;
        Integer captured = null;
        for (Object item : (List<Object>) data) {
            if (!(item instanceof Map)) continue;
            Map<String, Object> entry = (Map<String, Object>) item;
            Map<String, Object> component = (Map<String, Object>) entry.get("component");
            Map<String, Object> variable = (Map<String, Object>) entry.get("variable");
            if (component == null || variable == null) continue;
            if (!"SmartChargingCtrlr".equals(component.get("name"))) continue;
            if (!"MaxExternalConstraintsId".equals(variable.get("name"))) continue;
            try {
                captured = Integer.parseInt(String.valueOf(entry.get("attributeValue")));
            } catch (NumberFormatException ignore) { /* 무시 */ }
        }
        if (captured == null) return;
        int dash = cpCsId.lastIndexOf(StringConstants.DASH);
        if (dash < 0) return;
        try {
            CsConfig cfg = new CsConfig();
            cfg.setCpId(cpCsId.substring(0, dash));
            cfg.setCsId(cpCsId.substring(dash + 1));
            cfg.setMaxExtConstraintsId(captured);
            cfg.setWriter(new Writer(StringConstants.SYSTEM_EMPLOYEE));
            chargingProfileService.saveCsConfig(cfg);
            log.info("[SetVariables] MaxExternalConstraintsId cpCsId={} value={}", cpCsId, captured);
        } catch (Exception e) {
            log.warn("[SetVariables] CsConfig 저장 실패 cpCsId={}: {}", cpCsId, e.getMessage());
        }
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
                    // K28 PullDynamicScheduleUpdate 가 송신 프로파일을 조회할 수 있도록 DB upsert
                    persistSentProfile(cpCsId, req7);
                    payload = toCleanMap(req7);
                    break;
                }
                case "SetVariables": {
                    captureMaxExternalConstraintsId(cpCsId, payload);
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

    /**
     * SetChargingProfile 송신 시 프로파일을 CSMS 저장소에 upsert.
     * cpCsId 는 "cpId-csId" 형식, DASH 위치는 cpId 에 하이픈이 있을 수 있으므로 lastIndexOf 로 분리.
     */
    private void persistSentProfile(String cpCsId, SetChargingProfile req) {
        if (req == null || req.getChargingProfile() == null) return;
        int dash = cpCsId.lastIndexOf(StringConstants.DASH);
        if (dash < 0) return;
        String cpId = cpCsId.substring(0, dash);
        String csId = cpCsId.substring(dash + 1);
        int evseId = req.getEvseId();
        try {
            smartChargingService.persistSentProfile(cpId, csId, evseId, req.getChargingProfile());
        } catch (Exception e) {
            log.warn("[OCPP2X BYPASS] SetChargingProfile 영속 실패 cpCsId={} profileId={}: {}",
                    cpCsId, req.getChargingProfile().getId(), e.getMessage());
        }
    }
}
