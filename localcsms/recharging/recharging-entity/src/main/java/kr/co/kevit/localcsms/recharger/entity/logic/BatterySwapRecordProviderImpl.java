/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.BatterySwapRecordProvider;
import kr.co.kevit.localcsms.recharger.entity.dao.BatterySwapRecordMapper;
import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecord;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordDto;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordSearchCond;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Component
public class BatterySwapRecordProviderImpl implements BatterySwapRecordProvider {

    @Autowired
    private BatterySwapRecordMapper mapper;

    @Override
    public void registerBatterySwapRecord(BatterySwapRecord record) {
        mapper.insertBatterySwapRecord(record);
    }

    @Override
    public void modifyBatterySwapRecord(BatterySwapRecord record) {
        mapper.updateBatterySwapRecord(record);
    }

    @Override
    public BatterySwapRecord retrieveBatterySwapRecord(Long requestId) {
        return mapper.selectBatterySwapRecord(requestId);
    }

    @Override
    public Page<BatterySwapRecordDto> retrieveBatterySwapRecordBySearchCond(BatterySwapRecordSearchCond searchCond) {
        int totCnt = mapper.countBatterySwapRecordBySearchCond(searchCond);
        Page<BatterySwapRecordDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<BatterySwapRecordDto> result = mapper.selectBatterySwapRecordBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

}
