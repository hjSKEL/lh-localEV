/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.process.logic;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.batteryswap.entity.BatterySwapPointProvider;
import kr.co.kevit.localcsms.batteryswap.entity.BatterySwapStationProvider;
import kr.co.kevit.localcsms.batteryswap.entity.SwapSlotStatusProvider;
import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapStation;
import kr.co.kevit.localcsms.batteryswap.entity.domain.SwapSlotStatus;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationSearchCond;
import kr.co.kevit.localcsms.batteryswap.process.BatterySwapStationService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Service
@Transactional
public class BatterySwapStationServiceImpl implements BatterySwapStationService {

    /** 슬롯 초기상태 (BSSS00) */
    private static final String SLOT_STATE_EMPTY = "BSSS00";

    @Autowired
    private BatterySwapPointProvider pointProvider;

    @Autowired
    private BatterySwapStationProvider stationProvider;

    @Autowired
    private SwapSlotStatusProvider slotProvider;

    @Override
    public void registerBatterySwapStation(BatterySwapStation station) {
        if (station.getCpId() == null || station.getCpId().isEmpty()) {
            throw new KEVITException("충전소ID가 비어 있습니다.");
        }
        if (station.getCsId() == null || station.getCsId().isEmpty()) {
            throw new KEVITException("충전기ID가 비어 있습니다.");
        }
        if (pointProvider.retrieveBatterySwapPoint(station.getCpId()) == null) {
            throw new KEVITException("등록되지 않은 충전소 입니다. CP_ID=" + station.getCpId());
        }
        if (stationProvider.retrieveBatterySwapStation(station.getCpId(), station.getCsId()) != null) {
            throw new KEVITException("이미 등록된 교환충전기 입니다.");
        }
        stationProvider.registerBatterySwapStation(station);

        initializeSlots(station);
    }

    /**
     * 충전기 신규 등록 직후 totalSlotCount 만큼의 슬롯(evseId=1..N)을
     * EMPTY 상태로 채워 넣는다. 같은 트랜잭션 내에서 수행되므로 station insert 와
     * 슬롯 insert 가 원자적으로 커밋된다.
     */
    private void initializeSlots(BatterySwapStation station) {
        int total = station.getTotalSlotCount();
        if (total <= 0) {
            return;
        }
        Date now = new Date();
        Writer baseWriter = station.getWriter();
        for (int evseId = 1; evseId <= total; evseId++) {
            SwapSlotStatus slot = new SwapSlotStatus();
            slot.setCpId(station.getCpId());
            slot.setCsId(station.getCsId());
            slot.setEvseId(evseId);
            slot.setSlotState(SLOT_STATE_EMPTY);
            slot.setLastEventDate(now);
            slot.setWriter(copyWriter(baseWriter, now));
            slotProvider.registerSwapSlotStatus(slot);
        }
    }

    private Writer copyWriter(Writer src, Date now) {
        String userId = (src == null) ? null : src.getRegUserId();
        Writer w = new Writer(userId);
        w.setRegistrationDate(now);
        w.setUpdateDate(now);
        return w;
    }

    @Override
    public void modifyBatterySwapStation(BatterySwapStation station) {
        if (station.getCpId() == null || station.getCpId().isEmpty() ||
            station.getCsId() == null || station.getCsId().isEmpty()) {
            throw new KEVITException("충전소ID/충전기ID 가 비어 있습니다.");
        }
        if (stationProvider.retrieveBatterySwapStation(station.getCpId(), station.getCsId()) == null) {
            throw new KEVITException("등록되지 않은 교환충전기 입니다.");
        }
        stationProvider.modifyBatterySwapStation(station);
    }

    @Override
    public void removeBatterySwapStation(String cpId, String csId) {
        if (stationProvider.retrieveBatterySwapStation(cpId, csId) == null) {
            throw new KEVITException("등록되지 않은 교환충전기 입니다.");
        }
        stationProvider.removeBatterySwapStation(cpId, csId);
    }

    @Transactional(readOnly = true)
    @Override
    public BatterySwapStation retrieveBatterySwapStation(String cpId, String csId) {
        return stationProvider.retrieveBatterySwapStation(cpId, csId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BatterySwapStationDto> retrieveBatterySwapStationBySearchCond(BatterySwapStationSearchCond searchCond) {
        return stationProvider.retrieveBatterySwapStationBySearchCond(searchCond);
    }

}
