/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.DischargingProvider;
import kr.co.kevit.localcsms.recharger.entity.dao.DischargingMapper;
import kr.co.kevit.localcsms.recharger.entity.domain.Discharging;
import kr.co.kevit.localcsms.recharger.entity.shared.DischargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.DischargingSearchCond;

@Component
public class DischargingProviderImpl implements DischargingProvider {

    @Autowired
    private DischargingMapper mapper;

    @Override
    public void registerDischarging(Discharging discharging) {
        mapper.insertDischarging(discharging);
    }

    @Override
    public void modifyDischarging(Discharging discharging) {
        mapper.updateDischarging(discharging);
    }

    @Override
    public Discharging retrieveDischargingById(String dcId) {
        return mapper.selectDischargingById(dcId);
    }

    @Override
    public DischargingDto retrieveDischargingDtoById(String dcId) {
        return mapper.selectDischargingDtoById(dcId);
    }

    @Override
    public Page<DischargingDto> retrieveDischargingBySearchCond(DischargingSearchCond searchCond) {
        int totalItemCount = mapper.countBySearchCond(searchCond);
        Page<DischargingDto> resultSet = new Page<>();
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) return resultSet;
        resultSet.setResult(mapper.selectBySearchCond(searchCond));
        return resultSet;
    }

    @Override
    public List<DischargingDto> retrieveDischargingByEvccId(String evccId) {
        return mapper.selectByEvccId(evccId);
    }
}
