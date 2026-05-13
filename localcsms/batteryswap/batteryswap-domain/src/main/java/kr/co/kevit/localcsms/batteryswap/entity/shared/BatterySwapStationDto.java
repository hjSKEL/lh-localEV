/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.shared;

import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapStation;

/**
 * 배터리 교환 충전기 DTO
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class BatterySwapStationDto extends BatterySwapStation {

    /** UID */
    private static final long serialVersionUID = 9012740118803124221L;

    /** 충전소명 (join 결과) */
    private String cpName;

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

}
