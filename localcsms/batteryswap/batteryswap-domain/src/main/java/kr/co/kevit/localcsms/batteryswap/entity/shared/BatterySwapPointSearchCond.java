/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 배터리 교체 충전소 검색조건
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class BatterySwapPointSearchCond extends PageCriteria implements Serializable {

    /** UID */
    private static final long serialVersionUID = 1124712038001442298L;

    /** 충전소ID */
    private String cpId;

    /** 충전소명 (LIKE) */
    private String cpName;

    /** 운영중 여부 — 'Y'=closedDate IS NULL, 'N'=closedDate IS NOT NULL */
    private String operatingYn;

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getOperatingYn() {
        return operatingYn;
    }

    public void setOperatingYn(String operatingYn) {
        this.operatingYn = operatingYn;
    }

}
