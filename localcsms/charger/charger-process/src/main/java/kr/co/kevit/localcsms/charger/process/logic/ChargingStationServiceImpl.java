/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.charger.entity.ChargePointProvider;
import kr.co.kevit.localcsms.charger.entity.ChargerStatusProvider;
import kr.co.kevit.localcsms.charger.entity.ChargingStationProvider;
import kr.co.kevit.localcsms.charger.entity.domain.ChargePoint;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStationCsm;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.exception.KEVITErrorCode;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 12.
 */
@Service
@Transactional
public class ChargingStationServiceImpl implements ChargingStationService {
    
    @Autowired
    private ChargingStationProvider provider;

    @Autowired
    private ChargerStatusProvider statusProvider;
    
    @Autowired
    private ChargePointProvider chargePointProvider;
    
    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ChargingStation retrieveChargingStationByCpIdNCsId(String cpId, String csId) {
        // 
        return provider.retrieveChargingStationByCpIdNCsId(cpId, csId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChargingStation(ChargingStation chargingStation) {
        // 
    	ChargePoint chargePoint = chargePointProvider.retrieveChargePointByCpId(chargingStation.getCpId());
    	int tempHiCsCnt = chargePoint.getHighCsCount();
    	int tempLowCsCnt = chargePoint.getLowCsCount();
    	int tempElecSupCap = chargePoint.getElectSupplyCapability();
    	if(chargingStation.getCsKindType().equals("CHKT01")) {
    	    chargePoint.setLowCsCount(tempLowCsCnt + 1);
    	}
    	if(chargingStation.getCsKindType().equals("CHKT02")) {
    	    chargePoint.setHighCsCount(tempHiCsCnt + 1);
    	}
    	chargePoint.setElectSupplyCapability(tempElecSupCap + chargingStation.getElectSupplyCapability());
        provider.registerChargingStation(chargingStation);
        chargePointProvider.modifyChargePoint(chargePoint);
        for(int i = 1; i <= chargingStation.getCsChanelCount() ; ++i) {            
            ChargerStatusInfo chargerStatusInfo = new ChargerStatusInfo();
            chargerStatusInfo.setCpId(chargingStation.getCpId());
            chargerStatusInfo.setCsId(chargingStation.getCsId());
            chargerStatusInfo.setEvseId(i);
            statusProvider.registerChargerStatus(chargerStatusInfo);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyChargingStation(ChargingStation chargingStation) {
        // 
        if(StringConstants.Y.equals(chargingStation.getUseYn())) {
            ChargePoint cp = chargePointProvider.retrieveChargePointByCpId(chargingStation.getCpId());
            if(StringConstants.N.equals(cp.getCpUseYn())) {
                throw new KEVITException("충전소 미사용. 충전기 사용으로 등록 불가");
            }
        }
        provider.modifyChargingStation(chargingStation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyChargingStationCsm(ChargingStationCsm chargingStation) {
        // 
        provider.modifyChargingStationCsm(chargingStation);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ChargingStationCsm retrieveChargingStationCsmByCpIdNCsId(String cpId, String csId) {
        // 
        return provider.retrieveChargingStationCsmByCpIdNCsId(cpId, csId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<ChargingStationCsm> retrieveChargingStationCsmByCpId(String cpId) {
        // 
        return provider.retrieveChargingStationCsmByCpId(cpId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<ChargingStationDto> retrieveChargingStationBySearchCond(ChargingStationSearchCond searchCond) {
        // 
        return provider.retrieveChargingStationBySearchCond(searchCond);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public String retrieveNextCsIdByCpId(String cpId) {
        // 
        List<ChargingStationCsm> csList = provider.retrieveChargingStationCsmByCpId(cpId);
        if(csList.isEmpty()) {
            return "01";
        }else {            
            ChargingStationCsm lastCs = csList.get(csList.size() -1);
            int csId = Integer.parseInt(lastCs.getCsId()) + 1;
            if(csId > 9) {
                return Integer.toString(csId);
            }else {
                return "0" + Integer.toString(csId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ChargingStationDto retrieveChargingStationDtoByCpIdNCsId(String cpId, String csId) {
        // 
        return provider.retrieveChargingStationDtoByCpIdNCsId(cpId, csId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeChargingStation(String cpId, String csId, String employeeId) {
        // 
        ChargingStation cs = provider.retrieveChargingStationByCpIdNCsId(cpId, csId);
        if(cs == null) {
            throw new KEVITException(KEVITErrorCode.INTERNAL_ERR.getCode(), "삭제할 충전기가 없습니다.");
        }
        provider.removeChargingStation(cpId, csId);
        statusProvider.removeChargerStatusByCpIdNCsId(cpId, csId);
        ChargePoint chargePoint = chargePointProvider.retrieveChargePointByCpId(cpId);
        if("CHKT02".equals(cs.getCsKindType())) { // 급속
            chargePoint.setHighCsCount(chargePoint.getHighCsCount() - 1);
        }
        if("CHKT01".equals(cs.getCsKindType())) { // 완속
            chargePoint.setLowCsCount(chargePoint.getLowCsCount() - 1);
        }
        chargePoint.setElectSupplyCapability(chargePoint.getElectSupplyCapability() - cs.getElectSupplyCapability());
        chargePoint.setWriter(new Writer(employeeId));
        chargePointProvider.modifyChargePoint(chargePoint);
    }



}
