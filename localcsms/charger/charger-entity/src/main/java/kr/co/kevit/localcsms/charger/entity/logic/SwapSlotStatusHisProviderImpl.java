/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.charger.entity.SwapSlotStatusHisProvider;
import kr.co.kevit.localcsms.charger.entity.dao.SwapSlotStatusHisMapper;
import kr.co.kevit.localcsms.charger.entity.domain.SwapSlotStatusHis;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusHisDto;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusHisSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Component
public class SwapSlotStatusHisProviderImpl implements SwapSlotStatusHisProvider {

    @Autowired
    private SwapSlotStatusHisMapper mapper;

    @Override
    public void registerSwapSlotStatusHis(SwapSlotStatusHis history) {
        mapper.insertSwapSlotStatusHis(history);
    }

    @Override
    public Page<SwapSlotStatusHisDto> retrieveSwapSlotStatusHisBySearchCond(SwapSlotStatusHisSearchCond searchCond) {
        int totCnt = mapper.countSwapSlotStatusHisBySearchCond(searchCond);
        Page<SwapSlotStatusHisDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<SwapSlotStatusHisDto> result = mapper.selectSwapSlotStatusHisBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

}
