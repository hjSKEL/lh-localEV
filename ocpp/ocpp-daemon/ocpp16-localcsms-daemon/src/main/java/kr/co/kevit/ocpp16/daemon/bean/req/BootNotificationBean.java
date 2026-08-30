/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStationCsm;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.domain.CodeVal;
import kr.co.kevit.localcsms.common.process.CodeValService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;
import kr.co.kevit.ocpp16.enumtype.BootNotificationResponseStatusEnum;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 8.
 */
public class BootNotificationBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootNotificationBean.class);
    
    @Autowired
    private ChargerStatusService chargerStatusService;
    
    @Autowired
    private ChargingStationService chargingStationService;
    
    @Autowired
    private CodeValService codeValService;
    
    private final String EVENTCODE = "EVT0J4";
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Object control(String csId, List<Object> reqs) {
        //
        String[] csIds = csId.split(StringConstants.DASH);
        
        Object object = reqs.get(3);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        kr.co.kevit.ocpp16.request.BootNotification obj = gson.fromJson(text,kr.co.kevit.ocpp16.request.BootNotification.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("BootNotificationBean text : {}", text);
        }
        
        kr.co.kevit.ocpp16.response.BootNotification response = new kr.co.kevit.ocpp16.response.BootNotification();
        response.setCurrentTime(DateUtils.dateToString(new Date(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        CodeVal codeVal = codeValService.retrieveCodeValByCode("OCPP01");
        response.setInterval(Integer.parseInt(codeVal.getCodeValue())); //300 seconds
        
        
        ChargingStation chargingStation = chargingStationService.retrieveChargingStationByCpIdNCsId(csIds[0], csIds[1]);
        if(chargingStation == null) {
            response.setStatus(BootNotificationResponseStatusEnum.Rejected);
            return response;
        }
        
        
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        if(StringConstants.N.equals(chargingStation.getUseYn()) || chargerStatusInfos.isEmpty()) {
            response.setStatus(BootNotificationResponseStatusEnum.Pending);
            return response;
        }
        
        for(ChargerStatusInfo chargerStatusInfo : chargerStatusInfos) {
            chargerStatusInfo.setInfoCollDate(new Date());
            chargerStatusInfo.setEventCode(EVENTCODE);
            chargerStatusInfo.setCuEleEnerge(BigDecimal.ZERO);
            chargerStatusInfo.setCutCardNo(null);
            chargerStatusInfo.setRechargingId(null);
            chargerStatusInfo.setInstChSum(BigDecimal.ZERO);// 순간충전금액
            chargerStatusInfo.setInstChAmont(BigDecimal.ZERO);// 순간 충전량
            chargerStatusInfo.setInstChCost(BigDecimal.ZERO);// 순간충전단가
            chargerStatusInfo.setChSum(BigDecimal.ZERO);// 충전금액
            chargerStatusInfo.setChStartDate(null);// 충전시작시간
            chargerStatusInfo.setChEndDate(null);//충전종료시간
            chargerStatusInfo.setUpdateDate(new Date());
            chargerStatusService.modifyChargerStatus(chargerStatusInfo);            
        }
        ChargingStationCsm csm = new ChargingStationCsm();
        csm.setCpId(csIds[0]);
        csm.setCsId(csIds[1]);
        csm.setSerialNumber(obj.getChargeBoxSerialNumber());
        csm.setModelName(obj.getChargePointModel());
        csm.setFwVer(obj.getFirmwareVersion());
        csm.setLastBootDate(new Date());
        chargingStationService.modifyChargingStationCsm(csm);
        response.setStatus(BootNotificationResponseStatusEnum.Accepted);
        return response;
    }
}