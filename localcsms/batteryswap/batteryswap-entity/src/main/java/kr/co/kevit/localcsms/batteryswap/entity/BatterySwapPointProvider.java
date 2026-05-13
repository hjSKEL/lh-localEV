/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity;

import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapPoint;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface BatterySwapPointProvider {

    void registerBatterySwapPoint(BatterySwapPoint point);

    void modifyBatterySwapPoint(BatterySwapPoint point);

    int removeBatterySwapPoint(String cpId);

    BatterySwapPoint retrieveBatterySwapPoint(String cpId);

    Page<BatterySwapPointDto> retrieveBatterySwapPointBySearchCond(BatterySwapPointSearchCond searchCond);

}
