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

        switch (action) {
            case "RequestStartTransaction":
                RequestStartTransaction req = (RequestStartTransaction) payload;
                if (req.getRemoteStartId() == 0) {
                    req.setRemoteStartId(sequenceService.generateRemoteStartSeq());
                }
                break;
            case "ReserveNow":
                ReserveNow req2 = (ReserveNow) payload;
                if (req2.getId() == 0) {
                    req2.setId(sequenceService.generateReservSeq());
                }
                break;
            case "GetLog":
                GetLog req3 = (GetLog) payload;
                if (req3.getRequestId() == 0) {
                    req3.setRequestId(sequenceService.generateLogSeq());
                }
                break;
            case "UpdateFirmware":
                UpdateFirmware req4 = (UpdateFirmware) payload;
                if (req4.getRequestId() == 0) {
                    req4.setRequestId(sequenceService.generateFirmwareSeq());
                }
                break;
            case "CustomerInformation":
                CustomerInformation req5 = (CustomerInformation) payload;
                if (req5.getRequestId() == 0) {
                    req5.setRequestId(sequenceService.generateCustomerInformationSeq());
                }
                break;
            case "GetDisplayMessages":
                GetDisplayMessages req6 = (GetDisplayMessages) payload;
                if (req6.getRequestId() == 0) {
                    req6.setRequestId(sequenceService.generateDisplayMessagesSeq());
                }
                break;
            case "SetChargingProfile":
                SetChargingProfile req7 = (SetChargingProfile) payload;
                if (req7.getChargingProfile().getId() == 0) {
                    req7.getChargingProfile().setId(sequenceService.generateChargingProfileSeq());
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

        log.debug("[OCPP2X BYPASS] sessions count={}", cpCsIds.size());
        return ResponseEntity.ok(Map.of("connected", cpCsIds));
    }
}
