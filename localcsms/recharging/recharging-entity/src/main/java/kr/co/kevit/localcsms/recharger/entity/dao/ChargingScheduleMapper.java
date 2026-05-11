/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.recharger.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.recharger.entity.shared.ChargingScheduleSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 10. 16.
 */
@Repository
public interface ChargingScheduleMapper {
    
    int insertChargingSchedule(@Param("sched")ChargingSchedule sched);
    
    int countChargingScheduleBySearchCond(@Param("searchCond")ChargingScheduleSearchCond searchCond);
    
    List<ChargingSchedule> selectChargingScheduleBySearchCond(@Param("searchCond")ChargingScheduleSearchCond searchCond);

}
