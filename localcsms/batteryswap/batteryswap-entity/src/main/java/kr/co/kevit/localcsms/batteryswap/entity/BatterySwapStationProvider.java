/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity;

import java.util.List;

import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapStation;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface BatterySwapStationProvider {

    void registerBatterySwapStation(BatterySwapStation station);

    void modifyBatterySwapStation(BatterySwapStation station);

    int removeBatterySwapStation(String cpId, String csId);

    int removeBatterySwapStationByCpId(String cpId);

    BatterySwapStation retrieveBatterySwapStation(String cpId, String csId);

    List<BatterySwapStation> retrieveBatterySwapStationByCpId(String cpId);

    Page<BatterySwapStationDto> retrieveBatterySwapStationBySearchCond(BatterySwapStationSearchCond searchCond);

}
