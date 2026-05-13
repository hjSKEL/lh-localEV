/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.process.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.batteryswap.entity.BatterySwapPointProvider;
import kr.co.kevit.localcsms.batteryswap.entity.BatterySwapStationProvider;
import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapPoint;
import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapStation;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointSearchCond;
import kr.co.kevit.localcsms.batteryswap.process.BatterySwapPointService;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Service
@Transactional
public class BatterySwapPointServiceImpl implements BatterySwapPointService {

    @Autowired
    private BatterySwapPointProvider pointProvider;

    @Autowired
    private BatterySwapStationProvider stationProvider;

    @Override
    public void registerBatterySwapPoint(BatterySwapPoint point) {
        if (point.getCpId() == null || point.getCpId().isEmpty()) {
            throw new KEVITException("충전소ID가 비어 있습니다.");
        }
        if (pointProvider.retrieveBatterySwapPoint(point.getCpId()) != null) {
            throw new KEVITException("이미 등록된 충전소ID 입니다.");
        }
        pointProvider.registerBatterySwapPoint(point);
    }

    @Override
    public void modifyBatterySwapPoint(BatterySwapPoint point) {
        if (point.getCpId() == null || point.getCpId().isEmpty()) {
            throw new KEVITException("충전소ID가 비어 있습니다.");
        }
        if (pointProvider.retrieveBatterySwapPoint(point.getCpId()) == null) {
            throw new KEVITException("등록되지 않은 충전소 입니다.");
        }
        pointProvider.modifyBatterySwapPoint(point);
    }

    @Override
    public void removeBatterySwapPoint(String cpId) {
        if (pointProvider.retrieveBatterySwapPoint(cpId) == null) {
            throw new KEVITException("등록되지 않은 충전소 입니다.");
        }
        stationProvider.removeBatterySwapStationByCpId(cpId);
        pointProvider.removeBatterySwapPoint(cpId);
    }

    @Transactional(readOnly = true)
    @Override
    public BatterySwapPoint retrieveBatterySwapPoint(String cpId) {
        BatterySwapPoint point = pointProvider.retrieveBatterySwapPoint(cpId);
        if (point != null) {
            List<BatterySwapStation> stations = stationProvider.retrieveBatterySwapStationByCpId(cpId);
            point.setStations(stations);
        }
        return point;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BatterySwapPointDto> retrieveBatterySwapPointBySearchCond(BatterySwapPointSearchCond searchCond) {
        return pointProvider.retrieveBatterySwapPointBySearchCond(searchCond);
    }

}
