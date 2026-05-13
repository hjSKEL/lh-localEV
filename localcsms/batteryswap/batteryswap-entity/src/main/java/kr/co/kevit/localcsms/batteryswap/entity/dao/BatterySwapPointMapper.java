/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapPoint;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointSearchCond;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Repository
public interface BatterySwapPointMapper {

    int insertBatterySwapPoint(@Param("point") BatterySwapPoint point);

    int updateBatterySwapPoint(@Param("point") BatterySwapPoint point);

    int deleteBatterySwapPoint(@Param("cpId") String cpId);

    BatterySwapPoint selectBatterySwapPoint(@Param("cpId") String cpId);

    int countBatterySwapPointBySearchCond(@Param("searchCond") BatterySwapPointSearchCond searchCond);

    List<BatterySwapPointDto> selectBatterySwapPointBySearchCond(@Param("searchCond") BatterySwapPointSearchCond searchCond);

}
