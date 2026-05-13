/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapStation;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationSearchCond;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Repository
public interface BatterySwapStationMapper {

    int insertBatterySwapStation(@Param("station") BatterySwapStation station);

    int updateBatterySwapStation(@Param("station") BatterySwapStation station);

    int deleteBatterySwapStation(@Param("cpId") String cpId, @Param("csId") String csId);

    int deleteBatterySwapStationByCpId(@Param("cpId") String cpId);

    BatterySwapStation selectBatterySwapStation(@Param("cpId") String cpId, @Param("csId") String csId);

    /** 단일 충전소(cpId) 내의 모든 교환충전기 */
    List<BatterySwapStation> selectBatterySwapStationByCpId(@Param("cpId") String cpId);

    int countBatterySwapStationBySearchCond(@Param("searchCond") BatterySwapStationSearchCond searchCond);

    List<BatterySwapStationDto> selectBatterySwapStationBySearchCond(@Param("searchCond") BatterySwapStationSearchCond searchCond);

}
