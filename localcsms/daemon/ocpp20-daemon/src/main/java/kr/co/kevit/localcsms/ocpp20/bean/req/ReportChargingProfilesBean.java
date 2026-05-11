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
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2022. 9. 8.
 */
@Component("ReportChargingProfiles")
public class ReportChargingProfilesBean implements ControlerBean{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportChargingProfilesBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String[] csIds = cpCsId.split(StringConstants.DASH);
        
        String text = msg.getPayload().toString();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("ReportChargingProfilesBean text : {}", text);
        }
//        kr.co.kevit.ocpp201.request.ReportChargingProfiles obj = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.ReportChargingProfiles.class);
        
        kr.co.kevit.ocpp201.response.ReportChargingProfiles response = new kr.co.kevit.ocpp201.response.ReportChargingProfiles();
        return objectMapper.valueToTree(response);
    }

}