/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.domain;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TB_RCRC001
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 17.
 */
public class Recharging implements Serializable {

    /**  */
    private static final long serialVersionUID = 6227653760426442995L;

    /**
     * 충전아이디 - 충전소ID(9)+충전기ID(2)+날짜(8)+번호(5) ex)112905004+01+20190417+00001 RC_ID
     * VARCHAR(36) NOT NULL,
     */
    private String rechargingId;

    /**
     * 충전소아이디 CP_ID CHAR(9) NOT NULL,
     */
    private String cpId;

    /**
     * 충전기아이디 CS_ID CHAR(2) NOT NULL,
     */
    private String csId;

    /**
     * 
     * EVSE_ID
     */
    private int evseId;

    /**
     * 고객ID CUT_ID CHAR(9)
     */
    private String customerId;

    /**
     * 고객카드번호 CUT_CRD_NO VARCHAR2(16 BYTE),
     */
    private String cutCardNo;

    /**
     * 고객 소속사 ID CO_ID CHAR(9)
     */
    private String companyId = StringConstants.DEFAULT_COMPANYID;

    /**
     * 마감 날짜 CL_DT CHAR(8)
     */
    private String closedDate;

    /**
     * 상품코드 PRD_TP VARCHAR2(6 BYTE),
     */
    private String productId;

    /**
     * 충전시작시간 CH_ST_DT DATE,
     */
    private Date chStartDate;

    /**
     * 충전종료시간 CH_ED_DT DATE,
     */
    private Date chEndDate;

    /**
     * 충전상태코드 - 공통코드 : RECS00 CH_STAT_CD CHAR(6 BYTE),
     */
    private String chStatCode;

    /**
     * 충전기사용전력량 CH_US_AMT NUMBER(12,3),
     */
    private BigDecimal chUseAmount = BigDecimal.ZERO;

    /**
     * 충전기사용단가 CH_US_CST NUMBER(12,3),
     */
    private BigDecimal chUseUnitCost = BigDecimal.ZERO;

    /**
     * 충전기사용전력요금 CH_US_SUM DOUBLE,
     */
    private BigDecimal chUseCost = BigDecimal.ZERO;

    /**
     * 방전 전력량 (V2X export, kWh) - OCPP Energy.Active.Export.Register 누적
     * DCH_US_AMT NUMBER(12,3) DEFAULT 0
     */
    private BigDecimal dchUseAmount = BigDecimal.ZERO;

    /**
     * 방전 단가
     * DCH_US_CST NUMBER(12,3) DEFAULT 0
     */
    private BigDecimal dchUseUnitCost = BigDecimal.ZERO;

    /**
     * 방전 금액 (양수 = 사업자→고객 보상금, 정책에 따라 결제 차감)
     * DCH_US_SUM DOUBLE DEFAULT 0
     */
    private BigDecimal dchUseCost = BigDecimal.ZERO;

    /**
     * 결제금액 PAY_SUM INT(11),
     */
    private Integer paySum = 0;

    /**
     * 할인된최종금액 DIS_SUM NUMBER DEFAULT 0,
     */
    private Integer finalPaySum = 0;

    /**
     * 충전 시작 누적 전력량 ST_CA_ELE_NRG NUMBER(15,2),
     */
    private BigDecimal startCaEleEnerge = BigDecimal.ZERO;

    /**
     * 충전 종료 누적 전력량 ED_CA_ELE_NRG NUMBER(15,2),
     */
    private BigDecimal endCaEleEnerge = BigDecimal.ZERO;

    /**
     * 방전 시작 누적 전력량 (V2X export, Wh) ST_DA_ELE_NRG NUMBER(15,2)
     */
    private BigDecimal startDaEleEnerge = BigDecimal.ZERO;

    /**
     * 방전 종료 누적 전력량 (V2X export, Wh) ED_DA_ELE_NRG NUMBER(15,2)
     */
    private BigDecimal endDaEleEnerge = BigDecimal.ZERO;

    /**
     * 트랜잭션 최대 에너지 한도 (Wh) — OCPP 2.1 TransactionLimitType.maxEnergy.
     * CSMS 가 override 한 값을 보관하여 다음 TransactionEventResponse 에 echo (E16.FR.02/07).
     * MAX_ENERGY DOUBLE DEFAULT 0
     */
    private Double maxEnergy = 0.0;

    private String cellphone;
    
    /**
     * 주차시작시간 PK_ST_DT DATETIME
     */
    private Date pkStartDate;

    /**
     * 출차시간 PK_ED_DT DATETIME
     */
    private Date pkEndDate;

    /**
     * 케이블연결시간 CBL_ST_DT DATETIME
     */
    private Date cableStartDate;

    /**
     * 케이블연결종료시간 CBL_ED_DT DATETIME
     */
    private Date cableEndDate;

    /**
     * ERR_CONT VARCHAR(255) DEFAULT NULL
     */
    private String errorContent;

    /**
     * Relation ...
     */
    private ChargingStation chargingStation;

    /**
     * Get rechargingId
     * 
     * @return rechargingId
     */
    public String getRechargingId() {
        return rechargingId;
    }

    /**
     * Set rechargingId
     * 
     * @param rechargingId
     */
    public void setRechargingId(String rechargingId) {
        this.rechargingId = rechargingId;
    }

    /**
     * Get cpId
     * 
     * @return cpId
     */
    public String getCpId() {
        return cpId;
    }

    /**
     * Set cpId
     * 
     * @param cpId
     */
    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    /**
     * Get csId
     * 
     * @return csId
     */
    public String getCsId() {
        return csId;
    }

    /**
     * Set csId
     * 
     * @param csId
     */
    public void setCsId(String csId) {
        this.csId = csId;
    }

    /**
     * Get evseId
     * 
     * @return evseId
     */
    public int getEvseId() {
        return evseId;
    }

    /**
     * Set evseId
     * 
     * @param evseId
     */
    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    /**
     * Get customerId
     * 
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Set customerId
     * 
     * @param customerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Get cutCardNo
     * 
     * @return cutCardNo
     */
    public String getCutCardNo() {
        return cutCardNo;
    }

    /**
     * Set cutCardNo
     * 
     * @param cutCardNo
     */
    public void setCutCardNo(String cutCardNo) {
        this.cutCardNo = cutCardNo;
    }

    /**
     * Get companyId
     * 
     * @return companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Set companyId
     * 
     * @param companyId
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * Get closedDate
     * 
     * @return closedDate
     */
    public String getClosedDate() {
        return closedDate;
    }

    /**
     * Set closedDate
     * 
     * @param closedDate
     */
    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    /**
     * Get productId
     * 
     * @return productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Set productId
     * 
     * @param productId
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Get chStartDate
     * 
     * @return chStartDate
     */
    public Date getChStartDate() {
        return chStartDate;
    }

    /**
     * Set chStartDate
     * 
     * @param chStartDate
     */
    public void setChStartDate(Date chStartDate) {
        this.chStartDate = chStartDate;
    }

    /**
     * Get chEndDate
     * 
     * @return chEndDate
     */
    public Date getChEndDate() {
        return chEndDate;
    }

    /**
     * Set chEndDate
     * 
     * @param chEndDate
     */
    public void setChEndDate(Date chEndDate) {
        this.chEndDate = chEndDate;
    }

    /**
     * Get chStatCode
     * 
     * @return chStatCode
     */
    public String getChStatCode() {
        return chStatCode;
    }

    /**
     * Set chStatCode
     * 
     * @param chStatCode
     */
    public void setChStatCode(String chStatCode) {
        this.chStatCode = chStatCode;
    }

    /**
     * Get chUseAmount
     * 
     * @return chUseAmount
     */
    public BigDecimal getChUseAmount() {
        return chUseAmount;
    }

    /**
     * Set chUseAmount
     * 
     * @param chUseAmount
     */
    public void setChUseAmount(BigDecimal chUseAmount) {
        this.chUseAmount = chUseAmount;
    }

    /**
     * Get chUseUnitCost
     * 
     * @return chUseUnitCost
     */
    public BigDecimal getChUseUnitCost() {
        return chUseUnitCost;
    }

    /**
     * Set chUseUnitCost
     * 
     * @param chUseUnitCost
     */
    public void setChUseUnitCost(BigDecimal chUseUnitCost) {
        this.chUseUnitCost = chUseUnitCost;
    }

    /**
     * Get chUseCost
     * 
     * @return chUseCost
     */
    public BigDecimal getChUseCost() {
        return chUseCost;
    }

    /**
     * Set chUseCost
     *
     * @param chUseCost
     */
    public void setChUseCost(BigDecimal chUseCost) {
        this.chUseCost = chUseCost;
    }

    /**
     * Get dchUseAmount (방전 전력량)
     *
     * @return dchUseAmount
     */
    public BigDecimal getDchUseAmount() {
        return dchUseAmount;
    }

    /**
     * Set dchUseAmount (방전 전력량)
     *
     * @param dchUseAmount
     */
    public void setDchUseAmount(BigDecimal dchUseAmount) {
        this.dchUseAmount = dchUseAmount;
    }

    /**
     * Get dchUseUnitCost (방전 단가)
     *
     * @return dchUseUnitCost
     */
    public BigDecimal getDchUseUnitCost() {
        return dchUseUnitCost;
    }

    /**
     * Set dchUseUnitCost (방전 단가)
     *
     * @param dchUseUnitCost
     */
    public void setDchUseUnitCost(BigDecimal dchUseUnitCost) {
        this.dchUseUnitCost = dchUseUnitCost;
    }

    /**
     * Get dchUseCost (방전 금액)
     *
     * @return dchUseCost
     */
    public BigDecimal getDchUseCost() {
        return dchUseCost;
    }

    /**
     * Set dchUseCost (방전 금액)
     *
     * @param dchUseCost
     */
    public void setDchUseCost(BigDecimal dchUseCost) {
        this.dchUseCost = dchUseCost;
    }

    /**
     * Get paySum
     *
     * @return paySum
     */
    public Integer getPaySum() {
        return paySum;
    }

    /**
     * Set paySum
     * 
     * @param paySum
     */
    public void setPaySum(Integer paySum) {
        this.paySum = paySum;
    }

    /**
     * Get finalPaySum
     * 
     * @return finalPaySum
     */
    public Integer getFinalPaySum() {
        return finalPaySum;
    }

    /**
     * Set finalPaySum
     * 
     * @param finalPaySum
     */
    public void setFinalPaySum(Integer finalPaySum) {
        this.finalPaySum = finalPaySum;
    }

    /**
     * Get startCaEleEnerge
     * 
     * @return startCaEleEnerge
     */
    public BigDecimal getStartCaEleEnerge() {
        return startCaEleEnerge;
    }

    /**
     * Set startCaEleEnerge
     * 
     * @param startCaEleEnerge
     */
    public void setStartCaEleEnerge(BigDecimal startCaEleEnerge) {
        this.startCaEleEnerge = startCaEleEnerge;
    }

    /**
     * Get endCaEleEnerge
     * 
     * @return endCaEleEnerge
     */
    public BigDecimal getEndCaEleEnerge() {
        return endCaEleEnerge;
    }

    /**
     * Set endCaEleEnerge
     *
     * @param endCaEleEnerge
     */
    public void setEndCaEleEnerge(BigDecimal endCaEleEnerge) {
        this.endCaEleEnerge = endCaEleEnerge;
    }

    /**
     * Get startDaEleEnerge (방전 시작 누적 전력량)
     *
     * @return startDaEleEnerge
     */
    public BigDecimal getStartDaEleEnerge() {
        return startDaEleEnerge;
    }

    /**
     * Set startDaEleEnerge (방전 시작 누적 전력량)
     *
     * @param startDaEleEnerge
     */
    public void setStartDaEleEnerge(BigDecimal startDaEleEnerge) {
        this.startDaEleEnerge = startDaEleEnerge;
    }

    /**
     * Get endDaEleEnerge (방전 종료 누적 전력량)
     *
     * @return endDaEleEnerge
     */
    public BigDecimal getEndDaEleEnerge() {
        return endDaEleEnerge;
    }

    /**
     * Set endDaEleEnerge (방전 종료 누적 전력량)
     *
     * @param endDaEleEnerge
     */
    public void setEndDaEleEnerge(BigDecimal endDaEleEnerge) {
        this.endDaEleEnerge = endDaEleEnerge;
    }

    /**
     * Get maxEnergy (Wh) — OCPP 2.1 TransactionLimitType.maxEnergy
     */
    public Double getMaxEnergy() {
        return maxEnergy;
    }

    /**
     * Set maxEnergy (Wh)
     */
    public void setMaxEnergy(Double maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    /**
     * Get cellphone
     * 
     * @return cellphone
     */
    public String getCellphone() {
        return cellphone;
    }

    /**
     * Set cellphone
     * 
     * @param cellphone
     */
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    /**
     * Get chargingStation
     * 
     * @return chargingStation
     */
    public ChargingStation getChargingStation() {
        return chargingStation;
    }

    /**
     * Set chargingStation
     * 
     * @param chargingStation
     */
    public void setChargingStation(ChargingStation chargingStation) {
        this.chargingStation = chargingStation;
    }

    /**
     * Get errorContent
     * @return errorContent
     */
    public String getErrorContent() {
        return errorContent;
    }

    /**
     * Set errorContent
     * @param errorContent
     */
    public void setErrorContent(String errorContent) {
        this.errorContent = errorContent;
    }

    public Date getPkStartDate() { return pkStartDate; }
    public void setPkStartDate(Date pkStartDate) { this.pkStartDate = pkStartDate; }

    public Date getPkEndDate() { return pkEndDate; }
    public void setPkEndDate(Date pkEndDate) { this.pkEndDate = pkEndDate; }

    public Date getCableStartDate() { return cableStartDate; }
    public void setCableStartDate(Date cableStartDate) { this.cableStartDate = cableStartDate; }

    public Date getCableEndDate() { return cableEndDate; }
    public void setCableEndDate(Date cableEndDate) { this.cableEndDate = cableEndDate; }

}
