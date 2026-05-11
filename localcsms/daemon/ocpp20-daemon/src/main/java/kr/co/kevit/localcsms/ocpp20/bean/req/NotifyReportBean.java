/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2022. 2. 21.
 */
@Component("NotifyReport")
public class NotifyReportBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyReportBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String[] csIds = cpCsId.split(StringConstants.DASH);
        
        String text = msg.getPayload().toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NotifyReportBean.control : {}", text);
        }
        kr.co.kevit.ocpp201.request.NotifyReport request = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.NotifyReport.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NotifyReportBean.getGeneratedAt : {}", request.getGeneratedAt());
            LOGGER.debug("NotifyReportBean.getSeqNo : {}", request.getSeqNo());
            LOGGER.debug("NotifyReportBean.getRequestId : {}", request.getRequestId());
            
        }
        return objectMapper.valueToTree(new kr.co.kevit.ocpp201.response.NotifyReport());
    }

}