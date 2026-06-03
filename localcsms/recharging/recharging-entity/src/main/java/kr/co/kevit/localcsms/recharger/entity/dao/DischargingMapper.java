/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.recharger.entity.domain.Discharging;
import kr.co.kevit.localcsms.recharger.entity.shared.DischargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.DischargingSearchCond;

@Repository
public interface DischargingMapper {

    int insertDischarging(@Param("discharging") Discharging discharging);

    int updateDischarging(@Param("discharging") Discharging discharging);

    Discharging selectDischargingById(@Param("dcId") String dcId);

    DischargingDto selectDischargingDtoById(@Param("dcId") String dcId);

    int countBySearchCond(@Param("searchCond") DischargingSearchCond searchCond);

    List<DischargingDto> selectBySearchCond(@Param("searchCond") DischargingSearchCond searchCond);

    List<DischargingDto> selectByEvccId(@Param("evccId") String evccId);
}
