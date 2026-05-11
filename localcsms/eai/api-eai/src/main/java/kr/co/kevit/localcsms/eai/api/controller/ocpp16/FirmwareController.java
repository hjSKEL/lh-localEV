package kr.co.kevit.localcsms.eai.api.controller.ocpp16;

import kr.co.kevit.localcsms.charger.entity.domain.CsFirmware;
import kr.co.kevit.localcsms.charger.process.CsFirmwareService;
import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Firmware Management 그룹 엔드포인트.
 *
 * POST /updateFirmware
 * POST /signedUpdateFirmware
 */
@RestController
@RequestMapping("/ocpp16")
public class FirmwareController {

    private static final Logger log = LoggerFactory.getLogger(FirmwareController.class);

    private final Daemon16Client daemonClient;
    private final SequenceService sequenceService;
    private final CsFirmwareService csFirmwareService;

    public FirmwareController(Daemon16Client daemonClient, SequenceService sequenceService,
                              CsFirmwareService csFirmwareService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.csFirmwareService = csFirmwareService;
    }

    /** Trigger a UpdateFirmware.req from the CSMS. */
    @PostMapping("/updateFirmware")
    public ResponseEntity<ApiResult> updateFirmware(
            @RequestParam String chargingStationIdentity,
            @RequestParam String location) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("location", location);
        payload.put("retrieveDate", DateUtils.dateToString(new Date(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT));

        ApiResult result = daemonClient.send(chargingStationIdentity, "UpdateFirmware", payload, null);
        saveFirmwareRequest(chargingStationIdentity, location);
        return ResponseEntity.ok(result);
    }

    /** Trigger a SignedUpdateFirmware.req from the CSMS. */
    @PostMapping("/signedUpdateFirmware")
    public ResponseEntity<ApiResult> signedUpdateFirmware(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> payload) {
        payload.put("requestId", sequenceService.generateFirmwareSeq());

        ApiResult result = daemonClient.send(chargingStationIdentity, "SignedUpdateFirmware", payload, null);

        // SignedUpdateFirmware의 firmware.location 추출
        String location = null;
        Object firmware = payload.get("firmware");
        if (firmware instanceof Map) {
            Object loc = ((Map<?, ?>) firmware).get("location");
            if (loc != null) location = loc.toString();
        }
        saveFirmwareRequest(chargingStationIdentity, location);
        return ResponseEntity.ok(result);
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
            Date now = new Date();
            fw.setRequestDate(now);
            fw.setUpdateDate(now);
            csFirmwareService.saveFirmware(fw);
        } catch (Exception e) {
            log.warn("CsFirmware 저장 실패: {}", e.getMessage(), e);
        }
    }
}
