package kr.co.kevit.localcsms.eai.api.controller.ocpp16;

import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reservation 그룹 엔드포인트.
 *
 * POST /reserveNow
 * POST /cancelReservation
 */
@RestController
@RequestMapping("/ocpp16")
public class ReservationController {

    private final Daemon16Client daemonClient;
    private final SequenceService sequenceService;

    public ReservationController(Daemon16Client daemonClient, SequenceService sequenceService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
    }

    /** Trigger a ReserveNow.req from the CSMS. */
    @PostMapping("/reserveNow")
    public ResponseEntity<ApiResult> reserveNow(
            @RequestParam String chargingStationIdentity,
            @RequestParam String expiryDate,
            @RequestParam String idTag,
            @RequestParam(required = false) Integer connectorId,
            @RequestParam(required = false) String parentIdTag) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("connectorId", connectorId);
        payload.put("expiryDate", expiryDate);
        payload.put("idTag", idTag);
        payload.put("reservationId", sequenceService.generateReservSeq());
        if (parentIdTag != null) {
            payload.put("parentIdTag", parentIdTag);
        }
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ReserveNow", payload, null));
    }

    /** Trigger a CancelReservation.req from the CSMS. */
    @PostMapping("/cancelReservation")
    public ResponseEntity<ApiResult> cancelReservation(
            @RequestParam String chargingStationIdentity,
            @RequestParam int reservationId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("reservationId", reservationId);
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "CancelReservation", payload, null));
    }
}
