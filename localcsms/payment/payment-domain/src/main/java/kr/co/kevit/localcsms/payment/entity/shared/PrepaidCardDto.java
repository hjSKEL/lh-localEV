/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.shared;

import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;

/**
 * 선불카드 DTO (조회 시 고객명 조인용)
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class PrepaidCardDto extends PrepaidCard {

    /** UID */
    private static final long serialVersionUID = -7488510031120442208L;

    /** 고객명 */
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
