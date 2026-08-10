/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.shared;

import kr.co.kevit.localcsms.payment.entity.domain.PspPayment;

public class PspPaymentDto extends PspPayment {
    private static final long serialVersionUID = 5301025001801251101L;

    /**
     * 단지명 (TB_CHCP001 -> TB_ORCX001 조인 조회 전용, 저장 안 함)
     */
    private String complexName;

    public String getComplexName() {
        return complexName;
    }

    public void setComplexName(String complexName) {
        this.complexName = complexName;
    }
}
