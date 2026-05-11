package kr.co.kevit.localcsms.ocpp20.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessage;
import kr.co.kevit.localcsms.charger.entity.shared.DisplayMessageSearchCond;
import kr.co.kevit.localcsms.charger.process.DisplayMessageService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("SetDisplayMessage")
public class SetDisplayMessageBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(SetDisplayMessageBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DisplayMessageService displayMessageService;

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        kr.co.kevit.ocpp201.response.SetDisplayMessage response =
                objectMapper.treeToValue(payload, kr.co.kevit.ocpp201.response.SetDisplayMessage.class);
        log.debug("SetDisplayMessageBean cpCsId={} status={}", cpCsId, response.getStatus());

        // cpCsId → cpId, csId (format: "223401-01")
        int idx = cpCsId.lastIndexOf('-');
        if (idx < 0) {
            log.warn("[SetDisplayMessage] cpCsId 형식 오류: {}", cpCsId);
            return;
        }
        String cpId = cpCsId.substring(0, idx);
        String csId = cpCsId.substring(idx + 1);

        // 가장 최근 DisplayMessage 조회 (MSG_ID DESC LIMIT 1)
        DisplayMessageSearchCond cond = new DisplayMessageSearchCond();
        cond.setCpId(cpId);
        cond.setCsId(csId);
        cond.setPageItemSize(1);

        Page<DisplayMessage> page = displayMessageService.retrieveBySearchCond(cond);
        if (page == null || page.getResult() == null || page.getResult().isEmpty()) {
            log.warn("[SetDisplayMessage] DisplayMessage 없음: cpId={} csId={}", cpId, csId);
            return;
        }

        DisplayMessage dm = page.getResult().get(0);
        String statusStr = response.getStatus() != null ? response.getStatus().name() : null;
        dm.setCsStatus(statusStr);

        if (dm.getWriter() == null) {
            dm.setWriter(new Writer(StringConstants.SYSTEM_EMPLOYEE));
        } else {
            dm.getWriter().setUpdUserId(StringConstants.SYSTEM_EMPLOYEE);
        }

        displayMessageService.updateMessage(dm);
        log.info("[SetDisplayMessage] CS_STATUS 갱신: cpId={} csId={} messageId={} status={}",
                cpId, csId, dm.getMessageId(), statusStr);
    }
}
