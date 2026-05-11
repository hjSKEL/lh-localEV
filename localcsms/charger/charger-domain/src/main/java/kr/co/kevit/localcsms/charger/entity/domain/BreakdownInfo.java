/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * TB_CHBD002
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 8. 16.
 */
public class BreakdownInfo implements Serializable{
    
    /**  */
    private static final long serialVersionUID = 135816253388933321L;

    /**
     * ID
     */
    private String id;
    
    /**
     * 충전기 오류 코드
     * CS_ER_CD VARCHAR2(6)
     */
    private String stationErrorCode;
    
    /**
     * BD_CONT VARCHAR2(200)
     */
    private String breakdownContent;
    
    /**
     * 차량모델명
     * CAR_MODEL_NM VARCHAR(20),
     */
    private String carModelName;
    
    /**
     * 충전기 타입 공통코드 : CHRA00 
     * CS_CAT_CD VARCHAR2(6),
     */
    private String csCatCode;
    
    /**
     * 신고자명
     * RPT_NM
     */
    private String reporterName;
    
    /**
     * 신고자 거주지
     * RPT_ADDR
     */
    private String reporterAddr;
    
    /**
     * 신고자 휴대폰 번호
     * RPT_PHN_NO
     */
    private String reporterPhoneNum;
    
    /**
     * 
     */
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
     * Get stationErrorCode
     * @return stationErrorCode
     */
    public String getStationErrorCode() {
        return stationErrorCode;
    }

    /**
     * Set stationErrorCode
     * @param stationErrorCode
     */
    public void setStationErrorCode(String stationErrorCode) {
        this.stationErrorCode = stationErrorCode;
    }

    /**
     * Get breakdownContent
     * @return breakdownContent
     */
    public String getBreakdownContent() {
        return breakdownContent;
    }

    /**
     * Set breakdownContent
     * @param breakdownContent
     */
    public void setBreakdownContent(String breakdownContent) {
        this.breakdownContent = breakdownContent;
    }

    /**
     * Get carModelName
     * @return carModelName
     */
    public String getCarModelName() {
        return carModelName;
    }

    /**
     * Set carModelName
     * @param carModelName
     */
    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

    /**
     * Get csCatCode
     * @return csCatCode
     */
    public String getCsCatCode() {
        return csCatCode;
    }

    /**
     * Set csCatCode
     * @param csCatCode
     */
    public void setCsCatCode(String csCatCode) {
        this.csCatCode = csCatCode;
    }

    /**
     * Get reporterName
     * @return reporterName
     */
    public String getReporterName() {
		return reporterName;
	}

    /**
     * Set reporterName
     * @param reporterName
     */
	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	/**
     * Get reporterAddr
     * @return reporterAddr
     */
	public String getReporterAddr() {
		return reporterAddr;
	}

	/**
     * Set reporterAddr
     * @param reporterAddr
     */
	public void setReporterAddr(String reporterAddr) {
		this.reporterAddr = reporterAddr;
	}

	/**
     * Get reporterPhoneNum
     * @return reporterPhoneNum
     */
	public String getReporterPhoneNum() {
		return reporterPhoneNum;
	}

	/**
     * Set reporterPhoneNum
     * @param reporterPhoneNum
     */
	public void setReporterPhoneNum(String reporterPhoneNum) {
		this.reporterPhoneNum = reporterPhoneNum;
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

    
}