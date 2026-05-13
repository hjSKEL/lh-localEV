/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 배터리 교체 기록 검색조건
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class BatterySwapRecordSearchCond extends PageCriteria implements Serializable {

    /** UID */
    private static final long serialVersionUID = 8920411483374412027L;

    /** 충전소ID */
    private String cpId;

    /** 충전기ID */
    private String csId;

    /** BatteryIn 측 고객ID */
    private String inCustomerId;

    /** BatteryOut 측 고객ID */
    private String outCustomerId;

    /** 상태 (IN / OUT / Timeout) */
    private String status;

    /** 조회 시작일 — IN_DT 기준 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromDate;

    /** 조회 종료일 — IN_DT 기준 */
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

    public String getInCustomerId() {
        return inCustomerId;
    }

    public void setInCustomerId(String inCustomerId) {
        this.inCustomerId = inCustomerId;
    }

    public String getOutCustomerId() {
        return outCustomerId;
    }

    public void setOutCustomerId(String outCustomerId) {
        this.outCustomerId = outCustomerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
