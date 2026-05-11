/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfoHis;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoHisSearchCond;
import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 12.
 */
public class ChargerStatusInfoHisServiceTest extends AbstractTestCase{
    
    @Autowired
    private ChargerStatusInfoHisService service;

    @Test
    public void testRetrieveChargerStatusInfoHisBySearchCond() {
        //
        ChargerStatusInfoHisSearchCond searchCond = new ChargerStatusInfoHisSearchCond();
        searchCond.setFromDate("00010101");
        searchCond.setToDate("99991231");
        Page<ChargerStatusInfoHis> resultSet = service.retrieveChargerStatusInfoHisBySearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }
}
