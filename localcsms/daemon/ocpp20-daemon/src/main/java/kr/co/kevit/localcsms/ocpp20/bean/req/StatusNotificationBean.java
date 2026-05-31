/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.util.Date;
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
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.process.CodeValService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargeStatusType;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 8.
 */
@Component("StatusNotification")
public class StatusNotificationBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusNotificationBean.class);
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired(required = false)
    private ChargingStationService chargingStationService;
    
    @Autowired(required = false)
    private ChargerStatusService chargerStatusService;
    
    @Autowired(required = false)
    private CodeValService codeValService;
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
            LOGGER.debug("StatusNotificationBean.control : {}", text);
        }
        
        kr.co.kevit.ocpp201.request.StatusNotification request = objectMapper.readValue(text, kr.co.kevit.ocpp201.request.StatusNotification.class);
        if(request.getEvseId() == 0) {
            return objectMapper.valueToTree(new kr.co.kevit.ocpp201.response.StatusNotification());
        }
        
        // 
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.stream().filter(s -> s.getEvseId() == request.getEvseId()).findFirst().orElse(null);
        if (chargerStatusInfo == null) {
            return objectMapper.valueToTree(new kr.co.kevit.ocpp201.response.StatusNotification());
        }
        ChargingStation station = chargingStationService.retrieveChargingStationByCpIdNCsId(chargerStatusInfo.getCpId(), chargerStatusInfo.getCsId());

        if(StringConstants.N.equals(station.getUseYn()) && !chargerStatusInfo.getEventCode().equals("EVT011")) {
            LOGGER.info("station UseYn N Exception");
            //강제 오류발생
            throw new SecurityException();
        }

        if(request.getTimestamp().contains(StringConstants.DOT)) {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS));
        }else {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
        }        
        
        switch(request.getConnectorStatus()) {
            case Available:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.STANDBY.getCode());
            break;

            case Reserved :
                chargerStatusInfo.setCsStatCode(ChargeStatusType.RESERVATION.getCode());
            break;
            case Occupied :
                chargerStatusInfo.setCsStatCode(ChargeStatusType.CHARGING.getCode());
            break;
            case Faulted :
            case Unavailable:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.REPAIRING.getCode());
            break; 
            default :
            break;
        }
        chargerStatusInfo.setEventCode(StringConstants.BLANK);
        chargerStatusInfo.setCutCardNo(null);
        chargerStatusInfo.setUpdateDate(new Date());
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);
        
        return objectMapper.valueToTree(new kr.co.kevit.ocpp201.response.StatusNotification());
    }
}