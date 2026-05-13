/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecord;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordDto;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordSearchCond;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface BatterySwapRecordProvider {

    void registerBatterySwapRecord(BatterySwapRecord record);

    void modifyBatterySwapRecord(BatterySwapRecord record);

    BatterySwapRecord retrieveBatterySwapRecord(Long requestId);

    Page<BatterySwapRecordDto> retrieveBatterySwapRecordBySearchCond(BatterySwapRecordSearchCond searchCond);

}
