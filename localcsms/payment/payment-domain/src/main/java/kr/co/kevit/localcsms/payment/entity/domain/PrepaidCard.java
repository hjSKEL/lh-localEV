/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.domain;

import java.io.Serializable;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * 선불카드
 *
 * TB : TB_PYCD001
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class PrepaidCard implements Serializable {

    /** UID */
    private static final long serialVersionUID = 6204713802617743501L;

    /**
     * PK
     * 선불카드번호
     * CARD_NO   VARCHAR(20 BYTE)   NOT NULL,
     */
    private String cardNo;

    /**
     * 고객아이디
     * CUT_ID   CHAR(9 BYTE),
     */
    private String customerId;

    /**
     * 유효기간 만료일
     * EXPIRE_DT  DATE,
     */
    private Date expireDate;

    /**
     * 선불카드상태코드
     * 공통코드 : PPCS00
     * 활성(PPCS01) / 정지(PPCS02) / 만료(PPCS03)
     * CARD_STAT_CD   VARCHAR2(6 BYTE)   DEFAULT 'PPCS01'   NOT NULL,
     */
    private String cardStatCode;

    /**
     * 잔액 (원)
     * BALANCE   BIGINT   DEFAULT 0   NOT NULL,
     */
    private Long balance = 0L;

    /**
     * 등록정보
     */
    private Writer writer;

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

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getCardStatCode() {
        return cardStatCode;
    }

    public void setCardStatCode(String cardStatCode) {
        this.cardStatCode = cardStatCode;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

}
