/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.domain;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * 선불카드 거래이력
 *
 * TB : TB_PYCH001
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class PrepaidCardHis implements Serializable {

    /** ISSUE - 발급 (초기 충전, 잔액 +) */
    public static final String TYPE_ISSUE = "ISSUE";

    /** USE   - 사용 (충전요금 차감, 잔액 -) */
    public static final String TYPE_USE = "USE";

    /** UID */
    private static final long serialVersionUID = 1192018847215240312L;

    /**
     * PK
     * 일련번호 (AUTO_INCREMENT)
     * SEQ   BIGINT   NOT NULL,
     */
    private Long seq;

    /**
     * 선불카드번호 (FK -> TB_PYCD001)
     * CARD_NO   VARCHAR(20 BYTE)   NOT NULL,
     */
    private String cardNo;

    /**
     * 거래유형
     * ISSUE : 발급 / USE : 사용
     * TYPE_CD   VARCHAR(10 BYTE)   NOT NULL,
     */
    private String typeCode;

    /**
     * 거래금액 (부호 포함: 충전 +, 사용 -)
     * AMOUNT   BIGINT   NOT NULL,
     */
    private Long amount;

    /**
     * 거래 전 잔액
     * BAL_BEFORE   BIGINT   NOT NULL,
     */
    private Long balanceBefore;

    /**
     * 거래 후 잔액
     * BAL_AFTER   BIGINT   NOT NULL,
     */
    private Long balanceAfter;

    /**
     * 연결된 충전ID (USE 일 때만 세팅)
     * RC_ID   VARCHAR(36 BYTE),
     */
    private String rechargingId;

    /**
     * 등록정보
     */
    private Writer writer;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(Long balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public Long getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Long balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getRechargingId() {
        return rechargingId;
    }

    public void setRechargingId(String rechargingId) {
        this.rechargingId = rechargingId;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

}
