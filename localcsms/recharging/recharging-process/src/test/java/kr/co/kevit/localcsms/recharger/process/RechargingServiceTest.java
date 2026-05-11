/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 15.
 */
public class RechargingServiceTest extends AbstractTestCase {
    
    @Autowired
    private RechargingService service;
    
    private Recharging registerRecharging() {
        Recharging recharging = new Recharging();
        //recharging.setRechargingId();
        recharging.setCpId("123456");
        recharging.setCsId("01");
        recharging.setEvseId(1);
        recharging.setChStartDate(new Date());
        recharging.setChEndDate(new Date());
        recharging.setChStatCode("RECS02");
        recharging.setChUseAmount(BigDecimal.ZERO);
        recharging.setChUseCost(BigDecimal.ZERO);
        recharging.setChUseUnitCost(BigDecimal.ZERO);
        recharging.setClosedDate(DateUtils.getCurrentDateAsString(DateUtils.DATE_FORMAT_WITHOUT_DASH));
        recharging.setCompanyId("CO0000001");
        recharging.setCustomerId("C00000001");
        recharging.setCutCardNo("1234123412341234");
        recharging.setEndCaEleEnerge(BigDecimal.ZERO);
        recharging.setStartCaEleEnerge(BigDecimal.ZERO);
        recharging.setProductId("PDLO001-001");
        recharging.setFinalPaySum(0);
        recharging.setPaySum(0);
        service.registerRecharging(recharging);
        return recharging;
    }
    
    @Test
    public void test() {
        String rcId = "1234560120231231235959";
        String id = rcId.substring(12);
        System.out.println(id);
        System.out.println(Integer.parseInt(id));
        System.out.println(Integer.MAX_VALUE);
    }
    
    @Test
    public void testRegisterRecharging() {
        Recharging recharging = registerRecharging();
        assertNotNull(recharging);
    }
    
    @Test
    public void testModifyRecharging() {
        Recharging recharging = registerRecharging();
        service.modifyRecharging(recharging);
    }
    
    @Test
    public void testRetrieveRecharging4IfById() {
        Recharging old = registerRecharging();
        Recharging recharging = service.retrieveRecharging4IfById(old.getRechargingId());
        assertNotNull(recharging);
    }
    
    @Test
    public void testCompleteRecharging() {
        Recharging old = registerRecharging();
        old.setErrorContent("ERROR OCUP");
        service.completeRecharging(old);
    }
    
    @Test
    public void testRetrieveRechargingById() {
        Recharging old = registerRecharging();
        RechargingDto recharging = service.retrieveRechargingById(old.getRechargingId());
        assertNotNull(recharging);
    }
    
    @Test
    public void testRetrieveRechargingByRechargingSearchCond() {
        Recharging old = registerRecharging();
        RechargingSearchCond searchCond = new RechargingSearchCond();
        searchCond.setCompanyId(old.getCompanyId());
        searchCond.setCpId(old.getCpId());
//        searchCond.setCpIds(cpIds);
//        searchCond.setCpName(cpName);
        searchCond.setCsId(old.getCsId());
        searchCond.setCustomerId(old.getCustomerId());
        searchCond.setCutCardNo(old.getCutCardNo());
//        searchCond.setFromDate(fromDate);
//        searchCond.setStatus(status);
//        searchCond.setToDate(toDate);
        searchCond.setDateOrder("A");
        searchCond.setDateType("S");
        Page<RechargingDto> resultSet = service.retrieveRechargingByRechargingSearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }
    
    @Test
    public void testRetrieveRechargingDtoByIds() {
        Recharging old = registerRecharging();
        List<String> ids = new ArrayList<>(1);
        ids.add(old.getRechargingId());
        List<RechargingDto> resultSet = service.retrieveRechargingDtoByIds(ids);
        assertTrue(!resultSet.isEmpty());
    }
    
}
