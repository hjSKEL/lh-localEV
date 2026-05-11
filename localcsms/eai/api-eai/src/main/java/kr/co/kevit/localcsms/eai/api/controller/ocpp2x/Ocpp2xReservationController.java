package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * OCPP 2.0.1 Reservation 그룹 엔드포인트.
 *
 * GET/POST /ocpp2x/reserveNow?chargingStationIdentity=&id=&expiryDateTime=&idToken=&idTokenType=&connectorType=&evseId=
 * GET/POST /ocpp2x/cancelReservation?chargingStationIdentity=&reservationId=
 */
@RestController
@RequestMapping("/ocpp2x")
public class Ocpp2xReservationController {

    private final Daemon2xClient daemonClient;
    private final SequenceService sequenceService;

    public Ocpp2xReservationController(Daemon2xClient daemonClient, SequenceService sequenceService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
    }

    /**
     * id (required), expiryDateTime (required), idToken (required), idTokenType (required)
     * connectorType (optional), evseId (optional)
     * idToken/idTokenType → { "idToken": ..., "type": ... } 객체로 조립
     */
    @RequestMapping(value = "/reserveNow", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> reserveNow(
            @RequestParam String chargingStationIdentity,
            @RequestParam String expiryDateTime,
            @RequestParam String idToken,
            @RequestParam String idTokenType,
            @RequestParam(required = false) String connectorType,
            @RequestParam(required = false) Integer evseId,
            @RequestParam(required = false) String groupIdToken) {

        Map<String, Object> idTokenMap = new HashMap<>();
        idTokenMap.put("idToken", idToken);
        idTokenMap.put("type", idTokenType);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", sequenceService.generateReservSeq());
        payload.put("expiryDateTime", expiryDateTime);
        payload.put("idToken", idTokenMap);
        if (groupIdToken != null) {
            Map<String, Object> groupIdTokenMap = new HashMap<>();
            groupIdTokenMap.put("idToken", groupIdToken);
            groupIdTokenMap.put("type", idTokenType);
            payload.put("groupIdToken", groupIdTokenMap);
        }
        if (connectorType != null) payload.put("connectorType", connectorType);
        if (evseId != null) payload.put("evseId", evseId);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ReserveNow", payload, null));
    }

    /** reservationId (required) */
    @RequestMapping(value = "/cancelReservation", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> cancelReservation(
            @RequestParam String chargingStationIdentity,
            @RequestParam Integer reservationId) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("reservationId", reservationId);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "CancelReservation", payload, null));
    }
}
