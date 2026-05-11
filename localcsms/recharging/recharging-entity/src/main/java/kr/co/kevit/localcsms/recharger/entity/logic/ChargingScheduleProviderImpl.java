/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.logic;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.ChargingScheduleProvider;
import kr.co.kevit.localcsms.recharger.entity.dao.ChargingScheduleMapper;
import kr.co.kevit.localcsms.recharger.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.recharger.entity.shared.ChargingScheduleSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 10. 16.
 */
@Component
public class ChargingScheduleProviderImpl implements ChargingScheduleProvider{
    
    @Autowired
    private ChargingScheduleMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChargingSchedule(ChargingSchedule sched) {
        // 
        mapper.insertChargingSchedule(sched);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ChargingSchedule> retrieveChargingScheduleBySearchCond(ChargingScheduleSearchCond searchCond) {
        // 
        Page<ChargingSchedule> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(mapper.countChargingScheduleBySearchCond(searchCond));
        if(searchCond.getTotalItemCount() > 0) {
            resultSet.setResult(mapper.selectChargingScheduleBySearchCond(searchCond));
        }else {
            resultSet.setResult(new ArrayList<>(0));
        }
        return resultSet;
    }

}
