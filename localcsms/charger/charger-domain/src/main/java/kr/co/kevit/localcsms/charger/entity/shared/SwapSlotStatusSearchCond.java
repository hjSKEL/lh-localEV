/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 슬롯 상태 검색조건
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class SwapSlotStatusSearchCond extends PageCriteria implements Serializable {

    /** UID */
    private static final long serialVersionUID = 8021410338114220073L;

    /** 충전소ID */
    private String cpId;

    /** 충전기ID */
    private String csId;

    /** 슬롯번호 (evseId) */
    private Integer evseId;

    /** 슬롯상태 (BSSS00) */
    private String slotState;

    /** 배터리 시리얼번호 */
    private String batterySerialNo;

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

    public String getSlotState() {
        return slotState;
    }

    public void setSlotState(String slotState) {
        this.slotState = slotState;
    }

    public String getBatterySerialNo() {
        return batterySerialNo;
    }

    public void setBatterySerialNo(String batterySerialNo) {
        this.batterySerialNo = batterySerialNo;
    }

}
