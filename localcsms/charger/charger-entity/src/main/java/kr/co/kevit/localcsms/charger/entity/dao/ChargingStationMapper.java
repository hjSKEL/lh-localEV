/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStationCsm;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationSearchCond;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 30.
 */
@Repository
public interface ChargingStationMapper {
    /**
     * IF 용
     * @param cpId
     * @param csId
     * @return
     */
    ChargingStation selectChargingStationByCpIdNCsId(@Param("cpId") String cpId, @Param("csId") String csId);
    
    int insertChargingStation(@Param("chargingStation") ChargingStation chargingStation);

    int updateChargingStation(@Param("chargingStation") ChargingStation chargingStation);

    int insertChargingStationCsm(@Param("chargingStationCsm") ChargingStationCsm chargingStation);

    int updateChargingStationCsm(@Param("chargingStationCsm") ChargingStationCsm chargingStation);
    
    int deleteChargingStation(@Param("cpId") String cpId, @Param("csId") String csId);
    
    int deleteChargingStationCsm(@Param("cpId") String cpId, @Param("csId") String csId);

    ChargingStationCsm selectChargingStationCsmByCpIdNCsId(@Param("cpId") String cpId, @Param("csId") String csId);
    
    ChargingStationDto selectChargingStationDtoByCpIdNCsId(@Param("cpId") String cpId, @Param("csId") String csId);

    List<ChargingStationCsm> selectChargingStationCsmByCpId(@Param("cpId") String cpId);

    int countChargingStationBySearchCond(@Param("searchCond") ChargingStationSearchCond searchCond);

    List<ChargingStationDto> selectChargingStationBySearchCond(@Param("searchCond") ChargingStationSearchCond searchCond);
}
