/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.charger.entity.domain.ChargePoint;
import kr.co.kevit.localcsms.charger.entity.shared.ChargePointSearchCond;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 12.
 */
public class ChargePointServiceTest extends AbstractTestCase{
    
    @Autowired
    private ChargePointService service;
    
    private ChargePoint registerChargePoint() {
        ChargePoint chargePoint = new ChargePoint();
        chargePoint.setCpId("123456780");
        chargePoint.setCpName("1004동");
        chargePoint.setCpLocation("지하2층 B지역");
        chargePoint.setCpUseYn(StringConstants.Y);
//        chargePoint.setDeleteDate(new Date());
        chargePoint.setDeleteYn(StringConstants.N);
        chargePoint.setElectSupplyCapability(57);
        chargePoint.setHighCsCount(1);
        chargePoint.setLowCsCount(2);
        chargePoint.setMemo("TEST");
        Writer writer = new Writer("E00000001");
        chargePoint.setWriter(writer);
        service.registerChargePoint(chargePoint);
        return chargePoint;
    }
    
    @Test
    public void testRegisterChargePoint() {
        ChargePoint chargePoint = registerChargePoint();
        assertNotNull(chargePoint);
    }
    
    @Test
    public void testRetrievetAllChargePoint() {
        //
        registerChargePoint();
        List<ChargePoint> cpList = service.retrievetAllChargePoint();
        assertNotNull(cpList.size() > 0);
    }
    
    @Test
    public void testRetrieveChargePointBySearchCond() {
        ChargePoint chargePoint = registerChargePoint();
        ChargePointSearchCond searchCond = new ChargePointSearchCond();
        searchCond.setCpId(chargePoint.getCpId());
        searchCond.setCpName(chargePoint.getCpName());
        Page<ChargePoint> resultSet = service.retrieveChargePointBySearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }
    
    @Test
    public void testRetrieveChargePointBySpotId() {
        ChargePoint chargePoint = registerChargePoint();
        ChargePoint result = service.retrieveChargePointBySpotId(chargePoint.getCpId());
        assertNotNull(result);
    }
    
    @Test
    public void testModifyChargePoint() {
        ChargePoint chargePoint = registerChargePoint(); 
        service.modifyChargePoint(chargePoint);
        assertNotNull(chargePoint);
    }
    
    @Test
    public void testRemoveChargePoint() {
        ChargePoint chargePoint = registerChargePoint(); 
        service.removeChargePoint(chargePoint);
        assertNotNull(chargePoint);
    }

}
