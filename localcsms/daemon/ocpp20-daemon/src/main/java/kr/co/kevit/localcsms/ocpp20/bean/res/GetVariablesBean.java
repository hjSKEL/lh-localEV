package kr.co.kevit.localcsms.ocpp20.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.kevit.localcsms.charger.entity.domain.CsVariable;
import kr.co.kevit.localcsms.charger.process.CsVariableService;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GetVariables CALLRESULT 처리.
 *
 * getVariableResult 배열을 순회하며 TB_CHCF002 에 저장.
 * 각 항목은 update → 없으면 insert 방식으로 처리.
 */
@Component("GetVariables")
public class GetVariablesBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(GetVariablesBean.class);

    @Autowired
    private CsVariableService csVariableService;

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        log.debug("[GetVariables] cpCsId={} payload={}", cpCsId, payload);

        JsonNode results = payload.get("getVariableResult");
        if (results == null || !results.isArray() || results.isEmpty()) {
            log.debug("[GetVariables] getVariableResult 없음 또는 빈 배열: {}", cpCsId);
            return;
        }

        int idx = cpCsId.lastIndexOf('-');
        if (idx < 0) {
            log.warn("[GetVariables] cpCsId 파싱 실패: {}", cpCsId);
            return;
        }
        String cpId = cpCsId.substring(0, idx);
        String csId = cpCsId.substring(idx + 1);

        int saved = 0;
        int failed = 0;
        for (JsonNode r : results) {
            try {
                CsVariable v = parseResult(cpId, csId, r);
                if (v == null) continue;
                csVariableService.saveVariable(v);
                saved++;
            } catch (Exception e) {
                failed++;
                log.warn("[GetVariables] 변수 저장 실패: cpCsId={} node={} error={}", cpCsId, r, e.getMessage());
            }
        }
        log.info("[GetVariables] 저장 완료: cpId={} csId={} saved={} failed={}", cpId, csId, saved, failed);
    }

    private CsVariable parseResult(String cpId, String csId, JsonNode r) {
        JsonNode comp = r.get("component");
        JsonNode var  = r.get("variable");
        if (comp == null || var == null) return null;

        CsVariable v = new CsVariable();
        v.setCpId(cpId);
        v.setCsId(csId);

        v.setCompNm(comp.path("name").asText(""));
        v.setCompInst(comp.path("instance").asText(""));

        JsonNode evse = comp.get("evse");
        if (evse != null) {
            v.setEvseId(evse.path("id").asInt(0));
            v.setConnId(evse.path("connectorId").asInt(0));
        }

        v.setVarNm(var.path("name").asText(""));
        v.setVarInst(var.path("instance").asText(""));

        String attrTp = r.path("attributeType").asText("Actual");
        v.setAttrTp(attrTp.isEmpty() ? "Actual" : attrTp);

        JsonNode attrVal = r.get("attributeValue");
        v.setAttrVal(attrVal != null && !attrVal.isNull() ? attrVal.asText() : null);
        v.setAttrStat(r.path("attributeStatus").asText(""));

        return v;
    }
}
