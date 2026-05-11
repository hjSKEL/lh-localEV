/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.charger.entity.ChargePointProvider;
import kr.co.kevit.localcsms.charger.entity.ChargingStationProvider;
import kr.co.kevit.localcsms.charger.entity.domain.ChargePoint;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStationCsm;
import kr.co.kevit.localcsms.charger.entity.shared.ChargePointSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargePointService;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 10. 29.
 */
@Service
@Transactional
public class ChargePointServiceImpl implements ChargePointService {

    @Autowired
    private ChargePointProvider provider;
    
    @Autowired
    private ChargingStationProvider csProvider;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<ChargePoint> retrievetAllChargePoint() {
        //
        return provider.retrievetAllChargePoint();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<ChargePoint> retrieveChargePointBySearchCond(ChargePointSearchCond searchCond) {
        //
        return provider.retrieveChargePointBySearchCond(searchCond);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ChargePoint retrieveChargePointBySpotId(String cpId) {
        //
        return provider.retrieveChargePointByCpId(cpId);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void registerChargePoint(ChargePoint chargePoint) {
        //
        provider.registerChargePoint(chargePoint);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void modifyChargePoint(ChargePoint chargePoint) {
        //
        provider.modifyChargePoint(chargePoint);
        if(StringConstants.N.equals(chargePoint.getCpUseYn())) {            
            List<ChargingStationCsm> csList = csProvider.retrieveChargingStationCsmByCpId(chargePoint.getCpId());
            for(ChargingStationCsm chargingStation : csList) {
                chargingStation.setUseYn(StringConstants.N);
                csProvider.modifyChargingStation(chargingStation);
            }
        }
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void removeChargePoint(ChargePoint chargePoint) {
        //
        provider.removeChargePoint(chargePoint);
    }

}
