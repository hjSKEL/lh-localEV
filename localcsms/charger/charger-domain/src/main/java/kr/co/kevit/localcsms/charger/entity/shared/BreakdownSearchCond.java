/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
public class BreakdownSearchCond extends PageCriteria{
    
    /**
     * 충전소 CP_ID VARCHAR2(8) NOT NULL,
     */
    private String cpId;

    /**
     * 충전기 CS_ID VARCHAR2(2) NOT NULL,
     */
    private String csId;
    
    /**
     * 접수날짜
     * RCPT_DT
     */
    private String receiptDate;
    
    /**
     * 조치(수리) 날짜
     * REP_DT
     */
    private String repairDate;
    
    /**
     * 상태
     * 공통코드 : BDST00
     * BD_STAT
     */
    private String breakdownStatus;
    
    private String fromDate;
    
    private String toDate;

    /**
     * Get cpId
     * @return cpId
     */
    public String getCpId() {
        return cpId;
    }

    /**
     * Set cpId
     * @param cpId
     */
    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    /**
     * Get csId
     * @return csId
     */
    public String getCsId() {
        return csId;
    }

    /**
     * Set csId
     * @param csId
     */
    public void setCsId(String csId) {
        this.csId = csId;
    }

    /**
     * Get receiptDate
     * @return receiptDate
     */
    public String getReceiptDate() {
        return receiptDate;
    }

    /**
     * Set receiptDate
     * @param receiptDate
     */
    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    /**
     * Get repairDate
     * @return repairDate
     */
    public String getRepairDate() {
        return repairDate;
    }

    /**
     * Set repairDate
     * @param repairDate
     */
    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }

    /**
     * Get breakdownStatus
     * @return breakdownStatus
     */
    public String getBreakdownStatus() {
        return breakdownStatus;
    }

    /**
     * Set breakdownStatus
     * @param breakdownStatus
     */
    public void setBreakdownStatus(String breakdownStatus) {
        this.breakdownStatus = breakdownStatus;
    }

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
    
}
