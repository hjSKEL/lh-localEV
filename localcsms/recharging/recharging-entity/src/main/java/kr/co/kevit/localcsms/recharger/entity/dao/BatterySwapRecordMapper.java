/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecord;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordDto;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordSearchCond;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Repository
public interface BatterySwapRecordMapper {

    int insertBatterySwapRecord(@Param("record") BatterySwapRecord record);

    int updateBatterySwapRecord(@Param("record") BatterySwapRecord record);

    BatterySwapRecord selectBatterySwapRecord(@Param("requestId") Long requestId);

    int countBatterySwapRecordBySearchCond(@Param("searchCond") BatterySwapRecordSearchCond searchCond);

    List<BatterySwapRecordDto> selectBatterySwapRecordBySearchCond(@Param("searchCond") BatterySwapRecordSearchCond searchCond);

}
