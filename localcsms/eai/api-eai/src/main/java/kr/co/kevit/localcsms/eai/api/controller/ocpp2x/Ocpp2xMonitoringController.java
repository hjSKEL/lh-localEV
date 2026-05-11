package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import kr.co.kevit.localcsms.charger.entity.domain.CsVariable;
import kr.co.kevit.localcsms.charger.process.CsVariableService;
import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OCPP 2.0.1 Monitoring 그룹 엔드포인트.
 *
 * GET/POST /ocpp2x/getMonitoringBase?chargingStationIdentity=
 * GET/POST /ocpp2x/setMonitoringBase?chargingStationIdentity=&monitoringBase=
 * GET/POST /ocpp2x/setMonitoringLevel?chargingStationIdentity=&severity=
 * POST     /ocpp2x/setVariableMonitoring?chargingStationIdentity=  (body: setMonitoringData 배열)
 * GET/POST /ocpp2x/clearVariableMonitoring?chargingStationIdentity=&id=1&id=2
 * GET/POST /ocpp2x/customerInformation?chargingStationIdentity=&requestId=&report=&clear=&customerIdentifier=&idToken=&idTokenType=
 */
@RestController
@RequestMapping("/ocpp2x")
public class Ocpp2xMonitoringController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp2xMonitoringController.class);

    private final AtomicInteger requestIdSeq = new AtomicInteger(1);
    private final Daemon2xClient daemonClient;
    private final SequenceService sequenceService;
    private final CsVariableService csVariableService;

    public Ocpp2xMonitoringController(Daemon2xClient daemonClient,
                                      SequenceService sequenceService,
                                      CsVariableService csVariableService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.csVariableService = csVariableService;
    }

    @RequestMapping(value = "/getMonitoringBase", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> getMonitoringBase(
            @RequestParam String chargingStationIdentity) {
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetMonitoringBase", Map.of(), null));
    }

    /** monitoringBase: All | FactoryDefault | HardWiredOnly (required) */
    @RequestMapping(value = "/setMonitoringBase", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> setMonitoringBase(
            @RequestParam String chargingStationIdentity,
            @RequestParam String reportBase) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("monitoringBase", reportBase);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "SetMonitoringBase", payload, null));
    }

    /** severity: 0~9 (required) */
    @RequestMapping(value = "/setMonitoringLevel", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> setMonitoringLevel(
            @RequestParam String chargingStationIdentity,
            @RequestParam Integer severity) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("severity", severity);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "SetMonitoringLevel", payload, null));
    }

    /**
     * body: { "setMonitoringData": [ { "value": double, "type": MonitorEnumType, "severity": int,
     *                                   "component": ..., "variable": ... } ] }
     * 배열 구조이므로 body 유지
     */
    @PostMapping("/setVariableMonitoring")
    public ResponseEntity<ApiResult> setVariableMonitoring(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "SetVariableMonitoring", payload, null));
    }

    /**
     * id 목록을 MonitoringCtrlr/ItemsPerMessage 값 기준으로 분할하여 전송.
     * DB 에 해당 변수가 없으면 전체를 한 번에 전송.
     * 복수 응답의 clearMonitoringResult 배열을 합산하여 반환.
     */
    @RequestMapping(value = "/clearVariableMonitoring", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> clearVariableMonitoring(
            @RequestParam String chargingStationIdentity,
            @RequestParam List<Integer> id) {

        int chunkSize = resolveItemsPerMessage(chargingStationIdentity, "ClearVariableMonitoring");
        log.info("[ClearVariableMonitoring] cpCsId={} totalIds={} chunkSize={}", chargingStationIdentity, id.size(), chunkSize);

        if (id.size() <= chunkSize) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", id);
            return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ClearVariableMonitoring", payload, null));
        }

        // 분할 전송 후 결과 병합
        List<Object> mergedResults = new ArrayList<>();
        String lastStatus = "accepted";

        for (int i = 0; i < id.size(); i += chunkSize) {
            List<Integer> chunk = id.subList(i, Math.min(i + chunkSize, id.size()));
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", chunk);

            ApiResult result = daemonClient.send(chargingStationIdentity, "ClearVariableMonitoring", payload, null);
            log.info("[ClearVariableMonitoring] chunk [{},{}] status={}", i, i + chunk.size() - 1, result.getStatus());

            if (!"accepted".equals(result.getStatus())) {
                lastStatus = result.getStatus();
            }

            // clearMonitoringResult 배열 합산
            if (result.getData() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> dataMap = (Map<String, Object>) result.getData();
                Object chunkResult = dataMap.get("clearMonitoringResult");
                if (chunkResult instanceof List) {
                    mergedResults.addAll((List<?>) chunkResult);
                }
            }
        }

        Map<String, Object> mergedData = new HashMap<>();
        mergedData.put("clearMonitoringResult", mergedResults);

        return ResponseEntity.ok("accepted".equals(lastStatus)
                ? ApiResult.accepted(mergedData)
                : ApiResult.rejected("일부 청크 전송 실패"));
    }

    /**
     * TB_CHCF002 에서 MonitoringCtrlr/ItemsPerMessage 값을 조회.
     * 없거나 0 이하이면 Integer.MAX_VALUE (제한 없음) 반환.
     */
    private int resolveItemsPerMessage(String chargingStationIdentity, String varInst) {
        int idx = chargingStationIdentity.lastIndexOf('-');
        if (idx < 0) return Integer.MAX_VALUE;

        String cpId = chargingStationIdentity.substring(0, idx);
        String csId = chargingStationIdentity.substring(idx + 1);

        try {
            CsVariable v = csVariableService.findOne(
                    cpId, csId,
                    "MonitoringCtrlr",
                    "ItemsPerMessage",
                    varInst,
                    "Actual");

            if (v != null && v.getAttrVal() != null) {
                int limit = Integer.parseInt(v.getAttrVal().trim());
                if (limit > 0) return limit;
            }
        } catch (Exception e) {
            log.warn("[ClearVariableMonitoring] ItemsPerMessage 조회 실패: cpCsId={} error={}", chargingStationIdentity, e.getMessage());
        }
        return Integer.MAX_VALUE;
    }

    /**
     * body (optional): { "componentVariable": [...], "monitoringCriteria": [...] }
     * requestId 는 CSMS 가 자동 생성.
     */
    @PostMapping("/getMonitoringReport")
    public ResponseEntity<ApiResult> getMonitoringReport(
            @RequestParam String chargingStationIdentity,
            @RequestBody(required = false) Map<String, Object> body) {

        if (body == null) body = new HashMap<>();
        body.put("requestId", requestIdSeq.getAndIncrement());

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetMonitoringReport", body, null));
    }

    /**
     * body (optional): { "monitor": [...], "requestId": auto }
     * requestId 는 CSMS 가 자동 생성.
     */
    @PostMapping("/setMonitoringReport")
    public ResponseEntity<ApiResult> setMonitoringReport(
            @RequestParam String chargingStationIdentity,
            @RequestParam String reportBase) {

        Map<String, Object> body = new HashMap<>();
        body.put("requestId", sequenceService.generateMonitoringReportSeq());
        body.put("reportBase", reportBase);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "SetMonitoringReport", body, null));
    }

    /**
     * body: { "report": true, "clear": false, "customerIdentifier": "...",
     *         "idToken": { "idToken": "...", "type": "..." } }
     * requestId 는 CSMS 가 자동 생성.
     */
    @PostMapping("/customerInformation")
    public ResponseEntity<ApiResult> customerInformation(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> body) {

        body.put("requestId", sequenceService.generateCustomerInformationSeq());

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "CustomerInformation", body, null));
    }
}
