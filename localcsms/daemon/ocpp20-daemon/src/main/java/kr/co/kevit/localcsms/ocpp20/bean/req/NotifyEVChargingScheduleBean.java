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
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.common.domain.CodeVal;
import kr.co.kevit.localcsms.common.process.CodeValService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.ocpp201.enumtype.GenericStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2022. 9. 8.
 */
@Component("NotifyEVChargingSchedule")
public class NotifyEVChargingScheduleBean implements ControlerBean{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyEVChargingScheduleBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired(required = false)
    private CodeValService codeValService;
    
    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String[] csIds = cpCsId.split(StringConstants.DASH);
        
        String text = msg.getPayload().toString();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NotifyEVChargingScheduleBean text : {}", text);
        }
//        kr.co.kevit.ocpp201.request.NotifyEVChargingSchedule obj = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.NotifyEVChargingSchedule.class);

        kr.co.kevit.ocpp201.response.NotifyEVChargingSchedule response = new kr.co.kevit.ocpp201.response.NotifyEVChargingSchedule();
        CodeVal codeVal = codeValService.retrieveCodeValByCode("OCPP02");
        if(codeVal == null || !"true".equals(codeVal.getCodeValue())) {            
            response.setStatus(GenericStatusEnumType.Rejected);
        }else {
            response.setStatus(GenericStatusEnumType.Accepted);
        }
        
        return objectMapper.valueToTree(response);
    }

}