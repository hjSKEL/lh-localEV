package kr.co.kevit.localcsms.ocpp20.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.co.kevit.localcsms.smartcharging.process.DynamicScheduleManager;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.ocpp201.domain.ChargingScheduleUpdateType;
import kr.co.kevit.ocpp201.enumtype.ChargingProfileStatusEnumType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PullDynamicScheduleUpdate (2.1, K28) — CS 가 Dynamic 프로파일 최신 setpoint/limit pull.
 *
 * <p>chargingProfileId 로 {@link DynamicScheduleManager} 위임:
 * 미존재 → Rejected, 존재 → Accepted + scheduleUpdate.</p>
 *
 * @author bckim
 */
@Component("PullDynamicScheduleUpdate")
public class PullDynamicScheduleUpdateBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullDynamicScheduleUpdateBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired(required = false)
    private DynamicScheduleManager dynamicScheduleManager;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        kr.co.kevit.ocpp201.request.PullDynamicScheduleUpdate request =
                objectMapper.readValue(msg.getPayload().toString(), kr.co.kevit.ocpp201.request.PullDynamicScheduleUpdate.class);
        kr.co.kevit.ocpp201.response.PullDynamicScheduleUpdate response =
                new kr.co.kevit.ocpp201.response.PullDynamicScheduleUpdate();

        ChargingScheduleUpdateType update = dynamicScheduleManager == null ? null
                : dynamicScheduleManager.computeUpdate(request.getChargingProfileId(), csIds[0], csIds[1]);

        if (update == null) {
            LOGGER.info("PullDynamicScheduleUpdate: unknown chargingProfileId={} cpCsId={}",
                    request.getChargingProfileId(), cpCsId);
            response.setStatus(ChargingProfileStatusEnumType.Rejected);
        } else {
            response.setStatus(ChargingProfileStatusEnumType.Accepted);
            response.setScheduleUpdate(update);
        }
        return objectMapper.valueToTree(response);
    }
}
