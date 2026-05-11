package kr.co.kevit.localcsms.eai.api.controller.ocpp16;

import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.system.entity.domain.DaemonAccess;
import kr.co.kevit.localcsms.system.process.DaemonAccessService;
import kr.co.kevit.ocpp16.request.GetLog;
import kr.co.kevit.ocpp16.request.ReserveNow;
import kr.co.kevit.ocpp16.request.SignedUpdateFirmware;
import kr.co.kevit.ocpp16.request.SetChargingProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * OCPP 1.6 Bypass Controller.
 * admin-web 에서 호출하여 ocpp16-daemon 으로 그대로 전달한다.
 *
 * POST /ocpp16/bypass/{cpCsId} { "action": "Reset", "payload": { "type": "Soft"
 * } }
 * GET /ocpp16/bypass/sessions
 */
@RestController
@RequestMapping("/ocpp16/bypass")
public class Ocpp16BypassController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp16BypassController.class);

    private final Daemon16Client daemonClient;
    private final DaemonAccessService daemonAccessService;
    private final SequenceService sequenceService;

    public Ocpp16BypassController(Daemon16Client daemonClient,
            SequenceService sequenceService,
            DaemonAccessService daemonAccessService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.daemonAccessService = daemonAccessService;
    }

    /**
     * 명령 bypass — ocpp16-daemon 으로 그대로 전달.
     */
    @PostMapping("/{cpCsId}")
    public ResponseEntity<ApiResult> bypassCommand(
            @PathVariable String cpCsId,
            @RequestBody Map<String, Object> request) {

        String action = (String) request.get("action");
        Object payload = request.get("payload");

        log.info("[OCPP16 BYPASS] cpCsId={} action={}", cpCsId, action);
        switch (action) {
            case "ReserveNow":
                ReserveNow req2 = (ReserveNow) payload;
                if (req2.getReservationId() == null || req2.getReservationId() == 0) {
                    req2.setReservationId(sequenceService.generateReservSeq());
                }
                break;
            case "GetLog":
                GetLog req3 = (GetLog) payload;
                if (req3.getRequestId() == null || req3.getRequestId() == 0) {
                    req3.setRequestId(sequenceService.generateLogSeq());
                }
                break;
            case "SignedUpdateFirmware":
                SignedUpdateFirmware req4 = (SignedUpdateFirmware) payload;
                if (req4.getRequestId() == null || req4.getRequestId() == 0) {
                    req4.setRequestId(sequenceService.generateFirmwareSeq());
                }
                break;
            case "SetChargingProfile":
                SetChargingProfile req7 = (SetChargingProfile) payload;
                if (req7.getCsChargingProfiles().getChargingProfileId() == 0) {
                    req7.getCsChargingProfiles().setChargingProfileId(sequenceService.generateChargingProfileSeq());
                }
                break;
            default:
                break;
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

        log.debug("[OCPP16 BYPASS] sessions count={}", cpCsIds.size());
        return ResponseEntity.ok(Map.of("connected", cpCsIds));
    }
}
