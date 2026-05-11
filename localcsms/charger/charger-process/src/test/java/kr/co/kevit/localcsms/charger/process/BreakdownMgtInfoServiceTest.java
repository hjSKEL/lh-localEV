/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.charger.entity.domain.BreakdownInfo;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownMgtInfo;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownRepairInfo;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownMgtInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownSearchCond;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
public class BreakdownMgtInfoServiceTest extends AbstractTestCase{
    
    @Autowired
    private BreakdownInfoService bdInfoService;
    
    @Autowired
    private BreakdownMgtInfoService service;
    
    @Autowired
    private BreakdownRepairInfoService bdRpService;
    
    private BreakdownInfo registerBreakdownInfo() {
        //
        BreakdownInfo info = new BreakdownInfo();
        String breakdownContent = "케넥터가 파손되었어요.";
        info.setBreakdownContent(breakdownContent);
        BreakdownMgtInfo mgtInfo = new BreakdownMgtInfo();
        mgtInfo.setBreakdownStatus("BDST01");
        mgtInfo.setCpId("123456");
        mgtInfo.setCsId("01");
        mgtInfo.setReceiptDate("20230704");
        mgtInfo.setReceiptTime("142600");
        mgtInfo.setWriter(new Writer("E00000001"));
        info.setBreakdownMgtInfo(mgtInfo);
        
//        info.setCarModelName(carModelName);
//        info.setCsCatCode(csCatCode);
//        info.setId(id);
//        info.setStationErrorCode(stationErrorCode);
        info.setWriter(new Writer("E00000001"));
        bdInfoService.registerBreakdownInfo(info);
        return info;
        
    }

    @Test
    public void testRegisterBreakdownInfo() {
        BreakdownInfo info = registerBreakdownInfo();
        assertNotNull(info);
    }
    
    @Test
    public void testModifyBreakdownInfo() {
        BreakdownInfo info = registerBreakdownInfo();
        bdInfoService.modifyBreakdownInfo(info);
    }
    
    @Test
    public void testRetrieveBreakdownInfo() {
        BreakdownInfo info = registerBreakdownInfo();
        BreakdownInfo old = bdInfoService.retrieveBreakdownInfo(info.getId());
        assertNotNull(old);
    }
    
    //////////////////////////////////////////////////////////////////
    @Test
    public void testRetrieveBreakdownMgtInfoBySearchCond() {
        //
        BreakdownInfo info = registerBreakdownInfo();
        BreakdownSearchCond searchCond = new BreakdownSearchCond();
        searchCond.setBreakdownStatus(info.getBreakdownMgtInfo().getBreakdownStatus());
        searchCond.setCsId(info.getBreakdownMgtInfo().getCsId());
        searchCond.setCpId(info.getBreakdownMgtInfo().getCpId());
        searchCond.setReceiptDate(info.getBreakdownMgtInfo().getReceiptDate());
        Page<BreakdownMgtInfoDto> resultSet = service.retrieveBreakdownMgtInfoBySearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }
    
    @Test
    public void testRetrieveBreakdownMgtInfoById() {
        //
        BreakdownInfo info = registerBreakdownInfo();
        BreakdownMgtInfo result = service.retrieveBreakdownMgtInfoById(info.getId());
        assertNotNull(result);
    }
    
    //////////////////////////////////////////////////////////////////
    
    private BreakdownRepairInfo registerBreakdownRepairInfo() {
        //
        BreakdownInfo info = registerBreakdownInfo();
        BreakdownRepairInfo repairInfo = new BreakdownRepairInfo();
        repairInfo.setId(info.getId());
        repairInfo.setReason("이걸때문에 고장.");
        repairInfo.setRepairContent("이것저것 수리");
        repairInfo.setRepairNote("ㅅㄷㄴㅅ");
        repairInfo.setRepairCompanyName("KEVT");
        repairInfo.setRepairMblPhoneNo("01012341234");
        repairInfo.setRepairName("홍길동");
        repairInfo.setRepairPosition("대리");
        repairInfo.setWriter(new Writer("E00000001"));
        repairInfo.setBreakdownMgtInfo(info.getBreakdownMgtInfo());
        bdRpService.registerBreakdownRepairInfo(repairInfo);
        return repairInfo;
    }
    
    @Test
    public void testRegisterBreakdownRepairInfo() {
        //
        BreakdownRepairInfo repairInfo = registerBreakdownRepairInfo();
        assertNotNull(repairInfo);
    }
    
    @Test
    public void testModifyBreakdownRepairInfo() {
        //
        BreakdownRepairInfo repairInfo = registerBreakdownRepairInfo();
        bdRpService.modifyBreakdownRepairInfo(repairInfo);
    }
    
    @Test
    public void testRetrieveBreakdownRepairInfo() {
        //
        BreakdownRepairInfo repairInfo = registerBreakdownRepairInfo();
        BreakdownRepairInfo result = bdRpService.retrieveBreakdownRepairInfo(repairInfo.getId());
        assertNotNull(result);
    }
}
