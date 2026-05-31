/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
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
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.exception.KEVITErrorCode;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 11.
 */
@Component("NotifyEvent")
public class NotifyEventBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyEventBean.class);
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired(required = false)
    private ChargingStationService chargingStationService;
    
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
            LOGGER.debug("NotifyEventBean.control : {}", text);
        }
        kr.co.kevit.ocpp201.request.NotifyEvent request = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.NotifyEvent.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getGeneratedAt : {}", request.getGeneratedAt());
            LOGGER.debug("getSeqNo : {}", request.getSeqNo());
        }
        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(csIds[0], csIds[1]);
        if(station == null) {
            throw new KEVITException(KEVITErrorCode.INTERNAL_ERR.getCode());
        }

        return objectMapper.valueToTree(new kr.co.kevit.ocpp201.response.NotifyEvent());
    }

}