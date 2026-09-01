/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.RechargingAdjustmentProvider;
import kr.co.kevit.localcsms.recharger.entity.dao.RechargingAdjustmentMapper;
import kr.co.kevit.localcsms.recharger.entity.domain.RechargingAdjustment;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingAdjustmentDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingAdjustmentSearchCond;

/**
 * @since 2026. 9. 1.
 */
@Component
public class RechargingAdjustmentProviderImpl implements RechargingAdjustmentProvider {

    @Autowired
    private RechargingAdjustmentMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerRechargingAdjustment(RechargingAdjustment adjustment) {
        //
        mapper.insertRechargingAdjustment(adjustment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<RechargingAdjustmentDto> retrieveRechargingAdjustmentBySearchCond(RechargingAdjustmentSearchCond searchCond) {
        //
        int totalItemCount = mapper.countRechargingAdjustmentBySearchCond(searchCond);
        Page<RechargingAdjustmentDto> resultSet = new Page<>();
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0)
            return resultSet;

        List<RechargingAdjustmentDto> result = mapper.selectRechargingAdjustmentBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

}
