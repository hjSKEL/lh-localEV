package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import kr.co.kevit.localcsms.charger.entity.domain.CsFirmware;
import kr.co.kevit.localcsms.charger.process.CsFirmwareService;
import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * OCPP 2.0.1 Firmware / Log / Network 그룹 엔드포인트.
 *
 * GET/POST /ocpp2x/updateFirmware?chargingStationIdentity=&requestId=&location=&retrieveDateTime=&...
 * GET/POST /ocpp2x/getLog?chargingStationIdentity=&logType=&requestId=&remoteLocation=&...
 * GET/POST /ocpp2x/setNetworkProfile?chargingStationIdentity=&configurationSlot=&ocppVersion=&...
 */
@RestController
@RequestMapping("/ocpp2x")
public class Ocpp2xFirmwareController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp2xFirmwareController.class);

    private final Daemon2xClient daemonClient;
    private final SequenceService sequenceService;
    private final CsFirmwareService csFirmwareService;

    public Ocpp2xFirmwareController(Daemon2xClient daemonClient, SequenceService sequenceService,
                                    CsFirmwareService csFirmwareService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.csFirmwareService = csFirmwareService;
    }

    /**
     * body: { "retries": ..., "retryInterval": ..., "firmware": { "location": ..., "retrieveDateTime": ..., ... } }
     * requestId 는 CSMS 가 자동 생성.
     */
    @PostMapping("/updateFirmware")
    public ResponseEntity<ApiResult> updateFirmware(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> body) {

        body.put("requestId", sequenceService.generateFirmwareSeq());

        ApiResult result = daemonClient.send(chargingStationIdentity, "UpdateFirmware", body, null);

        // firmware.location 추출 후 CsFirmware 저장
        String location = null;
        Object firmware = body.get("firmware");
        if (firmware instanceof Map) {
            Object loc = ((Map<?, ?>) firmware).get("location");
            if (loc != null) location = loc.toString();
        }
        saveFirmwareRequest(chargingStationIdentity, location);

        return ResponseEntity.ok(result);
    }

    /**
     * logType (required): DiagnosticsLog | SecurityLog
     * remoteLocation (required): 로그 업로드 URL
     * oldestTimestamp, latestTimestamp (optional)
     * retries, retryInterval (optional)
     * requestId 는 CSMS 가 자동 생성.
     */
    @RequestMapping(value = "/getLog", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> getLog(
            @RequestParam String chargingStationIdentity,
            @RequestParam String logType,
            @RequestParam String remoteLocation,
            @RequestParam(required = false) String oldestTimestamp,
            @RequestParam(required = false) String latestTimestamp,
            @RequestParam(required = false) Integer retries,
            @RequestParam(required = false) Integer retryInterval) {

        Map<String, Object> log = new HashMap<>();
        log.put("remoteLocation", remoteLocation);
        if (oldestTimestamp != null) log.put("oldestTimestamp", oldestTimestamp);
        if (latestTimestamp != null) log.put("latestTimestamp", latestTimestamp);

        Map<String, Object> payload = new HashMap<>();
        payload.put("requestId", sequenceService.generateLogSeq());
        payload.put("logType", logType);
        payload.put("log", log);
        if (retries != null) payload.put("retries", retries);
        if (retryInterval != null) payload.put("retryInterval", retryInterval);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetLog", payload, null));
    }

    /**
     * configurationSlot (required), ocppCsmsUrl (required), messageTimeout (required),
     * securityProfile (required), ocppInterface (optional)
     * → OCPP 2.0.1 connectionData 객체로 조립.
     */
    @RequestMapping(value = "/setNetworkProfile", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> setNetworkProfile(
            @RequestParam String chargingStationIdentity,
            @RequestParam String configurationSlot,
            @RequestParam String ocppCsmsUrl,
            @RequestParam String messageTimeout,
            @RequestParam String securityProfile,
            @RequestParam(required = false) String ocppInterface) {

        Map<String, Object> connectionData = new HashMap<>();
        connectionData.put("ocppCsmsUrl", ocppCsmsUrl);
        connectionData.put("ocppVersion", "ocpp2.0.1");
        connectionData.put("messageTimeout", Integer.parseInt(messageTimeout));
        connectionData.put("securityProfile", Integer.parseInt(securityProfile));
        if (ocppInterface != null) connectionData.put("ocppInterface", ocppInterface);

        Map<String, Object> payload = new HashMap<>();
        payload.put("configurationSlot", Integer.parseInt(configurationSlot));
        payload.put("connectionData", connectionData);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "SetNetworkProfile", payload, null));
    }

    private void saveFirmwareRequest(String chargingStationIdentity, String location) {
        try {
            int idx = chargingStationIdentity.lastIndexOf('-');
            if (idx < 0) return;
            String cpId = chargingStationIdentity.substring(0, idx);
            String csId = chargingStationIdentity.substring(idx + 1);

            CsFirmware fw = new CsFirmware();
            fw.setCpId(cpId);
            fw.setCsId(csId);
            fw.setUrl(location);
            fw.setStatus("CSFW01"); // 요청
            fw.setRequestEmployeeId(kr.co.kevit.localcsms.common.util.string.StringConstants.SYSTEM_EMPLOYEE);
            java.util.Date now = new java.util.Date();
            fw.setRequestDate(now);
            fw.setUpdateDate(now);
            csFirmwareService.saveFirmware(fw);
        } catch (Exception e) {
            log.warn("CsFirmware 저장 실패: {}", e.getMessage(), e);
        }
    }
}
