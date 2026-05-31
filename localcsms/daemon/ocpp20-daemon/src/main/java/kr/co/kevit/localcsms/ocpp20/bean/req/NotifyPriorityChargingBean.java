package kr.co.kevit.localcsms.ocpp20.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * NotifyPriorityCharging (K21) — CS 가 우선 충전 활성/해제를 CSMS 에 통보.
 *
 * <p>NotifyPriorityChargingResponse 는 본문 필드가 없어 빈 객체 응답이 스펙상 정상.</p>
 */
@Component("NotifyPriorityCharging")
public class NotifyPriorityChargingBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyPriorityChargingBean.class);

    @Autowired
    private ObjectMapper objectMapper;

    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        kr.co.kevit.ocpp201.request.NotifyPriorityCharging req =
                objectMapper.readValue(msg.getPayload().toString(), kr.co.kevit.ocpp201.request.NotifyPriorityCharging.class);
        LOGGER.info("NotifyPriorityCharging cpCsId={} transactionId={} activated={}",
                cpCsId, req.getTransactionId(), req.isActivated());

        return objectMapper.valueToTree(new kr.co.kevit.ocpp201.response.NotifyPriorityCharging());
    }

}
