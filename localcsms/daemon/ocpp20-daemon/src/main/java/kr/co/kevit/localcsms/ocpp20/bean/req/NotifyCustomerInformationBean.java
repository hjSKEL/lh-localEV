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
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2022. 2. 21.
 */
@Component("NotifyCustomerInformation")
public class NotifyCustomerInformationBean implements ControlerBean{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyCustomerInformationBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String[] csIds = cpCsId.split(StringConstants.DASH);
        
        String text = msg.getPayload().toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NotifyCustomerInformationBean text : {}", text);
        }
//        kr.co.kevit.ocpp201.request.NotifyCustomerInformation obj = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.NotifyCustomerInformation.class);

        
        kr.co.kevit.ocpp201.response.NotifyCustomerInformation response = new kr.co.kevit.ocpp201.response.NotifyCustomerInformation();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NotifyCustomerInformationBean.response : {}", response);
        }
        return objectMapper.valueToTree(response);
    }

}