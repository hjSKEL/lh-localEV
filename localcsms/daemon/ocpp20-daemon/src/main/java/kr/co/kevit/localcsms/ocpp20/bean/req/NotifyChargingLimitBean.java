/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.smartcharging.process.ExternalLimitManager;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * NotifyChargingLimit (K15) — 외부(EMS/CSO/grid) 충전 제약 통보 수신 → 영속.
 *
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2022. 9. 8.
 */
@Component("NotifyChargingLimit")
public class NotifyChargingLimitBean implements ControlerBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyChargingLimitBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired(required = false)
    private ExternalLimitManager externalLimitManager;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        String text = msg.getPayload().toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NotifyChargingLimitBean text : {}", text);
        }
        kr.co.kevit.ocpp201.request.NotifyChargingLimit obj =
                objectMapper.readValue(text, kr.co.kevit.ocpp201.request.NotifyChargingLimit.class);

        if (externalLimitManager != null) {
            try {
                externalLimitManager.applyLimit(csIds[0], csIds[1], obj);
            } catch (Exception ex) {
                LOGGER.warn("외부제약 적용 실패 cpCsId={}: {}", cpCsId, ex.getMessage());
            }
        }

        kr.co.kevit.ocpp201.response.NotifyChargingLimit response = new kr.co.kevit.ocpp201.response.NotifyChargingLimit();
        return objectMapper.valueToTree(response);
    }

}
