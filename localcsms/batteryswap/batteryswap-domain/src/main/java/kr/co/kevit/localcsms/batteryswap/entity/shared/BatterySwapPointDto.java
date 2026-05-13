/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.shared;

import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapPoint;

/**
 * 배터리 교체 충전소 DTO
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class BatterySwapPointDto extends BatterySwapPoint {

    /** UID */
    private static final long serialVersionUID = -3308124210440728791L;

    /** 등록된 교환충전기 수 (집계) */
    private int registeredStationCount;

    public int getRegisteredStationCount() {
        return registeredStationCount;
    }

    public void setRegisteredStationCount(int registeredStationCount) {
        this.registeredStationCount = registeredStationCount;
    }

}
