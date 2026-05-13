/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.shared;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 슬롯 상태이력 검색조건
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class SwapSlotStatusHisSearchCond extends PageCriteria implements Serializable {

    /** UID */
    private static final long serialVersionUID = 7733029110582243341L;

    /** 충전소ID */
    private String cpId;

    /** 충전기ID */
    private String csId;

    /** 슬롯번호 */
    private Integer evseId;

    /** 변경 후 상태 (BSSS00) */
    private String newState;

    /** 트리거 유형 */
    private String eventType;

    /** 배터리 시리얼 */
    private String batterySerialNo;

    /** 조회 시작일 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromDate;

    /** 조회 종료일 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toDate;

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

    public Integer getEvseId() {
        return evseId;
    }

    public void setEvseId(Integer evseId) {
        this.evseId = evseId;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getBatterySerialNo() {
        return batterySerialNo;
    }

    public void setBatterySerialNo(String batterySerialNo) {
        this.batterySerialNo = batterySerialNo;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

}
