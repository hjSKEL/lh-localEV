package kr.co.kevit.localcsms.eai.api.controller.ocpp16;

import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.type.AvailabilityType;
import kr.co.kevit.localcsms.eai.api.dto.type.ExtendedMessageTriggerType;
import kr.co.kevit.localcsms.eai.api.dto.type.MessageTriggerType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Remote Control 그룹 엔드포인트.
 *
 * POST /unlockConnector
 * POST /triggerMessage
 * POST /extendedTriggerMessage
 * POST /changeAvailability
 */
@RestController
@RequestMapping("/ocpp16")
public class RemoteControlController {

    private final Daemon16Client daemonClient;

    public RemoteControlController(Daemon16Client daemonClient) {
        this.daemonClient = daemonClient;
    }

    /** Trigger a UnlockConnector.req from the CSMS. */
    @PostMapping("/unlockConnector")
    public ResponseEntity<ApiResult> unlockConnector(
            @RequestParam String chargingStationIdentity,
            @RequestParam int connectorId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("connectorId", connectorId);
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "UnlockConnector", payload, null));
    }

    /**
     * Trigger a TriggerMessage.req from the CSMS.
     * requestMessage: BootNotification | DiagnosticsStatusNotification |
     *                 FirmwareStatusNotification | Heartbeat | MeterValues | StatusNotification
     */
    @PostMapping("/triggerMessage")
    public ResponseEntity<ApiResult> triggerMessage(
            @RequestParam String chargingStationIdentity,
            @RequestParam MessageTriggerType requestMessage,
            @RequestParam(required = false) Integer connectorId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("requestedMessage", requestMessage.name());
        if (connectorId != null) {
            payload.put("connectorId", connectorId);
        }
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "TriggerMessage", payload, null));
    }

    /** Trigger an ExtendedTriggerMessage.req from the CSMS. */
    @PostMapping("/extendedTriggerMessage")
    public ResponseEntity<ApiResult> extendedTriggerMessage(
            @RequestParam String chargingStationIdentity,
            @RequestParam ExtendedMessageTriggerType requestMessage,
            @RequestParam(required = false) Integer connectorId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("requestedMessage", requestMessage.name());
        if (connectorId != null) {
            payload.put("connectorId", connectorId);
        }
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ExtendedTriggerMessage", payload, null));
    }

    /** Trigger a ChangeAvailability.req from the CSMS. */
    @PostMapping("/changeAvailability")
    public ResponseEntity<ApiResult> changeAvailability(
            @RequestParam String chargingStationIdentity,
            @RequestParam int connectorId,
            @RequestParam AvailabilityType type) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("connectorId", connectorId);
        payload.put("type", type.name());
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ChangeAvailability", payload, null));
    }
}
