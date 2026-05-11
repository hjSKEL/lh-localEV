/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.charger.entity.BreakdownRepairInfoProvider;
import kr.co.kevit.localcsms.charger.entity.dao.BreakdownRepairInfoMapper;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownRepairInfo;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
@Component
public class BreakdownRepairInfoProviderImpl implements BreakdownRepairInfoProvider{
    
    @Autowired
    private BreakdownRepairInfoMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBreakdownRepairInfo(BreakdownRepairInfo info) {
        // 
        mapper.insertBreakdownRepairInfo(info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyBreakdownRepairInfo(BreakdownRepairInfo info) {
        // 
        mapper.updateBreakdownRepairInfo(info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BreakdownRepairInfo retrieveBreakdownRepairInfo(String id) {
        // 
        return mapper.selectBreakdownRepairInfo(id);
    }
    

}
