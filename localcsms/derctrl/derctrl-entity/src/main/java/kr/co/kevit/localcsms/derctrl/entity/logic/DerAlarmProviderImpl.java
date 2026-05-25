/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.derctrl.entity.DerAlarmProvider;
import kr.co.kevit.localcsms.derctrl.entity.dao.DerAlarmMapper;
import kr.co.kevit.localcsms.derctrl.entity.domain.DerAlarm;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerAlarmDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerAlarmSearchCond;

@Component
public class DerAlarmProviderImpl implements DerAlarmProvider {

    @Autowired
    private DerAlarmMapper mapper;

    @Override
    public void registerAlarm(DerAlarm alarm) {
        mapper.insertAlarm(alarm);
    }

    @Override
    public Page<DerAlarmDto> retrieveAlarmBySearchCond(DerAlarmSearchCond cond) {
        int totCnt = mapper.countAlarmBySearchCond(cond);
        Page<DerAlarmDto> resultSet = new Page<>();
        resultSet.setCriteria(cond);
        cond.setTotalItemCount(totCnt);
        if (totCnt == 0) return resultSet;
        List<DerAlarmDto> result = mapper.selectAlarmBySearchCond(cond);
        resultSet.setResult(result);
        return resultSet;
    }
}
