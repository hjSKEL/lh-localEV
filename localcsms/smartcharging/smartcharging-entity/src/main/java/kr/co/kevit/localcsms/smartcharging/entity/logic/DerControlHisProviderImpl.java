/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.smartcharging.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.smartcharging.entity.DerControlHisProvider;
import kr.co.kevit.localcsms.smartcharging.entity.dao.DerControlHisMapper;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerControlHis;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlHisDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlHisSearchCond;

@Component
public class DerControlHisProviderImpl implements DerControlHisProvider {

    @Autowired
    private DerControlHisMapper mapper;

    @Override
    public void registerHis(DerControlHis his) {
        mapper.insertHis(his);
    }

    @Override
    public Page<DerControlHisDto> retrieveHisBySearchCond(DerControlHisSearchCond cond) {
        int totCnt = mapper.countHisBySearchCond(cond);
        Page<DerControlHisDto> resultSet = new Page<>();
        resultSet.setCriteria(cond);
        cond.setTotalItemCount(totCnt);
        if (totCnt == 0) return resultSet;
        List<DerControlHisDto> result = mapper.selectHisBySearchCond(cond);
        resultSet.setResult(result);
        return resultSet;
    }
}
