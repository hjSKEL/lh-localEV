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
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
@Component("LogStatusNotification")
public class LogStatusNotificationBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String text = msg.getPayload().toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("LogStatusNotificationBean.control : {}", text);
        }
        kr.co.kevit.ocpp201.request.LogStatusNotification request = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.LogStatusNotification.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getRequestId : {}", request.getRequestId());
            LOGGER.debug("getStatus : {}", request.getStatus());
        }
        kr.co.kevit.ocpp201.response.LogStatusNotification response = new kr.co.kevit.ocpp201.response.LogStatusNotification();
        return objectMapper.valueToTree(response);
    }
}
