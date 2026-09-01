/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.RechargingProvider;
import kr.co.kevit.localcsms.recharger.entity.dao.RechargingMapper;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingSearchCond;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 17.
 */
@Component
public class RechargingProviderImpl implements RechargingProvider {

    @Autowired
    private RechargingMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerRecharging(Recharging recharging) {
        //
        mapper.insertRecharging(recharging);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerRechargingError(Recharging recharging) {
        //
        mapper.insertRechargingError(recharging);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyRecharging(Recharging recharging) {
        //
        mapper.updateRecharging(recharging);
    }

    @Override
    public int modifyMaxEnergy(String rechargingId, Double maxEnergy) {
        return mapper.updateMaxEnergy(rechargingId, maxEnergy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Recharging retrieveRechargingById(String rechargingId) {
        //
        return mapper.selectRechargingById(rechargingId);
    }

    @Override
    public RechargingDto retrieveRechargingDtoById(String id) {
        //
        return mapper.selectRechargingDtoById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<RechargingDto> retrieveRechargingByRechargingSearchCond(RechargingSearchCond searchCond) {
        //
        int totalItemCount = mapper.countRechargingByRechargingSearchCond(searchCond);
        Page<RechargingDto> resultSet = new Page<>();
        searchCond.setTotalItemCount(totalItemCount);
        searchCond.setPaySumTotal(totalItemCount > 0 ? mapper.sumPaySumByRechargingSearchCond(searchCond) : 0L);
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0)
            return resultSet;

        List<RechargingDto> result = mapper.selectRechargingByRechargingSearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RechargingDto> retrieveRechargingDtoByIds(List<String> ids) {
        //
        return mapper.selectRechargingDtoByIds(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RechargingDto> retrieveRecharging4DownloadByRechargingSearchCond(RechargingSearchCond searchCond) {
        // 
        return mapper.selectRechargingByRechargingSearchCond(searchCond);
    }
}
