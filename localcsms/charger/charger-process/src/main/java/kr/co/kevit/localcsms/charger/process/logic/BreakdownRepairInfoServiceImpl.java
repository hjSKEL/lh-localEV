/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.charger.entity.BreakdownMgtInfoProvider;
import kr.co.kevit.localcsms.charger.entity.BreakdownRepairInfoProvider;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownMgtInfo;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownRepairInfo;
import kr.co.kevit.localcsms.charger.process.BreakdownRepairInfoService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
@Service
@Transactional
public class BreakdownRepairInfoServiceImpl implements BreakdownRepairInfoService{
    
    @Autowired
    private BreakdownRepairInfoProvider provder;
    
    @Autowired
    private BreakdownMgtInfoProvider mgtInfoProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBreakdownRepairInfo(BreakdownRepairInfo info) {
        // 
        provder.registerBreakdownRepairInfo(info);
        BreakdownMgtInfo mgtInfo = info.getBreakdownMgtInfo();
        mgtInfo.setWriter(info.getWriter());
        String repairDt = DateUtils.dateToString(info.getWriter().getUpdateDate(), DateUtils.YYYYMMDDHHMMSS);
        mgtInfo.setRepairDate(repairDt.substring(0, 8));
        mgtInfo.setRepairTime(repairDt.substring(8, 14));
        mgtInfoProvider.modifyBreakdownMgtInfo(mgtInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyBreakdownRepairInfo(BreakdownRepairInfo info) {
        // 
        provder.modifyBreakdownRepairInfo(info);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public BreakdownRepairInfo retrieveBreakdownRepairInfo(String id) {
        // 
        return provder.retrieveBreakdownRepairInfo(id);
    }
    


}
