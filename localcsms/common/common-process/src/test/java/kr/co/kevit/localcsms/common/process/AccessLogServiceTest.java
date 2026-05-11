/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.shared.AccessLogSearchCond;
import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 15.
 */
public class AccessLogServiceTest extends AbstractTestCase{
    
    @Autowired
    private AccessLogService service;
    
    private AccessLog registerAccessLog() {
        //
        AccessLog accessLog = new AccessLog();
        accessLog.setLogDate(DateUtils.getCurrentDateAsString(DateUtils.DATE_FORMAT_WITHOUT_DASH));
        accessLog.setLogId("1");
        accessLog.setLogIp("127.0.0.1");
        accessLog.setLogTime("190202");
        accessLog.setLogType("11");
        accessLog.setLogUrl("/test/test");
        accessLog.setSeq(1l);
        service.registerAccessLog(accessLog);
        return accessLog;
    }

    @Test
    public void testRegisterAccessLog() {
        AccessLog accessLog = registerAccessLog();
        assertNotNull(accessLog);
    }
    
    @Test
    public void testRetrieveAccessLogBySearchCond() {
        registerAccessLog();
        AccessLogSearchCond searchCond = new AccessLogSearchCond();
        Page<AccessLog> resultSet = service.retrieveAccessLogBySearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0 );
    }
    
}
