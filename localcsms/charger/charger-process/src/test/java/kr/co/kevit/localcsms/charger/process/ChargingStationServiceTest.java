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

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStationCsm;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationSearchCond;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 12.
 */
public class ChargingStationServiceTest extends AbstractTestCase{
    
    @Autowired
    private ChargingStationService service;
    
    private ChargingStation registerChargingStation() {
        ChargingStation chargingStation = new ChargingStation();
        chargingStation.setCpId("123456");
        chargingStation.setCsId("99");
        chargingStation.setCsChanelCount(1);
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
        chargingStation.setOcppVersion(StringConstants.OCPP16);
        chargingStation.setUseYn(StringConstants.Y);
        Writer writer = new Writer("E00000001");
        chargingStation.setWriter(writer);
        service.registerChargingStation(chargingStation);
        return chargingStation;
    }
    
    @Test
    public void testRegisterChargingStation() {
        ChargingStation chargingStation = registerChargingStation();
        assertNotNull(chargingStation);
    }
    
    @Test
    public void testModifyChargingStation() {
        //
        ChargingStation chargingStation = registerChargingStation();
        chargingStation.setCsChanelCount(1);
        chargingStation.setWriter(new Writer("E00000001"));
        service.modifyChargingStation(chargingStation);
    }
    
    @Test
    public void testModifyChargingStationCsm() {
        //
        ChargingStation chargingStation = registerChargingStation();
        ChargingStationCsm csm = new ChargingStationCsm();
        csm.setCpId(chargingStation.getCpId());
        csm.setCsId(chargingStation.getCsId());
        service.modifyChargingStationCsm(csm);
    }
    
    @Test
    public void testRetrieveChargingStationByCpIdNCsId() {
        //
        ChargingStation cs = registerChargingStation();
        ChargingStation result = service.retrieveChargingStationByCpIdNCsId(cs.getCpId(), cs.getCsId());
        assertNotNull(result);
    }
    
    @Test
    public void testRetrieveChargingStationCsmByCpIdNCsId() {
        //
        ChargingStation cs = registerChargingStation();
        ChargingStationCsm result = service.retrieveChargingStationCsmByCpIdNCsId(cs.getCpId(), cs.getCsId());
        assertNotNull(result);
    }
    
    @Test
    public void testRetrieveChargingStationCsmByCpId() {
        //
        ChargingStation cs = registerChargingStation();
        List<ChargingStationCsm> result = service.retrieveChargingStationCsmByCpId(cs.getCpId());
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testRetrieveChargingStationBySearchCond() {
        //
        ChargingStation cs = registerChargingStation();
        ChargingStationSearchCond searchCond = new ChargingStationSearchCond();
        searchCond.setCpId(cs.getCpId());
        searchCond.setCpName("테스트충전소");
        searchCond.setMakerType(cs.getMakerType());
        Page<ChargingStationDto> result = service.retrieveChargingStationBySearchCond(searchCond);
        assertTrue(result.getCriteria().getTotalItemCount() > 0);
    }
}
