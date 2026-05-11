/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusSearchCond;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 12.
 */
public class ChargerStatusServiceTest extends AbstractTestCase{
    
    @Autowired
    private ChargerStatusService service;
    
    @Autowired
    private ChargingStationService csService;
    
    private ChargingStation registerChargingStation() {
        ChargingStation chargingStation = new ChargingStation();
        chargingStation.setCpId("123456");
        chargingStation.setCsId("02");
        chargingStation.setCsChanelCount(2);
        chargingStation.setBrkdownYn(StringConstants.N);
        chargingStation.setCsCatCode("CHRS08");
        chargingStation.setCsInstallCo("KEVIT");
        chargingStation.setCsKindType("CHKT01");
        chargingStation.setCsPassword("1234567890123456");
        chargingStation.setCsUniqId(chargingStation.getCpId()+chargingStation.getCsId());
        chargingStation.setElectSupplyCapability(50);
        chargingStation.setInsYearMon("202306");
        chargingStation.setMakerType("KE");
        chargingStation.setProdType("PDHO01");
        chargingStation.setUseYn(StringConstants.Y);
        Writer writer = new Writer("E00000001");
        chargingStation.setWriter(writer);
        csService.registerChargingStation(chargingStation);
        return chargingStation;
    }
    @Test
    public void testRetrieveChargerStatusByCpIdNCsId() {
        //
        ChargingStation csInfo = registerChargingStation();
        List<ChargerStatusInfo> csStatusInfos = service.retrieveChargerStatusByCpIdNCsId(csInfo.getCpId(), csInfo.getCsId());
        assertTrue(csStatusInfos.size()>0);
    }
    
    @Test
    public void testModifyChargerStatus() {
        //
        ChargingStation csInfo = registerChargingStation();
        List<ChargerStatusInfo> csStatusInfos = service.retrieveChargerStatusByCpIdNCsId(csInfo.getCpId(), csInfo.getCsId());
        for(ChargerStatusInfo csStatusInfo : csStatusInfos) {            
            service.modifyChargerStatus(csStatusInfo);
        }
    }
    
    @Test
    public void testModifyChargerStatusWithoutHis() {
        ChargingStation csInfo = registerChargingStation();
        List<ChargerStatusInfo> csStatusInfos = service.retrieveChargerStatusByCpIdNCsId(csInfo.getCpId(), csInfo.getCsId());
        for(ChargerStatusInfo csStatusInfo : csStatusInfos) {            
            service.modifyChargerStatusWithoutHis(csStatusInfo);
        }
    }
    
    @Test
    public void testRetrieveChargerStatusBySearchCond() {
        ChargingStation csInfo = registerChargingStation();
        ChargerStatusSearchCond searchCond = new ChargerStatusSearchCond();
        searchCond.setCpId(csInfo.getCpId());
        searchCond.setCsId(csInfo.getCsId());
        searchCond.setMakerType(csInfo.getMakerType());
//        List<String> status = new ArrayList<>();
//        status.add("");
//        searchCond.setStatus(status);
        Page<ChargerStatusInfoDto> resultSet = service.retrieveChargerStatusBySearchCond(searchCond);
        assertTrue(resultSet.getCriteria().getTotalItemCount() > 0);
    }
}
