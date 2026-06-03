package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.domain.CodeVal;
import kr.co.kevit.localcsms.common.process.CodeValService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.smartcharging.process.NegotiationCoordinator;
import kr.co.kevit.localcsms.smartcharging.process.NegotiationResult;
import kr.co.kevit.localcsms.smartcharging.process.SmartChargingService;
import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds;
import kr.co.kevit.ocpp201.request.SetChargingProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * OCPP 2.x Inbound Callback Controller.
 *
 * <p>ocpp20-daemon 이 CS 로부터 받은 메시지 중 자동 후속 push 결정이 필요한 액션을
 * 본 콜백으로 forward 한다. daemon 은 OCPP transport 만 담당하고, 결정(어떤 profile,
 * 어떤 setpoint)·외부 EMS 연동·DB 영속화는 api-eai 에서 수행한다.</p>
 *
 * <p>흐름:
 * <pre>
 *   CS ─ NotifyEVChargingNeeds(CALL) → daemon
 *     daemon: 즉시 NotifyEVChargingNeedsResponse(Processing) 응답
 *     daemon → POST /ocpp2x/inbound/{cpCsId}/NotifyEVChargingNeeds (이 컨트롤러)
 *
 *   api-eai: NegotiationCoordinator.handleNeeds() 호출 → profile 결정
 *   api-eai → Daemon2xClient.send(cpCsId, "SetChargingProfile", payload)
 *
 *   daemon → CS ─ SetChargingProfile(CALL)
 * </pre>
 */
@RestController
@RequestMapping("/ocpp2x/inbound")
public class Ocpp2xInboundController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp2xInboundController.class);

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final NegotiationCoordinator negotiationCoordinator;
    private final SmartChargingService smartChargingService;
    private final ChargerStatusService chargerStatusService;
    private final CodeValService codeValService;
    private final Daemon2xClient daemonClient;

    public Ocpp2xInboundController(NegotiationCoordinator negotiationCoordinator,
                                   SmartChargingService smartChargingService,
                                   ChargerStatusService chargerStatusService,
                                   CodeValService codeValService,
                                   Daemon2xClient daemonClient) {
        this.negotiationCoordinator = negotiationCoordinator;
        this.smartChargingService = smartChargingService;
        this.chargerStatusService = chargerStatusService;
        this.codeValService = codeValService;
        this.daemonClient = daemonClient;
    }

    /**
     * daemon 에서 전달된 inbound 메시지의 후속 결정 처리.
     *
     * @param cpCsId  cpId-csId 결합 ID
     * @param action  OCPP action 이름 (예: NotifyEVChargingNeeds)
     * @param body    { "payload": { ... } } 형태의 메시지 본문
     */
    @PostMapping("/{cpCsId}/{action}")
    public ResponseEntity<ApiResult> handleInbound(@PathVariable String cpCsId,
                                                   @PathVariable String action,
                                                   @RequestBody Map<String, Object> body) {
        log.info("[OCPP2X INBOUND] cpCsId={} action={}", cpCsId, action);
        try {
            switch (action) {
                case "NotifyEVChargingNeeds":
                    return handleNotifyEVChargingNeeds(cpCsId, body);
                default:
                    log.warn("[OCPP2X INBOUND] 처리하지 않는 action: {}", action);
                    return ResponseEntity.ok(ApiResult.rejected("Unhandled action: " + action));
            }
        } catch (Exception e) {
            log.error("[OCPP2X INBOUND] 처리 실패 cpCsId={} action={}", cpCsId, action, e);
            return ResponseEntity.ok(ApiResult.rejected("Inbound 처리 오류: " + e.getMessage()));
        }
    }

    /**
     * NotifyEVChargingNeeds → NegotiationCoordinator 결정 → Daemon2xClient 로 SetChargingProfile push.
     */
    private ResponseEntity<ApiResult> handleNotifyEVChargingNeeds(String cpCsId, Map<String, Object> body) throws Exception {
        Object payloadObj = body.get("payload");
        if (payloadObj == null) {
            return ResponseEntity.ok(ApiResult.rejected("payload 누락"));
        }
        NotifyEVChargingNeeds req = objectMapper.convertValue(payloadObj, NotifyEVChargingNeeds.class);

        int dash = cpCsId.lastIndexOf(StringConstants.DASH);
        if (dash < 0) {
            return ResponseEntity.ok(ApiResult.rejected("cpCsId 형식 오류: " + cpCsId));
        }
        String cpId = cpCsId.substring(0, dash);
        String csId = cpCsId.substring(dash + 1);
        int evseId = req.getEvseId() != null ? req.getEvseId() : 0;

        // 진행 중 충전 컨텍스트 조회
        List<ChargerStatusInfo> infos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(cpId, csId);
        ChargerStatusInfo info = infos == null ? null : infos.stream()
                .filter(s -> s.getEvseId() == evseId).findFirst().orElse(null);
        boolean hasActiveCharging = info != null && !StringUtils.isEmpty(info.getRechargingId());
        String rechargingId = info != null ? info.getRechargingId() : null;

        boolean noProfilePolicy = isNoProfilePolicy();

        NegotiationResult result = negotiationCoordinator.handleNeeds(
                cpId, csId, rechargingId, req, hasActiveCharging, noProfilePolicy);

        if (!result.hasProfileToPush()) {
            log.info("[OCPP2X INBOUND] NotifyEVChargingNeeds: push 없음 cpCsId={} status={}", cpCsId, result.getStatus());
            return ResponseEntity.ok(ApiResult.accepted("결정 status=" + result.getStatus()));
        }

        ChargingProfileType profile = result.getProfileToPush();
        SetChargingProfile scp = new SetChargingProfile();
        scp.setEvseId(evseId);
        scp.setChargingProfile(profile);

        // 송신 프로파일 영속 (TB_CHPF001/002/003 upsert)
        try {
            smartChargingService.persistSentProfile(cpId, csId, evseId, profile);
        } catch (Exception e) {
            log.warn("[OCPP2X INBOUND] persistSentProfile 실패 cpCsId={} profileId={}: {}",
                    cpCsId, profile.getId(), e.getMessage());
        }

        Map<String, Object> scpPayload = toCleanMap(scp);
        log.info("[OCPP2X INBOUND] SetChargingProfile push cpCsId={} profileId={}", cpCsId, profile.getId());
        ApiResult sendResult = daemonClient.send(cpCsId, "SetChargingProfile", scpPayload, null);
        return ResponseEntity.ok(sendResult);
    }

    /** NotifyEVChargingNeedsBean 과 동일 — DynamicControl + NoProfile 정책 (CodeVal OCPP03). */
    private boolean isNoProfilePolicy() {
        if (codeValService == null) {
            return false;
        }
        CodeVal codeVal = codeValService.retrieveCodeValByCode("OCPP03");
        return codeVal != null && "true".equals(codeVal.getCodeValue());
    }

    /** typed 객체 → JSON String → Map. null 필드 omit + boolean primitive(false) 제거. */
    @SuppressWarnings("unchecked")
    private Map<String, Object> toCleanMap(Object typedReq) throws Exception {
        String json = objectMapper.writeValueAsString(typedReq);
        return objectMapper.readValue(json, Map.class);
    }
}
