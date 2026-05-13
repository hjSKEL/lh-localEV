/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 선불카드 검색 조건
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class PrepaidCardSearchCond extends PageCriteria implements Serializable {

    /** UID */
    private static final long serialVersionUID = 4810314410723102291L;

    /** 선불카드번호 */
    private String cardNo;

    /** 고객ID */
    private String customerId;

    /** 고객명 */
    private String customerName;

    /** 카드상태코드 (PPCS00) */
    private String cardStatCode;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCardStatCode() {
        return cardStatCode;
    }

    public void setCardStatCode(String cardStatCode) {
        this.cardStatCode = cardStatCode;
    }

}
