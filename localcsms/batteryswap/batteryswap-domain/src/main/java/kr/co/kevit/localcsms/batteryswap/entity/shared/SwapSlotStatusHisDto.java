/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.shared;

import kr.co.kevit.localcsms.batteryswap.entity.domain.SwapSlotStatusHis;

/**
 * 슬롯 상태이력 DTO
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class SwapSlotStatusHisDto extends SwapSlotStatusHis {

    /** UID */
    private static final long serialVersionUID = 1208334017740521921L;

    /** 충전소명 (조인) */
    private String cpName;

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

}
