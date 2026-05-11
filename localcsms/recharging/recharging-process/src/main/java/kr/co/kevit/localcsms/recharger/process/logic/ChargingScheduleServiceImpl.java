/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.ChargingScheduleProvider;
import kr.co.kevit.localcsms.recharger.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.recharger.entity.shared.ChargingScheduleSearchCond;
import kr.co.kevit.localcsms.recharger.process.ChargingScheduleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 10. 16.
 */
@Service
@Transactional
public class ChargingScheduleServiceImpl implements ChargingScheduleService {
    
    @Autowired
    private ChargingScheduleProvider provider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChargingSchedule(ChargingSchedule sched) {
        // 
        provider.registerChargingSchedule(sched);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<ChargingSchedule> retrieveChargingScheduleBySearchCond(ChargingScheduleSearchCond searchCond) {
        // 
        return provider.retrieveChargingScheduleBySearchCond(searchCond);
    }

}
