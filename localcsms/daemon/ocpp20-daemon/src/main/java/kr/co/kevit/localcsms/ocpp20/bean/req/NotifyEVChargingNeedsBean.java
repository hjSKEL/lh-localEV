/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.ocpp201.enumtype.NotifyEVChargingNeedsStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2022. 9. 8.
 */
@Component("NotifyEVChargingNeeds")
public class NotifyEVChargingNeedsBean implements ControlerBean{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyEVChargingNeedsBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired(required = false)
    private ChargerStatusService chargerStatusService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String[] csIds = cpCsId.split(StringConstants.DASH);
        
        String text = msg.getPayload().toString();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("NotifyEVChargingNeedsBean text : {}", text);
        }
        kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds obj = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds.class);
        kr.co.kevit.ocpp201.response.NotifyEVChargingNeeds response = new kr.co.kevit.ocpp201.response.NotifyEVChargingNeeds();
        //충전ID 조회
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.stream().filter(s -> s.getEvseId() == obj.getEvseId()).findFirst().orElse(null);
        if (chargerStatusInfo == null || StringUtils.isEmpty(chargerStatusInfo.getRechargingId())) {
            LOGGER.info("Charging does not exist.");
            response.setStatus(NotifyEVChargingNeedsStatusEnumType.Rejected);
            return objectMapper.valueToTree(response);
        }
        
        response.setStatus(NotifyEVChargingNeedsStatusEnumType.Accepted);
        return objectMapper.valueToTree(response);
    }

}