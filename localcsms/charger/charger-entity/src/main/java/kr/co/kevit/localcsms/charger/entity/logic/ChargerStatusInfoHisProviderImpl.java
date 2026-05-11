/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.charger.entity.ChargerStatusInfoHisProvider;
import kr.co.kevit.localcsms.charger.entity.dao.ChargerStatusHisMapper;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfoHis;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoHisSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 16.
 */
@Component
public class ChargerStatusInfoHisProviderImpl implements ChargerStatusInfoHisProvider {

    @Autowired
    private ChargerStatusHisMapper mapper;

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Page<ChargerStatusInfoHis> retrieveChargerStatusInfoHisBySearchCond(ChargerStatusInfoHisSearchCond searchCond) {
        //
        Page<ChargerStatusInfoHis> resultSet = new Page<>();
        if(!StringUtils.isEmpty(searchCond.getFromDate())) {            
            searchCond.setFromDate(searchCond.getFromDate() + StringConstants.START_TIME);
        }
        if(!StringUtils.isEmpty(searchCond.getToDate())) {            
            searchCond.setToDate(searchCond.getToDate() + StringConstants.END_TIME);
        }
        int totalItemCount = mapper.countChargerStatusInfoHisBySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) {
            resultSet.setResult(new ArrayList<>(0));
        }else {            
            List<ChargerStatusInfoHis> result = mapper.selectChargerStatusInfoHisBySearchCond(searchCond);
            resultSet.setResult(result);
        }
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChargerStatusHis(ChargerStatusInfo chargerStatusInfo) {
        // 
        mapper.insertChargerStatusHis(chargerStatusInfo);
    }
}
