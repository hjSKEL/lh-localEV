/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.ocpp201.response.Heartbeat;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 6.
 */
@Component("Heartbeat")
public class HeartbeatBean implements ControlerBean {

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

        LOGGER.debug("HeartbeatBean Request : {}", text);
        // -09:00 시간, 기준시로 보냄.
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setCurrentTime(DateUtils.dateToString(new Date(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        LOGGER.debug("HeartbeatBean Response : {}", objectMapper.writeValueAsString(heartbeat));
        return objectMapper.valueToTree(heartbeat);
    }
}
