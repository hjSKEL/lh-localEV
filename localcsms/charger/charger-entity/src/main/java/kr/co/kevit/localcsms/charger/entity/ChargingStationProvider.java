/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity;

import java.util.List;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStationCsm;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 30.
 */
public interface ChargingStationProvider {

    /**
     * IF 서비스 용
     * 
     * @param cpId
     * @param csId
     * @return
     */
    ChargingStation retrieveChargingStationByCpIdNCsId(String cpId, String csId);

    void registerChargingStation(ChargingStation chargingStation);

    void modifyChargingStation(ChargingStation chargingStation);

    void modifyChargingStationCsm(ChargingStationCsm chargingStation);
    
    void removeChargingStation(String cpId, String csId);

    ChargingStationCsm retrieveChargingStationCsmByCpIdNCsId(String cpId, String csId);
    
    ChargingStationDto retrieveChargingStationDtoByCpIdNCsId(String cpId, String csId);

    List<ChargingStationCsm> retrieveChargingStationCsmByCpId(String cpId);

    Page<ChargingStationDto> retrieveChargingStationBySearchCond(ChargingStationSearchCond searchCond);


}