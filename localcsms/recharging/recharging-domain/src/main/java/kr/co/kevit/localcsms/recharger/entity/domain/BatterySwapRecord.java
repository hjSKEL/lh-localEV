/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * 배터리 교체 기록 (cycle 헤더)
 *
 * TB : TB_RCBS001
 *
 * OCPP 2.1 BatterySwap 1 cycle = 1행. requestId 단위.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class BatterySwapRecord implements Serializable {

    /** UID */
    private static final long serialVersionUID = 4471102203812410023L;

    /**
     * PK
     * OCPP requestId
     * REQ_ID   BIGINT   NOT NULL,
     */
    private Long requestId;

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
     * BatteryIn 측 고객ID (배터리 반납자)
     * IN_CUT_ID   CHAR(9 BYTE),
     */
    private String inCustomerId;

    /**
     * BatteryOut 측 고객ID (배터리 수령자)
     * OUT_CUT_ID   CHAR(9 BYTE),
     */
    private String outCustomerId;

    /**
     * BatteryIn 배터리 수
     * IN_CNT   INT   DEFAULT 0,
     */
    private int inCount;

    /**
     * BatteryIn 수신 시각
     * IN_DT   DATETIME,
     */
    private Date inDateTime;

    /**
     * BatteryOut 배터리 수
     * OUT_CNT   INT   DEFAULT 0,
     */
    private int outCount;

    /**
     * BatteryOut 수신 시각
     * OUT_DT   DATETIME,
     */
    private Date outDateTime;

    /**
     * 상태 — IN / OUT / Timeout
     * STAT_CD   VARCHAR(10 BYTE)   NOT NULL,
     */
    private String status;

    /**
     * 등록정보
     */
    private Writer writer;

    /**
     * Object Relation — 디테일 (배터리별)
     */
    private List<BatterySwapRecordDetail> details;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
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

    public int getInCount() {
        return inCount;
    }

    public void setInCount(int inCount) {
        this.inCount = inCount;
    }

    public Date getInDateTime() {
        return inDateTime;
    }

    public void setInDateTime(Date inDateTime) {
        this.inDateTime = inDateTime;
    }

    public int getOutCount() {
        return outCount;
    }

    public void setOutCount(int outCount) {
        this.outCount = outCount;
    }

    public Date getOutDateTime() {
        return outDateTime;
    }

    public void setOutDateTime(Date outDateTime) {
        this.outDateTime = outDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public List<BatterySwapRecordDetail> getDetails() {
        return details;
    }

    public void setDetails(List<BatterySwapRecordDetail> details) {
        this.details = details;
    }

}
