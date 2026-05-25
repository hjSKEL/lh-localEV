/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.TariffHisProvider;
import kr.co.kevit.localcsms.payment.entity.dao.TariffHisMapper;
import kr.co.kevit.localcsms.payment.entity.domain.TariffHis;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisSearchCond;

@Component
public class TariffHisProviderImpl implements TariffHisProvider {

    @Autowired
    private TariffHisMapper mapper;

    @Override
    public void registerTariffHis(TariffHis his) {
        mapper.insertTariffHis(his);
    }

    @Override
    public Page<TariffHisDto> retrieveTariffHisBySearchCond(TariffHisSearchCond searchCond) {
        int totCnt = mapper.countTariffHisBySearchCond(searchCond);
        Page<TariffHisDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<TariffHisDto> result = mapper.selectTariffHisBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }
}
