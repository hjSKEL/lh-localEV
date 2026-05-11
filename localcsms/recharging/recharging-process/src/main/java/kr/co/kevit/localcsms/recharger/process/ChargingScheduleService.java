/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.recharger.entity.shared.ChargingScheduleSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 10. 16.
 */
public interface ChargingScheduleService {
    
    void registerChargingSchedule(ChargingSchedule sched);
    
    Page<ChargingSchedule> retrieveChargingScheduleBySearchCond(ChargingScheduleSearchCond searchCond);
}
