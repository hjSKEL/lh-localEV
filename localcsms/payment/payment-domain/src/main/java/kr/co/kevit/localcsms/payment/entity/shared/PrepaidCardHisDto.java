/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.shared;

import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCardHis;

/**
 * 선불카드 거래이력 DTO
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class PrepaidCardHisDto extends PrepaidCardHis {

    /** UID */
    private static final long serialVersionUID = 3110742810031227442L;

    /** 고객명 */
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
