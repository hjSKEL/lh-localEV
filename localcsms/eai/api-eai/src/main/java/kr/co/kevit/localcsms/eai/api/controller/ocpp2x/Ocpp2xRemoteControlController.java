package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * OCPP 2.0.1 Remote Control 그룹 엔드포인트.
 *
 * GET/POST /ocpp2x/reset?chargingStationIdentity=&type=&evseId=
 * GET/POST /ocpp2x/changeAvailability?chargingStationIdentity=&operationalStatus=&evseId=&connectorId=
 * GET/POST /ocpp2x/unlockConnector?chargingStationIdentity=&evseId=&connectorId=
 * GET/POST /ocpp2x/triggerMessage?chargingStationIdentity=&requestedMessage=&evseId=&connectorId=
 * GET/POST /ocpp2x/clearCache?chargingStationIdentity=
 */
@RestController
@RequestMapping("/ocpp2x")
public class Ocpp2xRemoteControlController {

    private final Daemon2xClient daemonClient;

    public Ocpp2xRemoteControlController(Daemon2xClient daemonClient) {
        this.daemonClient = daemonClient;
    }

    /** type: Immediate | OnIdle (required), evseId (optional) */
    @RequestMapping(value = "/reset", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> reset(
            @RequestParam String chargingStationIdentity,
            @RequestParam String type,
            @RequestParam(required = false) Integer evseId) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("type", type);
        if (evseId != null) payload.put("evseId", evseId);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "Reset", payload, null));
    }

    /** operationalStatus: Operative | Inoperative (required), evseId (optional), connectorId (optional) */
    @RequestMapping(value = "/changeAvailability", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> changeAvailability(
            @RequestParam String chargingStationIdentity,
            @RequestParam String operationalStatus,
            @RequestParam(required = false) Integer evseId,
            @RequestParam(required = false) Integer connectorId) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("operationalStatus", operationalStatus);
        if (evseId != null) {
            Map<String, Object> evse = new HashMap<>();
            evse.put("id", evseId);
            if (connectorId != null) evse.put("connectorId", connectorId);
            payload.put("evse", evse);
        }

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ChangeAvailability", payload, null));
    }

    /** evseId (required), connectorId (required) */
    @RequestMapping(value = "/unlockConnector", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> unlockConnector(
            @RequestParam String chargingStationIdentity,
            @RequestParam Integer evseId,
            @RequestParam Integer connectorId) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("evseId", evseId);
        payload.put("connectorId", connectorId);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "UnlockConnector", payload, null));
    }

    /** requestMessage: MessageTriggerEnumType (required), evseId (optional), connectorId (optional) */
    @RequestMapping(value = "/triggerMessage", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> triggerMessage(
            @RequestParam String chargingStationIdentity,
            @RequestParam String requestMessage,
            @RequestParam(required = false) Integer evseId,
            @RequestParam(required = false) Integer connectorId) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("requestedMessage", requestMessage);
        if (evseId != null) {
            Map<String, Object> evse = new HashMap<>();
            evse.put("id", evseId);
            if (connectorId != null) evse.put("connectorId", connectorId);
            payload.put("evse", evse);
        }

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "TriggerMessage", payload, null));
    }

    @RequestMapping(value = "/clearCache", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> clearCache(
            @RequestParam String chargingStationIdentity) {
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ClearCache", Map.of(), null));
    }
}
