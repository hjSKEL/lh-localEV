/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.logic;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.charger.entity.BreakdownMgtInfoProvider;
import kr.co.kevit.localcsms.charger.entity.dao.BreakdownMgtInfoMapper;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownMgtInfo;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownMgtInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
@Component
public class BreakdownMgtInfoProviderImpl implements BreakdownMgtInfoProvider{
    
    @Autowired
    private BreakdownMgtInfoMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<BreakdownMgtInfoDto> retrieveBreakdownMgtInfoBySearchCond(BreakdownSearchCond searchCond) {
        // 
        Page<BreakdownMgtInfoDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(mapper.countBreakdownMgtInfoBySearchCond(searchCond));
        if(searchCond.getTotalItemCount() > 0) {
            resultSet.setResult(mapper.selectBreakdownMgtInfoBySearchCond(searchCond));
        }else {
            resultSet.setResult(new ArrayList<>(0));
        }
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BreakdownMgtInfoDto retrieveBreakdownMgtInfoById(String id) {
        // 
        return mapper.selectBreakdownMgtInfoById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyBreakdownMgtInfo(BreakdownMgtInfo info) {
        // 
        mapper.updateBreakdownMgtInfo(info);
    }


}
