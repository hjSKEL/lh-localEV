/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * TB_CHCP001
 * 충전소
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 06. 10.
 */
public class ChargePoint implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -8246521715180659270L;

    /**
     * 충전소ID
     * CP_ID       VARCHAR2(6 BYTE)                 NOT NULL,
     */
    private String cpId;
    
    /**
     * 충전소명
     * CP_NM      VARCHAR2(60 BYTE)                 NOT NULL,
     */
    private String cpName;
    
    /**
     * 급속충전기대수
     * HI_CS_CNT    NUMBER,
     */
    private int highCsCount = 0;

    /**
     * 완속충전기대수
     * LO_CS_CNT    NUMBER,
     */
    private int lowCsCount;
    
    /**
     * 전력량
     * ELEC_SPLY_CPTY INT(5)
     */
    private int electSupplyCapability;
    
    /**
     * 충전소 위치
     * CP_LOC     VARCHAR2(200 BYTE),
     */
    private String cpLocation;
    
    /**
     * 충전소사용가능여부
     * CP_US_YN       CHAR(1 BYTE)                      DEFAULT 'N'                   NOT NULL,
     */
    private String cpUseYn = StringConstants.Y;

    /**
     * 단지아이디
     * CX_ID       CHAR(9 BYTE),
     */
    private String complexId;

    /**
     * 단지명 (TB_ORCX001 조인 조회 전용, 저장 안 함)
     */
    private String complexName;

    /**
     * 삭제여부
     * DEL_YN       CHAR(1 BYTE)                      DEFAULT 'N'                   NOT NULL,
     */
    private String deleteYn = StringConstants.N;

    /**
     * 삭제일
     * DEL_DT      DATE,
     */
    private Date deleteDate;

    private List<ChargingStation> chargingStations;

    /**
     * 등록정보
     */
    private Writer writer;

    private String memo;

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
     * Get cpName
     * @return cpName
     */
    public String getCpName() {
        return cpName;
    }

    /**
     * Set cpName
     * @param cpName
     */
    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    /**
     * Get highCsCount
     * @return highCsCount
     */
    public int getHighCsCount() {
        return highCsCount;
    }

    /**
     * Set highCsCount
     * @param highCsCount
     */
    public void setHighCsCount(int highCsCount) {
        this.highCsCount = highCsCount;
    }

    /**
     * Get lowCsCount
     * @return lowCsCount
     */
    public int getLowCsCount() {
        return lowCsCount;
    }

    /**
     * Set lowCsCount
     * @param lowCsCount
     */
    public void setLowCsCount(int lowCsCount) {
        this.lowCsCount = lowCsCount;
    }

    /**
     * Get cpLocation
     * @return cpLocation
     */
    public String getCpLocation() {
        return cpLocation;
    }

    /**
     * Set cpLocation
     * @param cpLocation
     */
    public void setCpLocation(String cpLocation) {
        this.cpLocation = cpLocation;
    }

    /**
     * Get cpUseYn
     * @return cpUseYn
     */
    public String getCpUseYn() {
        return cpUseYn;
    }

    /**
     * Set cpUseYn
     * @param cpUseYn
     */
    public void setCpUseYn(String cpUseYn) {
        this.cpUseYn = cpUseYn;
    }

    /**
     * Get complexId
     * @return complexId
     */
    public String getComplexId() {
        return complexId;
    }

    /**
     * Set complexId
     * @param complexId
     */
    public void setComplexId(String complexId) {
        this.complexId = complexId;
    }

    /**
     * Get complexName
     * @return complexName
     */
    public String getComplexName() {
        return complexName;
    }

    /**
     * Set complexName
     * @param complexName
     */
    public void setComplexName(String complexName) {
        this.complexName = complexName;
    }

    /**
     * Get deleteYn
     * @return deleteYn
     */
    public String getDeleteYn() {
        return deleteYn;
    }

    /**
     * Set deleteYn
     * @param deleteYn
     */
    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    /**
     * Get deleteDate
     * @return deleteDate
     */
    public Date getDeleteDate() {
        return deleteDate;
    }

    /**
     * Set deleteDate
     * @param deleteDate
     */
    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    /**
     * Get chargingStations
     * @return chargingStations
     */
    public List<ChargingStation> getChargingStations() {
        return chargingStations;
    }

    /**
     * Set chargingStations
     * @param chargingStations
     */
    public void setChargingStations(List<ChargingStation> chargingStations) {
        this.chargingStations = chargingStations;
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
     * Get memo
     * @return memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Set memo
     * @param memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * Get electSupplyCapability
     * @return electSupplyCapability
     */
    public int getElectSupplyCapability() {
        return electSupplyCapability;
    }

    /**
     * Set electSupplyCapability
     * @param electSupplyCapability
     */
    public void setElectSupplyCapability(int electSupplyCapability) {
        this.electSupplyCapability = electSupplyCapability;
    }
    
}
