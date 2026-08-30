/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.req;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargerStatusInfoHisService;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.customer.entity.domain.CustomerMgt;
import kr.co.kevit.localcsms.customer.process.CustomerMgtService;
import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;
import kr.co.kevit.ocpp16.domain.IdTagInfo;
import kr.co.kevit.ocpp16.enumtype.IdTagInfoStatus;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 11.
 */
public class AuthorizeBean implements ControlerBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeBean.class);
    
    @Autowired
    private ChargerStatusService chargerStatusService;
    
    @Autowired
    private ChargerStatusInfoHisService csStatusHisService;

    @Autowired
    private CustomerMgtService customerService;

    @Autowired
    private ChargingStationService chargingStationService;
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Object control(String csId, List<Object> reqs) {
        //
        LOGGER.debug("Auth CSID : {}", csId);
        String[] csIds = csId.split(StringConstants.DASH);
        
        Object object = reqs.get(3);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        kr.co.kevit.ocpp16.request.Authorize request = gson.fromJson(text,kr.co.kevit.ocpp16.request.Authorize.class);
        kr.co.kevit.ocpp16.response.Authorize response = new kr.co.kevit.ocpp16.response.Authorize();
        IdTagInfo idTagInfo = new IdTagInfo();
        idTagInfo.setParentIdTag(StringConstants.BLANK);
        Date curDt = new Date();
        curDt = DateUtils.changeDateWithDayLevel(curDt, 7);
        idTagInfo.setExpiryDate(DateUtils.dateToString(curDt, DateUtils.RFC3339_DEFAULT_DATE_FORMAT));

        ChargingStation chargingStation = chargingStationService.retrieveChargingStationByCpIdNCsId(csIds[0], csIds[1]);
        if(StringConstants.N.equals(chargingStation.getUseYn())) {
            idTagInfo.setStatus(IdTagInfoStatus.Invalid);
            response.setIdTagInfo(idTagInfo);
            return response;
        }
        // 사용자인증 이벤트 저장
        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.get(0);
        chargerStatusInfo.setInfoCollDate(new Date());
        chargerStatusInfo.setEventCode("EVT0A1");
        chargerStatusInfo.setCutCardNo(request.getIdTag());
        chargerStatusInfo.setUpdateDate(new Date());

        CustomerMgt customerMgt = customerService.retrieveCustomerMgtByCustomerCardNo(request.getIdTag());
        // 서버내에 등록되지 않는 회원 카드번호
        if(customerMgt == null) {
            chargerStatusInfo.setEvseId(0);
            chargerStatusInfo.setEventCode("EVT0A2");
            csStatusHisService.registerChargerStatusHis(chargerStatusInfo);
            idTagInfo.setStatus(IdTagInfoStatus.Invalid);
            response.setIdTagInfo(idTagInfo);
            return response;
        }
        
        if(chargerStatusInfos.size() == 1) {
            chargerStatusService.modifyChargerStatus(chargerStatusInfo);
        }else {
            chargerStatusInfo.setEvseId(0);
            csStatusHisService.registerChargerStatusHis(chargerStatusInfo);
        }

        idTagInfo.setStatus(IdTagInfoStatus.Accepted);
        response.setIdTagInfo(idTagInfo);
        return response;
        
    }
}