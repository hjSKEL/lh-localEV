/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * TB_CHBD001
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 8. 16.
 */
public class BreakdownMgtInfo implements Serializable {

    /**  */
    private static final long serialVersionUID = 3563707743122359817L;

    /**
     * ID
     * BD00000000001
     * BD_ID CHAR(13)
     */
    private String id;
    
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
     * 접수시간
     * RCPT_TM
     */
    private String receiptTime;

    /**
     * 조치(수리) 날짜
     * REP_DT
     */
    private String repairDate;

    /**
     * 조치(수리) 시간
     * REP_TM
     */
    private String repairTime;
    
    /**
     * 상태
     * 공통코드 : BDST00
     * BD_STAT
     */
    private String breakdownStatus;
    
    /**
     * 
     */
    private Writer writer;
    
    /**
     * Relation
     */
    private BreakdownInfo receptInfo;
    
    /**
     * Relation
     */
    private BreakdownRepairInfo repairInfo;

    /**
     * Get id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

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
     * Get receiptTime
     * @return receiptTime
     */
    public String getReceiptTime() {
        return receiptTime;
    }

    /**
     * Set receiptTime
     * @param receiptTime
     */
    public void setReceiptTime(String receiptTime) {
        this.receiptTime = receiptTime;
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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
     * Get writer
     * @return writer
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * Set writer
     * @param writer
     */
    public void setWriter(Writer writer) {
        this.writer = writer;
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
     * Get repairTime
     * @return repairTime
     */
    public String getRepairTime() {
        return repairTime;
    }

    /**
     * Set repairTime
     * @param repairTime
     */
    public void setRepairTime(String repairTime) {
        this.repairTime = repairTime;
    }

    /**
     * Get receptInfo
     * @return receptInfo
     */
    public BreakdownInfo getReceptInfo() {
        return receptInfo;
    }

    /**
     * Set receptInfo
     * @param receptInfo
     */
    public void setReceptInfo(BreakdownInfo receptInfo) {
        this.receptInfo = receptInfo;
    }

    /**
     * Get repairInfo
     * @return repairInfo
     */
    public BreakdownRepairInfo getRepairInfo() {
        return repairInfo;
    }

    /**
     * Set repairInfo
     * @param repairInfo
     */
    public void setRepairInfo(BreakdownRepairInfo repairInfo) {
        this.repairInfo = repairInfo;
    }

}