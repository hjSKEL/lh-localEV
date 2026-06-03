/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * 배터리 슬롯 상태 변경이력 (append-only)
 *
 * TB : TB_BSSH001
 *
 * SwapSlotStatus 가 변경될 때마다 1행 INSERT.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class SwapSlotStatusHis implements Serializable {

    /** UID */
    private static final long serialVersionUID = 9012483720114820017L;

    /**
     * PK — 일련번호 (AUTO_INCREMENT)
     * SEQ   BIGINT   NOT NULL AUTO_INCREMENT,
     */
    private Long seq;

    /**
     * 충전소ID
     * CP_ID   CHAR(6 BYTE)   NOT NULL,
     */
    private String cpId;

    /**
     * 충전기ID
     * CS_ID   CHAR(2 BYTE)   NOT NULL,
     */
    private String csId;

    /**
     * 슬롯번호
     * EVSE_ID   INT   NOT NULL,
     */
    private int evseId;

    /**
     * 변경 전 상태 (BSSS00) — 최초 등록 시 NULL
     * PREV_ST_CD   VARCHAR(20 BYTE),
     */
    private String prevState;

    /**
     * 변경 후 상태 (BSSS00)
     * NEW_ST_CD   VARCHAR(20 BYTE)   NOT NULL,
     */
    private String newState;

    /**
     * 이벤트 시점의 배터리 시리얼
     * BAT_SN   VARCHAR(50 BYTE),
     */
    private String batterySerialNo;

    /**
     * 이벤트 시점의 SoC
     * CUR_SOC   DECIMAL(5,2),
     */
    private BigDecimal currentSoC;

    /**
     * 이벤트 시점의 SoH
     * CUR_SOH   DECIMAL(5,2),
     */
    private BigDecimal currentSoH;

    /**
     * 트리거 유형
     * BatteryIn / BatteryOut / StatusNotification / Manual / Timeout / TransactionEvent ...
     * EVT_TP   VARCHAR(20 BYTE)   NOT NULL,
     */
    private String eventType;

    /**
     * 트리거 참조 ID (예: BatterySwap requestId, transactionId)
     * EVT_REF_ID   VARCHAR(64 BYTE),
     */
    private String eventRefId;

    /**
     * 이벤트 발생 시각
     * EVT_DT   DATETIME   NOT NULL,
     */
    private Date eventDate;

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

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public int getEvseId() {
        return evseId;
    }

    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    public String getPrevState() {
        return prevState;
    }

    public void setPrevState(String prevState) {
        this.prevState = prevState;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public String getBatterySerialNo() {
        return batterySerialNo;
    }

    public void setBatterySerialNo(String batterySerialNo) {
        this.batterySerialNo = batterySerialNo;
    }

    public BigDecimal getCurrentSoC() {
        return currentSoC;
    }

    public void setCurrentSoC(BigDecimal currentSoC) {
        this.currentSoC = currentSoC;
    }

    public BigDecimal getCurrentSoH() {
        return currentSoH;
    }

    public void setCurrentSoH(BigDecimal currentSoH) {
        this.currentSoH = currentSoH;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventRefId() {
        return eventRefId;
    }

    public void setEventRefId(String eventRefId) {
        this.eventRefId = eventRefId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

}
