package kr.co.kevit.localcsms.eai.api.controller.ocpp16;

import kr.co.kevit.localcsms.smartcharging.process.ChargingProfileService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kr.co.kevit.ocpp16.domain.ChargingProfile;
import kr.co.kevit.ocpp16.request.RemoteStartTransaction;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Transaction 그룹 엔드포인트.
 *
 * POST /requestStartTransaction
 * POST /requestStopTransaction
 */
@RestController
@RequestMapping("/ocpp16")
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final Daemon16Client daemonClient;
    private final SequenceService sequenceService;
    private final ChargingProfileService chargingProfileService;

    public TransactionController(Daemon16Client daemonClient,
            SequenceService sequenceService,
            ChargingProfileService chargingProfileService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.chargingProfileService = chargingProfileService;
    }

    /** Trigger a RemoteStartTransaction.req from the CSMS. */
    @PostMapping("/requestStartTransaction")
    public ResponseEntity<ApiResult> requestStartTransaction(
            @RequestParam String chargingStationIdentity,
            @RequestBody(required = false) RemoteStartTransaction payload) {

        if (payload.getChargingProfile() != null) {
            // cpId / csId 파싱
            String cpId = null, csId = null;
            int idx = chargingStationIdentity.lastIndexOf('-');
            if (idx >= 0) {
                cpId = chargingStationIdentity.substring(0, idx);
                csId = chargingStationIdentity.substring(idx + 1);
            }

            ChargingProfile ocppProfile = payload.getChargingProfile();
            if (ocppProfile.getChargingProfileId() == null) {
                ocppProfile.setChargingProfileId(sequenceService.generateChargingProfileSeq());
            }

            kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile profile = new kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile();
            try {
                profile.setWriter(new Writer(StringConstants.SYSTEM_EMPLOYEE));
                chargingProfileService.saveProfile(profile);
                log.info("[RequestStartTransaction] chargingProfile DB 저장: cpId={} csId={} profileId={}", cpId, csId,
                        ocppProfile.getChargingProfileId());
            } catch (Exception e) {
                log.error("[RequestStartTransaction] chargingProfile DB 저장 실패: cpId={} csId={} error={}", cpId, csId,
                        e.getMessage());
            }
        }
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "RemoteStartTransaction", payload, null));
    }

    /** Trigger a RemoteStopTransaction.req from the CSMS. */
    @PostMapping("/requestStopTransaction")
    public ResponseEntity<ApiResult> requestStopTransaction(
            @RequestParam String chargingStationIdentity,
            @RequestParam int transactionId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("transactionId", transactionId);
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "RemoteStopTransaction", payload, null));
    }
}
