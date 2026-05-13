/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.batteryswap.entity.BatterySwapStationProvider;
import kr.co.kevit.localcsms.batteryswap.entity.dao.BatterySwapStationMapper;
import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapStation;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Component
public class BatterySwapStationProviderImpl implements BatterySwapStationProvider {

    @Autowired
    private BatterySwapStationMapper mapper;

    @Override
    public void registerBatterySwapStation(BatterySwapStation station) {
        mapper.insertBatterySwapStation(station);
    }

    @Override
    public void modifyBatterySwapStation(BatterySwapStation station) {
        mapper.updateBatterySwapStation(station);
    }

    @Override
    public int removeBatterySwapStation(String cpId, String csId) {
        return mapper.deleteBatterySwapStation(cpId, csId);
    }

    @Override
    public int removeBatterySwapStationByCpId(String cpId) {
        return mapper.deleteBatterySwapStationByCpId(cpId);
    }

    @Override
    public BatterySwapStation retrieveBatterySwapStation(String cpId, String csId) {
        return mapper.selectBatterySwapStation(cpId, csId);
    }

    @Override
    public List<BatterySwapStation> retrieveBatterySwapStationByCpId(String cpId) {
        return mapper.selectBatterySwapStationByCpId(cpId);
    }

    @Override
    public Page<BatterySwapStationDto> retrieveBatterySwapStationBySearchCond(BatterySwapStationSearchCond searchCond) {
        int totCnt = mapper.countBatterySwapStationBySearchCond(searchCond);
        Page<BatterySwapStationDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(totCnt);
        if (totCnt == 0) {
            return resultSet;
        }
        List<BatterySwapStationDto> result = mapper.selectBatterySwapStationBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

}
