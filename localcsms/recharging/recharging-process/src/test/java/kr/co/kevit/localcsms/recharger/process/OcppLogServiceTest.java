/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.enumtype.ocpp.OCPPMsgDirectType;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.recharger.entity.domain.OcppLog;
import kr.co.kevit.localcsms.recharger.entity.shared.OcppLogSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 15.
 */
public class OcppLogServiceTest extends AbstractTestCase {
    
    @Autowired
    private OcppLogService service;

    private OcppLog registerOcppLog() {
        OcppLog log = new OcppLog();
        log.setAction("Heartbeat");
        log.setCpId("123456");
        log.setCsId("01");
        log.setDirectType(OCPPMsgDirectType.CP2CSMS);
        log.setId(UUID.randomUUID().toString());
        log.setMessageTypeId("2");
        log.setOcppVer(StringConstants.OCPP201);
        log.setPayloadJson("{}");
        log.setRegDate(new Date());
        service.registerOcppLog(log);
        return log;
    }
    
    @Test
    public void testRegisterOcppLog() {
        OcppLog log = registerOcppLog();
        assertNotNull(log);
    }
    
    @Test
    public void retrieveOcppLogByOcppLogSearchCond() {
        //
        OcppLog log = registerOcppLog();
        OcppLogSearchCond searchCond = new OcppLogSearchCond();
        searchCond.setAction(log.getAction());
        searchCond.setCpId(log.getCpId());
        searchCond.setCsId(log.getCsId());
        searchCond.setId(log.getId());
        searchCond.setMessageTypeId(log.getMessageTypeId());
        searchCond.setFromDate("20230718");
        searchCond.setToDate("20230720");
        Page<OcppLog> resultSet = service.retrieveOcppLogByOcppLogSearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }
}
