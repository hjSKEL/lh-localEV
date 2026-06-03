/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * TB_RCDC001 — 방전 거래 (V2X bidirectional).
 *
 * PK 정책: {@link #dcId} = OCPP transactionId. Recharging.rechargingId 와 항상 동일 값.
 * EVCCID 는 방전 거래에서만 필수 — 차량별 누적 보상금 정산을 위해 보관.
 */
public class Discharging implements Serializable {

    private static final long serialVersionUID = 1L;

    /** PK = OCPP transactionId. DC_ID VARCHAR(36) NOT NULL. */
    private String dcId;

    /** CP_ID CHAR(9) NOT NULL. */
    private String cpId;

    /** CS_ID CHAR(2) NOT NULL. */
    private String csId;

    /** EVSE_ID INT NOT NULL. */
    private int evseId;

    /** 고객ID CUT_ID CHAR(9). */
    private String customerId;

    /** EVCCID — TB_CUEV001 의 PK 와 연결. EVCC_ID VARCHAR(50). */
    private String evccId;

    /**
     * 인증 토큰 종류 — OCPP 2.1 IdTokenEnumType.name() (일반적으로 EVCCID).
     * ID_TAG_TP CHAR(20).
     */
    private String idTagType;

    /** 고객 소속사 ID CO_ID CHAR(9). */
    private String companyId = StringConstants.DEFAULT_COMPANYID;

    /** 마감 날짜 CL_DT CHAR(8). */
    private String closedDate;

    /** 상품코드 PRD_TP VARCHAR(6). */
    private String productId;

    /** 방전 시작 시각 DCH_ST_DT DATETIME. */
    private Date dchStartDate;

    /** 방전 종료 시각 DCH_ED_DT DATETIME. */
    private Date dchEndDate;

    /** 방전상태 — 공통코드 DCSS00. DCH_STAT_CD CHAR(6). */
    private String dchStatCode;

    /** 방전 전력량 (V2X export, kWh) — Energy.Active.Export.Register 누적 차. DCH_US_AMT DECIMAL(11,3). */
    private BigDecimal dchUseAmount = BigDecimal.ZERO;

    /** 방전 단가. DCH_US_CST DECIMAL(11,3). */
    private BigDecimal dchUseUnitCost = BigDecimal.ZERO;

    /** 방전 금액 (양수 = 사업자→고객 보상금). DCH_US_SUM DECIMAL(15,2). */
    private BigDecimal dchUseCost = BigDecimal.ZERO;

    /** 방전 시작 누적 전력량. ST_DA_ELE_NRG DECIMAL(11,3). */
    private BigDecimal startDaEleEnerge = BigDecimal.ZERO;

    /** 방전 종료 누적 전력량. ED_DA_ELE_NRG DECIMAL(11,3). */
    private BigDecimal endDaEleEnerge = BigDecimal.ZERO;

    /** 최대 방전 에너지 한도. MAX_DCH_NRG DOUBLE. */
    private Double maxDischargeEnergy = 0.0;

    /** 등록정보. */
    private Writer writer;

    public String getDcId() { return dcId; }
    public void setDcId(String dcId) { this.dcId = dcId; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public int getEvseId() { return evseId; }
    public void setEvseId(int evseId) { this.evseId = evseId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getEvccId() { return evccId; }
    public void setEvccId(String evccId) { this.evccId = evccId; }

    public String getIdTagType() { return idTagType; }
    public void setIdTagType(String idTagType) { this.idTagType = idTagType; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getClosedDate() { return closedDate; }
    public void setClosedDate(String closedDate) { this.closedDate = closedDate; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public Date getDchStartDate() { return dchStartDate; }
    public void setDchStartDate(Date dchStartDate) { this.dchStartDate = dchStartDate; }

    public Date getDchEndDate() { return dchEndDate; }
    public void setDchEndDate(Date dchEndDate) { this.dchEndDate = dchEndDate; }

    public String getDchStatCode() { return dchStatCode; }
    public void setDchStatCode(String dchStatCode) { this.dchStatCode = dchStatCode; }

    public BigDecimal getDchUseAmount() { return dchUseAmount; }
    public void setDchUseAmount(BigDecimal dchUseAmount) { this.dchUseAmount = dchUseAmount; }

    public BigDecimal getDchUseUnitCost() { return dchUseUnitCost; }
    public void setDchUseUnitCost(BigDecimal dchUseUnitCost) { this.dchUseUnitCost = dchUseUnitCost; }

    public BigDecimal getDchUseCost() { return dchUseCost; }
    public void setDchUseCost(BigDecimal dchUseCost) { this.dchUseCost = dchUseCost; }

    public BigDecimal getStartDaEleEnerge() { return startDaEleEnerge; }
    public void setStartDaEleEnerge(BigDecimal startDaEleEnerge) { this.startDaEleEnerge = startDaEleEnerge; }

    public BigDecimal getEndDaEleEnerge() { return endDaEleEnerge; }
    public void setEndDaEleEnerge(BigDecimal endDaEleEnerge) { this.endDaEleEnerge = endDaEleEnerge; }

    public Double getMaxDischargeEnergy() { return maxDischargeEnergy; }
    public void setMaxDischargeEnergy(Double maxDischargeEnergy) { this.maxDischargeEnergy = maxDischargeEnergy; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }
}
