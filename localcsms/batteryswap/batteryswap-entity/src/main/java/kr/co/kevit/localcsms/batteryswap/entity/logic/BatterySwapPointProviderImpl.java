/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.batteryswap.entity.BatterySwapPointProvider;
import kr.co.kevit.localcsms.batteryswap.entity.dao.BatterySwapPointMapper;
import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapPoint;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Component
public class BatterySwapPointProviderImpl implements BatterySwapPointProvider {

    @Autowired
    private BatterySwapPointMapper mapper;

    @Override
    public void registerBatterySwapPoint(BatterySwapPoint point) {
        mapper.insertBatterySwapPoint(point);
    }

    @Override
    public void modifyBatterySwapPoint(BatterySwapPoint point) {
        mapper.updateBatterySwapPoint(point);
    }

    @Override
    public int removeBatterySwapPoint(String cpId) {
        return mapper.deleteBatterySwapPoint(cpId);
    }

    @Override
    public BatterySwapPoint retrieveBatterySwapPoint(String cpId) {
        return mapper.selectBatterySwapPoint(cpId);
    }

    @Override
    public Page<BatterySwapPointDto> retrieveBatterySwapPointBySearchCond(BatterySwapPointSearchCond searchCond) {
        int totCnt = mapper.countBatterySwapPointBySearchCond(searchCond);
        Page<BatterySwapPointDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<BatterySwapPointDto> result = mapper.selectBatterySwapPointBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

}
