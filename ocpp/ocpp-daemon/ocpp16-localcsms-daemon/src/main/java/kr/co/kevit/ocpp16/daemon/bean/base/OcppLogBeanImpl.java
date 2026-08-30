/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.base;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.common.util.enumtype.ocpp.OCPPMsgDirectType;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.recharger.entity.domain.OcppLog;
import kr.co.kevit.localcsms.recharger.process.OcppLogService;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 31.
 */
public class OcppLogBeanImpl implements OcppLogBean{
    
    private final String Heartbeat = "Heartbeat";
    
    @Autowired
    private OcppLogService ocppLogService;
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void requestLog(String id, String actionName, String ids, String connectorId, String text) {
//        if(Heartbeat.equals(actionName)) {
//            return ;
//        }
        OcppLog log = new OcppLog();
        log.setId(id);
        log.setAction(actionName);
        String cpCsIds [] = ids.split(StringConstants.DASH);
        log.setCpId(cpCsIds[0]);
        log.setCsId(cpCsIds[1]);
        log.setDirectType(OCPPMsgDirectType.CP2CSMS);
        log.setOcppVer(StringConstants.OCPP16);
        log.setMessageTypeId(StringConstants.TWO);
        log.setPayloadJson(text);
        log.setRegDate(new Date());
        ocppLogService.registerOcppLog(log);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void responseLog(String id, String messageTypeId, String actionName, String ids, String connectorId, String text) {
//        if(Heartbeat.equals(actionName)) {
//            return ;
//        }
        OcppLog log = new OcppLog();
        log.setId(id);
        log.setAction(actionName);
        String cpCsIds [] = ids.split(StringConstants.DASH);
        log.setCpId(cpCsIds[0]);
        log.setCsId(cpCsIds[1]);
        log.setDirectType(OCPPMsgDirectType.CSMS2CP);
        log.setOcppVer(StringConstants.OCPP16);
        log.setMessageTypeId(messageTypeId);
        log.setPayloadJson(text);
        log.setRegDate(new Date());
        ocppLogService.registerOcppLog(log);
    }

}
