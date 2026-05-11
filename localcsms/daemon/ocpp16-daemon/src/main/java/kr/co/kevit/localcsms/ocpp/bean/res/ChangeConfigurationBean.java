package kr.co.kevit.localcsms.ocpp.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.ocpp.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ChangeConfiguration CALLRESULT 처리.
 *
 * 충전기가 Accepted 응답하면 lastCsPassword → csPassword 확정.
 * (api-eai updatePassword 에서 lastCsPassword 에 신규 비밀번호를 미리 스테이징해 둠)
 */
@Component
public class ChangeConfigurationBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(ChangeConfigurationBean.class);

    @Autowired
    private ChargingStationService chargingStationService;

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        String status = payload.path("status").asText("");
        log.debug("[ChangeConfiguration] cpCsId={} status={}", cpCsId, status);

        if ("Accepted".equals(status)) {
            applyPassword(cpCsId);
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
            log.warn("[ChangeConfiguration] cpCsId 파싱 실패: {}", cpCsId);
            return;
        }
        String cpId = cpCsId.substring(0, idx);
        String csId = cpCsId.substring(idx + 1);

        try {
            ChargingStation cs =
                    chargingStationService.retrieveChargingStationByCpIdNCsId(cpId, csId);
            if (cs == null) {
                log.warn("[ChangeConfiguration] ChargingStation 없음: cpId={} csId={}", cpId, csId);
                return;
            }
            String lastPwd = cs.getLastCsPassword();
            if (lastPwd == null || lastPwd.isBlank()) {
                log.debug("[ChangeConfiguration] lastCsPassword 비어 있음 — 비밀번호 변경 아닌 일반 ChangeConfiguration: {}", cpCsId);
                return;
            }
            cs.setCsPassword(lastPwd);
            cs.setLastCsPassword(null);
            chargingStationService.modifyChargingStation(cs);
            log.info("[ChangeConfiguration] csPassword 확정 완료: cpId={} csId={}", cpId, csId);
        } catch (Exception e) {
            log.error("[ChangeConfiguration] csPassword 확정 실패: cpId={} csId={} error={}", cpId, csId, e.getMessage());
        }
    }
}
