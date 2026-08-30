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
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargeStatusType;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;
import kr.co.kevit.ocpp16.daemon.store.DaemonStore;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 8.
 */
public class StatusNotificationBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusNotificationBean.class);
    
    @Autowired
    private ChargerStatusService chargerStatusService;
    
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
        
        kr.co.kevit.ocpp16.request.StatusNotification request = gson.fromJson(text, kr.co.kevit.ocpp16.request.StatusNotification.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("text : {}", text);
        }
        if(request.getTimestamp() == null) {
            return new Object();
        }
        
        String connectorId = Integer.toString(request.getConnectorId());

        String lastStatus = DaemonStore.StatusNotificationMap.get(csId.concat(StringConstants.UNDER_LINE).concat(connectorId));
        String curStatus = request.getStatus().name();
        if(StringUtils.isNotEmpty(lastStatus) && lastStatus.equals(curStatus)){
            LOGGER.debug("StatusNotificationBean Cache Response : {}", text);
            return new Object();
        }
        DaemonStore.StatusNotificationMap.put(csId.concat(StringConstants.UNDER_LINE).concat(connectorId), curStatus);

        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.get(0);
        if(chargerStatusInfos.size() > 1) {
            for(ChargerStatusInfo datum : chargerStatusInfos) {
                if(datum.getEvseId() == request.getConnectorId()) {
                    chargerStatusInfo = datum;
                    break;
                }
            }
        }
        if(request.getTimestamp().contains(StringConstants.DOT)) {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC));
        }else {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        }
        
        switch(request.getStatus()) {
            case Available:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.STANDBY.getCode());
                chargerStatusInfo.setCsErrorStatus(StringConstants.BLANK);
                chargerStatusInfo.setCutCardNo(null);
                chargerStatusInfo.setCsCableStatus(StringConstants.ZERO);
            break;
            case Charging:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.CHARGING.getCode());
                chargerStatusInfo.setCutCardNo(null);
            break;
            case Finishing :
                chargerStatusInfo.setCsStatCode(ChargeStatusType.COMPLETE.getCode());
                chargerStatusInfo.setCutCardNo(null);
            break;
            case Preparing :
                chargerStatusInfo.setCsStatCode(ChargeStatusType.STANDBY4USE.getCode());
            break;
            case Reserved :
                chargerStatusInfo.setCsStatCode(ChargeStatusType.RESERVATION.getCode());
                chargerStatusInfo.setCutCardNo(null);
            break;
            case Faulted :
            case Unavailable:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.REPAIRING.getCode());
                chargerStatusInfo.setCsErrorStatus(request.getVendorErrorCode());
                chargerStatusInfo.setCutCardNo(null);
            break;
            default :
            break;
        }
        chargerStatusInfo.setEventCode(StringConstants.BLANK);
        chargerStatusInfo.setUpdateDate(new Date());
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);
        return new Object();
    }
}