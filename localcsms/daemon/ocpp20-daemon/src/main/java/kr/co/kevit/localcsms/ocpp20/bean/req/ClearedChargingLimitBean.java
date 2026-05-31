/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.smartcharging.process.ExternalLimitManager;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ClearedChargingLimit (K15) — CS 가 외부 충전 제약 해제 통보 → 비활성화.
 *
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2022. 9. 13.
 */
@Component("ClearedChargingLimit")
public class ClearedChargingLimitBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearedChargingLimitBean.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private ExternalLimitManager externalLimitManager;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        String text = msg.getPayload().toString();
        kr.co.kevit.ocpp201.request.ClearedChargingLimit obj =
                objectMapper.readValue(text, kr.co.kevit.ocpp201.request.ClearedChargingLimit.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ClearedChargingLimitBean.control : {}", text);
        }
        if (externalLimitManager != null) {
            try {
                externalLimitManager.clearLimit(csIds[0], csIds[1], obj);
            } catch (Exception ex) {
                LOGGER.warn("외부제약 해제 실패 cpCsId={}: {}", cpCsId, ex.getMessage());
            }
        }
        return objectMapper.valueToTree(new kr.co.kevit.ocpp201.response.ClearedChargingLimit());
    }
}