/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.shared;

import kr.co.kevit.localcsms.charger.entity.domain.SwapSlotStatus;

/**
 * 슬롯 상태 DTO (조회용)
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class SwapSlotStatusDto extends SwapSlotStatus {

    /** UID */
    private static final long serialVersionUID = 5530811047742210812L;

    /** 충전소명 (TB_CHCP001 조인) */
    private String cpName;

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

}
