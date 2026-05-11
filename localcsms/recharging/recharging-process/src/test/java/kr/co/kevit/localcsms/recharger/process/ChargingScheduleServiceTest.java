/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.recharger.entity.shared.ChargingScheduleSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 10. 16.
 */
public class ChargingScheduleServiceTest extends AbstractTestCase{
    
    @Autowired
    private ChargingScheduleService service;

    private ChargingSchedule registerChargingSchedule() {
        ChargingSchedule sched = new ChargingSchedule();
        sched.setCsUniqId("123456789-12");
        sched.setStartTime("20231016152300");
        sched.setEndTime(  "20231016152400");
        sched.setDuration("60");
        sched.setLimitKW(5);
        service.registerChargingSchedule(sched);
        return sched;
    }
    
    @Test
    public void testRetrieveChargingScheduleBySearchCond() {
        ChargingSchedule sched = registerChargingSchedule();
        ChargingScheduleSearchCond searchCond = new ChargingScheduleSearchCond();
        searchCond.setCsUniqId(sched.getCsUniqId());
        Page<ChargingSchedule> resultSet = service.retrieveChargingScheduleBySearchCond(searchCond);
        assertTrue(!resultSet.getResult().isEmpty());
    }
}
