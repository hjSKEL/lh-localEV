/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.smartcharging.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.smartcharging.entity.DerStartStopProvider;
import kr.co.kevit.localcsms.smartcharging.entity.dao.DerStartStopMapper;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerStartStop;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerStartStopDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerStartStopSearchCond;

@Component
public class DerStartStopProviderImpl implements DerStartStopProvider {

    @Autowired
    private DerStartStopMapper mapper;

    @Override
    public void registerStartStop(DerStartStop event) {
        mapper.insertStartStop(event);
    }

    @Override
    public Page<DerStartStopDto> retrieveStartStopBySearchCond(DerStartStopSearchCond cond) {
        int totCnt = mapper.countStartStopBySearchCond(cond);
        Page<DerStartStopDto> resultSet = new Page<>();
        resultSet.setCriteria(cond);
        cond.setTotalItemCount(totCnt);
        if (totCnt == 0) return resultSet;
        List<DerStartStopDto> result = mapper.selectStartStopBySearchCond(cond);
        resultSet.setResult(result);
        return resultSet;
    }
}
