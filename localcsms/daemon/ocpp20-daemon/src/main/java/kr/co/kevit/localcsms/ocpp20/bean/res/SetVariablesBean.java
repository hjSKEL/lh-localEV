package kr.co.kevit.localcsms.ocpp20.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SetVariables CALLRESULT 처리.
 *
 * SecurityCtrlr/BasicAuthPassword 가 Accepted 되면
 * csPassword ← lastCsPassword (api-eai 가 미리 스테이징해 둔 새 비밀번호) 로 DB 확정.
 * Rejected 등 다른 상태는 무시한다.
 */
@Component("SetVariables")
public class SetVariablesBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(SetVariablesBean.class);

    @Autowired
    private ChargingStationService chargingStationService;

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        log.debug("[SetVariables] cpCsId={} payload={}", cpCsId, payload);

        JsonNode results = payload.get("setVariableResult");
        if (results == null || !results.isArray()) return;

        for (JsonNode result : results) {
            String status        = result.path("attributeStatus").asText("");
            String componentName = result.path("component").path("name").asText("");
            String variableName  = result.path("variable").path("name").asText("");

            if ("SecurityCtrlr".equals(componentName)
                    && "BasicAuthPassword".equals(variableName)) {

                if (!"Accepted".equals(status)) {
                    log.info("[SetVariables] BasicAuthPassword {} — DB 갱신 생략: {}", status, cpCsId);
                    return;
                }

                applyPassword(cpCsId);
                break;
            }
        }
    }

    /**
     * csPassword ← lastCsPassword 확정.
     * api-eai 가 전송 시점에 lastCsPassword 에 새 비밀번호를 스테이징해 두었으므로
     * 충전기가 Accepted 하면 csPassword 로 확정한다.
     */
    private void applyPassword(String cpCsId) {
        int idx = cpCsId.lastIndexOf('-');
        if (idx < 0) {
            log.warn("[SetVariables] cpCsId 파싱 실패: {}", cpCsId);
            return;
        }
        String cpId = cpCsId.substring(0, idx);
        String csId = cpCsId.substring(idx + 1);

        try {
            ChargingStation cs =
                    chargingStationService.retrieveChargingStationByCpIdNCsId(cpId, csId);
            if (cs == null) {
                log.warn("[SetVariables] ChargingStation 없음: cpId={} csId={}", cpId, csId);
                return;
            }
            String lastPwd = cs.getLastCsPassword();
            if (lastPwd == null || lastPwd.isBlank()) {
                log.warn("[SetVariables] lastCsPassword 비어 있음 — 갱신 생략: {}", cpCsId);
                return;
            }
            cs.setCsPassword(lastPwd);
            chargingStationService.modifyChargingStation(cs);
            log.info("[SetVariables] csPassword 확정 완료: cpId={} csId={} (lastCsPassword → csPassword)", cpId, csId);
        } catch (Exception e) {
            log.error("[SetVariables] csPassword 확정 실패: cpId={} csId={} error={}", cpId, csId, e.getMessage());
        }
    }
}
