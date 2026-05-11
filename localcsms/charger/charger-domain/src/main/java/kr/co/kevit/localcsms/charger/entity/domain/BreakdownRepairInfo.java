/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * TB_CHBD003
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 8. 16.
 */
public class BreakdownRepairInfo implements Serializable{
    
    /**  */
    private static final long serialVersionUID = 2910209741026309189L;

    /**
     * ID
     */
    private String id;
    
    /**
     * 고장 사유
     * REASON VARCHAR(200),
     */
    private String reason;
    
    /**
     * 조치내용
     * REP_CONT VARCHAR(200) NOT NULL,
     */
    private String repairContent;
    
    /**
     * 비고
     * REP_NOTE VARCHAR(100),
     */
    private String repairNote;
    
    /**
     * 고장수리 회사명
     * REP_CO_NM       VARCHAR(60)                NOT NULL,
     */
    private String repairCompanyName;
    /**
     * 고장 수리자 연락처
     * REP_MBL_PHN_NO VARCHAR2(60 BYTE) NOT NULL,
     */
    private String repairMblPhoneNo;
    
    /**
     * 고장수리자 명
     * REP_NM VARCHAR2(20 BYTE) NOT NULL,
     */
    private String repairName;
    
    /**
     * 고장 수리자 연락처
     * REP_POS VARCHAR2(60 BYTE) NOT NULL,
     */
    private String repairPosition;

    private Writer writer;


    /**
     * Relation ....
     */
    private BreakdownMgtInfo breakdownMgtInfo;


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
     * Get reason
     * @return reason
     */
    public String getReason() {
        return reason;
    }


    /**
     * Set reason
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }


    /**
     * Get repairContent
     * @return repairContent
     */
    public String getRepairContent() {
        return repairContent;
    }


    /**
     * Set repairContent
     * @param repairContent
     */
    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }


    /**
     * Get repairNote
     * @return repairNote
     */
    public String getRepairNote() {
        return repairNote;
    }


    /**
     * Set repairNote
     * @param repairNote
     */
    public void setRepairNote(String repairNote) {
        this.repairNote = repairNote;
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
     * Get breakdownMgtInfo
     * @return breakdownMgtInfo
     */
    public BreakdownMgtInfo getBreakdownMgtInfo() {
        return breakdownMgtInfo;
    }


    /**
     * Set breakdownMgtInfo
     * @param breakdownMgtInfo
     */
    public void setBreakdownMgtInfo(BreakdownMgtInfo breakdownMgtInfo) {
        this.breakdownMgtInfo = breakdownMgtInfo;
    }


    /**
     * Get repairCompanyName
     * @return repairCompanyName
     */
    public String getRepairCompanyName() {
        return repairCompanyName;
    }


    /**
     * Set repairCompanyName
     * @param repairCompanyName
     */
    public void setRepairCompanyName(String repairCompanyName) {
        this.repairCompanyName = repairCompanyName;
    }


    /**
     * Get repairMblPhoneNo
     * @return repairMblPhoneNo
     */
    public String getRepairMblPhoneNo() {
        return repairMblPhoneNo;
    }


    /**
     * Set repairMblPhoneNo
     * @param repairMblPhoneNo
     */
    public void setRepairMblPhoneNo(String repairMblPhoneNo) {
        this.repairMblPhoneNo = repairMblPhoneNo;
    }


    /**
     * Get repairName
     * @return repairName
     */
    public String getRepairName() {
        return repairName;
    }


    /**
     * Set repairName
     * @param repairName
     */
    public void setRepairName(String repairName) {
        this.repairName = repairName;
    }


    /**
     * Get repairPosition
     * @return repairPosition
     */
    public String getRepairPosition() {
        return repairPosition;
    }


    /**
     * Set repairPosition
     * @param repairPosition
     */
    public void setRepairPosition(String repairPosition) {
        this.repairPosition = repairPosition;
    }
    
}
