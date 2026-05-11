/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 충전 상태 정보 TB : TB_CHCS005
 * 
 * @since 2020. 06. 10.
 *
 */
public class ChargerStatusInfo implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 7564386206993118174L;

    /**
     * PK
     * 충전소 ID CP_ID CHAR(9)
     */
    private String cpId;

    /**
     * PK
     * 충전기 ID CS_ID VARCHAR2(2 BYTE) NOT NULL,
     */
    private String csId;
    
    /**
     * PK
     * EVSE_ID
     */
    private int evseId;
    
    /**
     * 정보수집일시 INFO_COLL_DT DATE,
     */
    private Date infoCollDate;
    
    /**
     * 충전기 타입 CS_CAT_CD VARCHAR2(6 BYTE),
     */
    private String csCatCode;

    /**
     * 충전기 상태정보 공통코드 : 공통코드 : CHRS00 CS_STAT_CD VARCHAR2(6 BYTE),
     */
    private String csStatCode;
    
    /**
     * 충전기 케이블 상태
     * 
     * 1/0 CS_CBL_STAT VARCHAR2(1 BYTE),
     */
    private String csCableStatus;
    
    /**
     * 충전번호 RC_ID CHAR(24 BYTE),
     */
    private String rechargingId;

    /**
     * 충전기 오류 상태 공통코드 : CHE000
     * 
     * CS_ERR_STAT VARCHAR2(6 BYTE),
     */
    private String csErrorStatus;

    /**
     * 충전사용전력량 CU_ELE_NRG NUMBER(8,2),
     */
    private BigDecimal cuEleEnerge;

    /**
     * 충전 누적 전력량 CA_ELE_NRG NUMBER(15,2),
     */
    private BigDecimal caEleEnerge;
    
    /**
     * 순간 충전량 INST_CH_AMT NUMBER(8,2),
     */
    private BigDecimal instChAmont;
    
    /**
     * 순간충전단가 INST_CH_CST NUMBER(8,2),
     */
    private BigDecimal instChCost;
    
    /**
     * 순간충전금액 INST_CH_SUM NUMBER(8,2),
     */
    private BigDecimal instChSum;
    
    /**
     * 충전금액 CH_SUM NUMBER(8,2),
     */
    private BigDecimal chSum;
    
    /**
     * 충전시작시간 CH_ST_DT DATE,
     */
    private Date chStartDate;
    
    /**
     * 충전종료시간 CH_ED_DT DATE,
     */
    private Date chEndDate;
    
    /**
     * 충전시작시간 LST_CH_ST_DT DATE,
     */
    private Date lastChStartDate;
    
    /**
     * 충전종료시간 LST_CH_ED_DT DATE,
     */
    private Date lastChEndDate;
    
    /**
     * 고객카드번호(티머니번호) MEM_NO VARCHAR2(16 BYTE)
     */
    private String cutCardNo;

    /**
     * 이벤트 코드 EVENT_CD VARCHAR2(6 BYTE),
     */
    private String eventCode;
    
    /**
     * 2019.01.16 추가 충전케이블 충전 속도 CS_CBL_SPD
     */
    private BigDecimal csCableSpeed;
    
    /**
     * 수정일 UPD_DT
     *
     */
    private Date updateDate;

    public ChargerStatusInfo() {
    }

    public ChargerStatusInfo(ChargingStationCsm chargingStationCsm) {
        //
        this.cpId = chargingStationCsm.getCpId();
        this.csId = chargingStationCsm.getCsId();
        this.csCatCode = chargingStationCsm.getCsCatCode();

        this.csCableStatus = StringConstants.ZERO;
        this.csErrorStatus = "CHE001";
        this.cuEleEnerge = BigDecimal.ZERO;
        this.caEleEnerge = BigDecimal.ZERO;
        this.instChAmont = BigDecimal.ZERO;
        this.instChCost = BigDecimal.ZERO;
        this.instChSum = BigDecimal.ZERO;
        this.chSum = BigDecimal.ZERO;
        this.csCableSpeed = BigDecimal.ZERO;
        this.infoCollDate = new Date();
        this.updateDate = this.infoCollDate;
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
     * Get evseId
     * @return evseId
     */
    public int getEvseId() {
        return evseId;
    }

    /**
     * Set evseId
     * @param evseId
     */
    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    /**
     * Get infoCollDate
     * @return infoCollDate
     */
    public Date getInfoCollDate() {
        return infoCollDate;
    }

    /**
     * Set infoCollDate
     * @param infoCollDate
     */
    public void setInfoCollDate(Date infoCollDate) {
        this.infoCollDate = infoCollDate;
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
     * Get csStatCode
     * @return csStatCode
     */
    public String getCsStatCode() {
        return csStatCode;
    }

    /**
     * Set csStatCode
     * @param csStatCode
     */
    public void setCsStatCode(String csStatCode) {
        this.csStatCode = csStatCode;
    }

    /**
     * Get csCableStatus
     * @return csCableStatus
     */
    public String getCsCableStatus() {
        return csCableStatus;
    }

    /**
     * Set csCableStatus
     * @param csCableStatus
     */
    public void setCsCableStatus(String csCableStatus) {
        this.csCableStatus = csCableStatus;
    }

    /**
     * Get rechargingId
     * @return rechargingId
     */
    public String getRechargingId() {
        return rechargingId;
    }

    /**
     * Set rechargingId
     * @param rechargingId
     */
    public void setRechargingId(String rechargingId) {
        this.rechargingId = rechargingId;
    }

    /**
     * Get csErrorStatus
     * @return csErrorStatus
     */
    public String getCsErrorStatus() {
        return csErrorStatus;
    }

    /**
     * Set csErrorStatus
     * @param csErrorStatus
     */
    public void setCsErrorStatus(String csErrorStatus) {
        this.csErrorStatus = csErrorStatus;
    }

    /**
     * Get cuEleEnerge
     * @return cuEleEnerge
     */
    public BigDecimal getCuEleEnerge() {
        return cuEleEnerge;
    }

    /**
     * Set cuEleEnerge
     * @param cuEleEnerge
     */
    public void setCuEleEnerge(BigDecimal cuEleEnerge) {
        this.cuEleEnerge = cuEleEnerge;
    }

    /**
     * Get caEleEnerge
     * @return caEleEnerge
     */
    public BigDecimal getCaEleEnerge() {
        return caEleEnerge;
    }

    /**
     * Set caEleEnerge
     * @param caEleEnerge
     */
    public void setCaEleEnerge(BigDecimal caEleEnerge) {
        this.caEleEnerge = caEleEnerge;
    }

    /**
     * Get instChAmont
     * @return instChAmont
     */
    public BigDecimal getInstChAmont() {
        return instChAmont;
    }

    /**
     * Set instChAmont
     * @param instChAmont
     */
    public void setInstChAmont(BigDecimal instChAmont) {
        this.instChAmont = instChAmont;
    }

    /**
     * Get instChCost
     * @return instChCost
     */
    public BigDecimal getInstChCost() {
        return instChCost;
    }

    /**
     * Set instChCost
     * @param instChCost
     */
    public void setInstChCost(BigDecimal instChCost) {
        this.instChCost = instChCost;
    }

    /**
     * Get instChSum
     * @return instChSum
     */
    public BigDecimal getInstChSum() {
        return instChSum;
    }

    /**
     * Set instChSum
     * @param instChSum
     */
    public void setInstChSum(BigDecimal instChSum) {
        this.instChSum = instChSum;
    }

    /**
     * Get chSum
     * @return chSum
     */
    public BigDecimal getChSum() {
        return chSum;
    }

    /**
     * Set chSum
     * @param chSum
     */
    public void setChSum(BigDecimal chSum) {
        this.chSum = chSum;
    }

    /**
     * Get chStartDate
     * @return chStartDate
     */
    public Date getChStartDate() {
        return chStartDate;
    }

    /**
     * Set chStartDate
     * @param chStartDate
     */
    public void setChStartDate(Date chStartDate) {
        this.chStartDate = chStartDate;
    }

    /**
     * Get chEndDate
     * @return chEndDate
     */
    public Date getChEndDate() {
        return chEndDate;
    }

    /**
     * Set chEndDate
     * @param chEndDate
     */
    public void setChEndDate(Date chEndDate) {
        this.chEndDate = chEndDate;
    }

    /**
     * Get lastChStartDate
     * @return lastChStartDate
     */
    public Date getLastChStartDate() {
        return lastChStartDate;
    }

    /**
     * Set lastChStartDate
     * @param lastChStartDate
     */
    public void setLastChStartDate(Date lastChStartDate) {
        this.lastChStartDate = lastChStartDate;
    }

    /**
     * Get lastChEndDate
     * @return lastChEndDate
     */
    public Date getLastChEndDate() {
        return lastChEndDate;
    }

    /**
     * Set lastChEndDate
     * @param lastChEndDate
     */
    public void setLastChEndDate(Date lastChEndDate) {
        this.lastChEndDate = lastChEndDate;
    }

    /**
     * Get cutCardNo
     * @return cutCardNo
     */
    public String getCutCardNo() {
        return cutCardNo;
    }

    /**
     * Set cutCardNo
     * @param cutCardNo
     */
    public void setCutCardNo(String cutCardNo) {
        this.cutCardNo = cutCardNo;
    }

    /**
     * Get eventCode
     * @return eventCode
     */
    public String getEventCode() {
        return eventCode;
    }

    /**
     * Set eventCode
     * @param eventCode
     */
    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    /**
     * Get csCableSpeed
     * @return csCableSpeed
     */
    public BigDecimal getCsCableSpeed() {
        return csCableSpeed;
    }

    /**
     * Set csCableSpeed
     * @param csCableSpeed
     */
    public void setCsCableSpeed(BigDecimal csCableSpeed) {
        this.csCableSpeed = csCableSpeed;
    }

    /**
     * Get updateDate
     * @return updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * Set updateDate
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
