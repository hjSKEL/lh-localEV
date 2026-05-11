package kr.co.kevit.localcsms.eai.api.controller.ocpp16;

import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.type.LogType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Diagnostics 그룹 엔드포인트.
 *
 * POST /getDiagnostics
 * POST /getLog
 */
@RestController
@RequestMapping("/ocpp16")
public class DiagnosticsController {

    private final Daemon16Client daemonClient;
    private final SequenceService sequenceService;

    public DiagnosticsController(Daemon16Client daemonClient, SequenceService sequenceService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
    }

    /** Trigger a GetDiagnostics.req from the CSMS. */
    @PostMapping("/getDiagnostics")
    public ResponseEntity<ApiResult> getDiagnostics(
            @RequestParam String chargingStationIdentity,
            @RequestParam String location,
            @RequestParam(required = false) Integer retries,
            @RequestParam(required = false) Integer retryInterval,
            @RequestParam(required = false) OffsetDateTime startTime,
            @RequestParam(required = false) OffsetDateTime stopTime) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("location", location);
        if (retries != null) payload.put("retries", retries);
        if (retryInterval != null) payload.put("retryInterval", retryInterval);
        if (startTime != null) payload.put("startTime", startTime);
        if (stopTime != null) payload.put("stopTime", stopTime);
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetDiagnostics", payload, null));
    }

    /** Trigger a GetLog.req from the CSMS. */
    @PostMapping("/getLog")
    public ResponseEntity<ApiResult> getLog(
            @RequestParam String chargingStationIdentity,
            @RequestParam LogType logType,
            @RequestParam String remoteLocation,
            @RequestParam(required = false) String oldestTimestamp,
            @RequestParam(required = false) String latestTimestamp,
            @RequestParam(required = false) Integer retries,
            @RequestParam(required = false) Integer retryInterval) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("logType", logType.name());
        payload.put("remoteLocation", remoteLocation);
        payload.put("requestId", sequenceService.generateLogSeq());
        if (oldestTimestamp != null) payload.put("oldestTimestamp", oldestTimestamp);
        if (latestTimestamp != null) payload.put("latestTimestamp", latestTimestamp);
        if (retries != null) payload.put("retries", retries);
        if (retryInterval != null) payload.put("retryInterval", retryInterval);      
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetLog", payload, null));
    }
}
