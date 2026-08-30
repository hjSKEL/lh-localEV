/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.ocpp16.daemon.BeanStore;
import kr.co.kevit.ocpp16.daemon.bean.BasicAuthBean;
import kr.co.kevit.ocpp16.daemon.util.StringConstants;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 12. 2.
 */
public class KevitBasicAuthBean implements BasicAuthBean{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(KevitBasicAuthBean.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSuccessed(String auth) {
        // 
        String [] auths = auth.split(StringConstants.COLON);
        LOGGER.info("ID : {}, PW : {}", auths[0], auths[1]);
        String [] csIds = auths[0].split("-");
        ChargingStationService csService = (ChargingStationService) BeanStore.getInstance().getBean("chargingStationServiceImpl");
        ChargingStation cs = csService.retrieveChargingStationByCpIdNCsId(csIds[0], csIds[1]);
        if(cs == null) {
            LOGGER.error("KevitBasicAuthBean CSAU0001 ID : {}",auths[0]);
            return false;
        }
        if (!cs.getCsPassword().equals(auths[1])) {
            LOGGER.error("KevitBasicAuthBean CSAU0002 ID : {}, PW : {} ",auths[0], auths[1]);
            return false;
        }
        return true;
    }

}
