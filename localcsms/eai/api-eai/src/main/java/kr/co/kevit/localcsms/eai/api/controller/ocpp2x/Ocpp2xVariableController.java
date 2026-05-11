package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.entity.domain.CsVariable;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.charger.process.CsVariableService;
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
 * OCPP 2.0.1 Variable 및 Report 그룹 엔드포인트.
 *
 * POST     /ocpp2x/getVariables?chargingStationIdentity=  (body: getVariableData 배열)
 * POST     /ocpp2x/setVariables?chargingStationIdentity=  (body: setVariableData 배열)
 * GET/POST /ocpp2x/getBaseReport?chargingStationIdentity=&requestId=&reportBase=
 * GET/POST /ocpp2x/getReport?chargingStationIdentity=&requestId=&componentCriteria=
 */
@RestController
@RequestMapping("/ocpp2x")
public class Ocpp2xVariableController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp2xVariableController.class);

    private final AtomicInteger requestIdSeq = new AtomicInteger(1);
    private final Daemon2xClient daemonClient;
    private final ChargingStationService chargingStationService;
    private final CsVariableService csVariableService;

    public Ocpp2xVariableController(Daemon2xClient daemonClient,
                                    ChargingStationService chargingStationService,
                                    CsVariableService csVariableService) {
        this.daemonClient = daemonClient;
        this.chargingStationService = chargingStationService;
        this.csVariableService = csVariableService;
    }

    /**
     * body: { "getVariableData": [ { "component": ..., "variable": ... } ] }
     * DeviceDataCtrlr/ItemsPerMessage(instance=GetVariables) 값 기준으로 분할 전송.
     * 응답의 getVariableResult 를 TB_CHCF002 에 저장.
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getVariables")
    public ResponseEntity<ApiResult> getVariables(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> payload) {

        List<Object> dataList = (List<Object>) payload.get("getVariableData");
        if (dataList == null || dataList.isEmpty()) {
            return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetVariables", payload, null));
        }

        int chunkSize = resolveGetVariablesItemsPerMessage(chargingStationIdentity);
        log.info("[GetVariables] cpCsId={} totalItems={} chunkSize={}", chargingStationIdentity, dataList.size(), chunkSize);

        if (dataList.size() <= chunkSize) {
            ApiResult result = daemonClient.send(chargingStationIdentity, "GetVariables", payload, null);
            if ("accepted".equals(result.getStatus())) {
                saveVariableResult(chargingStationIdentity, result.getData());
            }
            return ResponseEntity.ok(result);
        }

        // 분할 전송 후 결과 병합
        List<Object> mergedResults = new ArrayList<>();
        String lastStatus = "accepted";

        for (int i = 0; i < dataList.size(); i += chunkSize) {
            List<Object> chunk = dataList.subList(i, Math.min(i + chunkSize, dataList.size()));
            Map<String, Object> chunkPayload = new HashMap<>();
            chunkPayload.put("getVariableData", chunk);

            ApiResult result = daemonClient.send(chargingStationIdentity, "GetVariables", chunkPayload, null);
            log.info("[GetVariables] chunk [{},{}] status={}", i, i + chunk.size() - 1, result.getStatus());

            if (!"accepted".equals(result.getStatus())) {
                lastStatus = result.getStatus();
            }

            if (result.getData() instanceof Map) {
                Map<String, Object> dataMap = (Map<String, Object>) result.getData();
                Object chunkResult = dataMap.get("getVariableResult");
                if (chunkResult instanceof List) {
                    mergedResults.addAll((List<?>) chunkResult);
                }
            }
        }

        Map<String, Object> mergedData = new HashMap<>();
        mergedData.put("getVariableResult", mergedResults);

        ApiResult mergedResult = "accepted".equals(lastStatus)
                ? ApiResult.accepted(mergedData)
                : ApiResult.rejected("일부 청크 전송 실패");

        if ("accepted".equals(lastStatus)) {
            saveVariableResult(chargingStationIdentity, mergedData);
        }

        return ResponseEntity.ok(mergedResult);
    }

    /**
     * TB_CHCF002 에서 DeviceDataCtrlr/ItemsPerMessage(instance=GetVariables) 값을 조회.
     * 없거나 0 이하이면 Integer.MAX_VALUE (제한 없음) 반환.
     */
    private int resolveGetVariablesItemsPerMessage(String chargingStationIdentity) {
        int idx = chargingStationIdentity.lastIndexOf('-');
        if (idx < 0) return Integer.MAX_VALUE;

        String cpId = chargingStationIdentity.substring(0, idx);
        String csId = chargingStationIdentity.substring(idx + 1);

        try {
            CsVariable v = csVariableService.findOne(
                    cpId, csId,
                    "DeviceDataCtrlr",
                    "ItemsPerMessage",
                    "GetVariables",
                    "Actual");

            if (v != null && v.getAttrVal() != null) {
                int limit = Integer.parseInt(v.getAttrVal().trim());
                if (limit > 0) return limit;
            }
        } catch (Exception e) {
            log.warn("[GetVariables] ItemsPerMessage 조회 실패: cpCsId={} error={}", chargingStationIdentity, e.getMessage());
        }
        return Integer.MAX_VALUE;
    }

    /**
     * body: { "setVariableData": [ { "attributeValue": ..., "component": ..., "variable": ... } ] }
     *
     * 전송 성공 시 BasicAuthPassword 가 포함되어 있으면 새 비밀번호를 lastCsPassword 에 저장.
     * 충전기가 Accepted 응답하면 daemon 의 SetVariablesBean 에서 csPassword ← lastCsPassword 반영.
     */
    @PostMapping("/setVariables")
    public ResponseEntity<ApiResult> setVariables(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> payload) {

        ApiResult result = daemonClient.send(chargingStationIdentity, "SetVariables", payload, null);

        if ("accepted".equals(result.getStatus())) {
            stagePasswordIfPresent(chargingStationIdentity, payload);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * GetVariables 응답 data 에서 getVariableResult 배열을 파싱하여 TB_CHCF002 에 저장.
     */
    @SuppressWarnings("unchecked")
    private void saveVariableResult(String chargingStationIdentity, Object data) {
        if (data == null) return;

        int idx = chargingStationIdentity.lastIndexOf('-');
        if (idx < 0) {
            log.warn("[GetVariables] cpCsId 파싱 실패: {}", chargingStationIdentity);
            return;
        }
        String cpId = chargingStationIdentity.substring(0, idx);
        String csId = chargingStationIdentity.substring(idx + 1);

        try {
            List<Map<String, Object>> resultList = null;
            if (data instanceof Map) {
                resultList = (List<Map<String, Object>>) ((Map<String, Object>) data).get("getVariableResult");
            }
            if (resultList == null || resultList.isEmpty()) return;

            List<CsVariable> variables = new ArrayList<>();
            for (Map<String, Object> r : resultList) {
                CsVariable v = parseVariableResult(cpId, csId, r);
                if (v != null) variables.add(v);
            }

            if (!variables.isEmpty()) {
                csVariableService.saveAll(cpId, csId, variables);
                log.info("[GetVariables] 변수 저장 완료: cpId={} csId={} count={}", cpId, csId, variables.size());
            }
        } catch (Exception e) {
            log.error("[GetVariables] 변수 저장 실패: cpId={} csId={} error={}", cpId, csId, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private CsVariable parseVariableResult(String cpId, String csId, Map<String, Object> r) {
        Map<String, Object> comp = (Map<String, Object>) r.get("component");
        Map<String, Object> var  = (Map<String, Object>) r.get("variable");
        if (comp == null || var == null) return null;

        CsVariable v = new CsVariable();
        v.setCpId(cpId);
        v.setCsId(csId);

        v.setCompNm((String) comp.get("name"));
        v.setCompInst((String) comp.getOrDefault("instance", ""));

        Map<String, Object> evse = (Map<String, Object>) comp.get("evse");
        if (evse != null) {
            Object evseId = evse.get("id");
            if (evseId instanceof Number) v.setEvseId(((Number) evseId).intValue());
            Object connId = evse.get("connectorId");
            if (connId instanceof Number) v.setConnId(((Number) connId).intValue());
        }

        v.setVarNm((String) var.get("name"));
        v.setVarInst((String) var.getOrDefault("instance", ""));

        Object attrTp = r.get("attributeType");
        v.setAttrTp(attrTp != null ? attrTp.toString() : "Actual");
        v.setAttrVal((String) r.get("attributeValue"));
        v.setAttrStat((String) r.get("attributeStatus"));

        return v;
    }

    /**
     * 전송 성공 시 요청 payload 에 BasicAuthPassword 가 포함되어 있으면
     * 새 비밀번호를 lastCsPassword 에 미리 저장한다 (스테이징).
     */
    @SuppressWarnings("unchecked")
    private void stagePasswordIfPresent(String chargingStationIdentity, Map<String, Object> payload) {
        List<Map<String, Object>> setVariableData =
                (List<Map<String, Object>>) payload.get("setVariableData");
        if (setVariableData == null) return;

        String newPassword = null;
        for (Map<String, Object> item : setVariableData) {
            Map<String, Object> component = (Map<String, Object>) item.get("component");
            Map<String, Object> variable  = (Map<String, Object>) item.get("variable");
            if (component == null || variable == null) continue;
            if ("SecurityCtrlr".equals(component.get("name"))
                    && "BasicAuthPassword".equals(variable.get("name"))) {
                newPassword = (String) item.get("attributeValue");
                break;
            }
        }
        if (newPassword == null) return;

        int idx = chargingStationIdentity.lastIndexOf('-');
        if (idx < 0) {
            log.warn("[SetVariables] cpCsId 파싱 실패: {}", chargingStationIdentity);
            return;
        }
        String cpId = chargingStationIdentity.substring(0, idx);
        String csId = chargingStationIdentity.substring(idx + 1);

        try {
            ChargingStation cs =
                    chargingStationService.retrieveChargingStationByCpIdNCsId(cpId, csId);
            if (cs == null) {
                log.warn("[SetVariables] ChargingStation 없음: cpId={} csId={}", cpId, csId);
                return;
            }
            cs.setLastCsPassword(newPassword);
            chargingStationService.modifyChargingStation(cs);
            log.info("[SetVariables] lastCsPassword 스테이징 완료: cpId={} csId={}", cpId, csId);
        } catch (Exception e) {
            log.error("[SetVariables] lastCsPassword 스테이징 실패: cpId={} csId={} error={}", cpId, csId, e.getMessage());
        }
    }

    /** reportBase: FullInventory | SummaryInventory | ConfigurationInventory (required) */
    @RequestMapping(value = "/getBaseReport", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> getBaseReport(
            @RequestParam String chargingStationIdentity,
            @RequestParam String reportBase) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("requestId", requestIdSeq.getAndIncrement());
        payload.put("reportBase", reportBase);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetBaseReport", payload, null));
    }

    /**
     * body: { "componentCriteria": [...], "componentVariable": [...] }
     * requestId 는 CSMS 가 자동 생성하여 payload 에 추가한다.
     */
    @PostMapping("/getReport")
    public ResponseEntity<ApiResult> getReport(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> body) {

        body.put("requestId", requestIdSeq.getAndIncrement());

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetReport", body, null));
    }
}
