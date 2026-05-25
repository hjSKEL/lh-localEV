/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.logic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.TariffProvider;
import kr.co.kevit.localcsms.payment.entity.dao.TariffMapper;
import kr.co.kevit.localcsms.payment.entity.domain.Tariff;
import kr.co.kevit.localcsms.payment.entity.shared.TariffDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffSearchCond;

@Component
public class TariffProviderImpl implements TariffProvider {

    @Autowired
    private TariffMapper mapper;

    @Override
    public void registerTariff(Tariff tariff) {
        mapper.insertTariff(tariff);
    }

    @Override
    public int modifyTariffStatus(String tariffId, String statusCd, Date validTo, String updUserId) {
        return mapper.updateTariffStatus(tariffId, statusCd, validTo, updUserId);
    }

    @Override
    public Tariff retrieveTariff(String tariffId) {
        return mapper.selectTariff(tariffId);
    }

    @Override
    public Page<TariffDto> retrieveTariffBySearchCond(TariffSearchCond searchCond) {
        int totCnt = mapper.countTariffBySearchCond(searchCond);
        Page<TariffDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<TariffDto> result = mapper.selectTariffBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

    @Override
    public int retrieveDailySequence(String tariffKind, String yyyymmdd) {
        Integer next = mapper.selectDailySequence(tariffKind, yyyymmdd);
        return next == null ? 1 : (next + 1);
    }
}
