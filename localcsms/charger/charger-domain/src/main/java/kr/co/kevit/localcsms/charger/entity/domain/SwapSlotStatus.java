/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * 배터리 슬롯 상태 (현재 스냅샷)
 *
 * TB : TB_BSSL001
 *
 * 1행 = 1 슬롯 = (cpId, csId, evseId).
 * 표출(운영자 화면) 이 매번 읽는 1차 데이터. OCPP 이벤트로 갱신되며 변경 시 SwapSlotStatusHis 에 이력 적재.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class SwapSlotStatus implements Serializable {

    /** UID */
    private static final long serialVersionUID = 7720418842309441208L;

    /**
     * PK part 1 — 충전소ID
     * CP_ID   CHAR(6 BYTE)   NOT NULL,
     */
    private String cpId;

    /**
     * PK part 2 — 충전기ID
     * CS_ID   CHAR(2 BYTE)   NOT NULL,
     */
    private String csId;

    /**
     * PK part 3 — 슬롯번호 (OCPP BatteryData.evseId)
     * 1..totalSlotCount
     * EVSE_ID   INT   NOT NULL,
     */
    private int evseId;

    /**
     * 슬롯 통합 상태 — 공통코드 BSSS00
     * EMPTY / OCCUPIED_IDLE / CHARGING / READY / RESERVED / FAULT / MAINTENANCE
     * SLOT_ST_CD   VARCHAR(20 BYTE)   NOT NULL,
     */
    private String slotState;

    /**
     * OCPP StatusNotification 원본값 (디버깅용)
     * Occupied / Available / Reserved / Unavailable / Faulted
     * OCPP_CONN_ST   VARCHAR(15 BYTE),
     */
    private String ocppConnectorStatus;

    // ── 적재 배터리 ──────────────────────────────────────────────

    /**
     * 적재 배터리 시리얼 번호 — NULL = 비어있음
     * BAT_SN   VARCHAR(50 BYTE),
     */
    private String batterySerialNo;

    /**
     * 현재 State of Charge (0.0 ~ 100.0)
     * CUR_SOC   DECIMAL(5,2),
     */
    private BigDecimal currentSoC;

    /**
     * 현재 State of Health (0.0 ~ 100.0)
     * CUR_SOH   DECIMAL(5,2),
     */
    private BigDecimal currentSoH;

    /**
     * 배터리 생산일 (캐시)
     * PROD_DT   DATETIME,
     */
    private Date productionDate;

    /**
     * 벤더 정보 (캐시)
     * VENDOR_INFO   VARCHAR(500 BYTE),
     */
    private String vendorInfo;

    // ── 충전 진행 ──────────────────────────────────────────────

    /**
     * 자체 충전 시작 시각
     * CH_ST_DT   DATETIME,
     */
    private Date chargingStartDate;

    /**
     * 예상 완충 시각 (계산값)
     * EST_RDY_DT   DATETIME,
     */
    private Date estimatedReadyDate;

    // ── 점유/예약 ──────────────────────────────────────────────

    /**
     * RequestBatterySwap Accepted 시 잠금된 requestId
     * RSV_REQ_ID   BIGINT,
     */
    private Long reservedRequestId;

    /**
     * 잠금 해제 예정 시각 (now + station.swapTimeoutSec)
     * RSV_UNTIL_DT   DATETIME,
     */
    private Date reservedUntil;

    // ── 장애 ──────────────────────────────────────────────

    /**
     * 장애 코드 (OCPP 또는 자체)
     * FLT_CD   VARCHAR(20 BYTE),
     */
    private String faultCode;

    /**
     * 장애 발생 시각
     * FLT_SINCE_DT   DATETIME,
     */
    private Date faultedSince;

    // ── 갱신 추적 ──────────────────────────────────────────────

    /**
     * 마지막 트리거 유형
     * BatteryIn / BatteryOut / StatusNotification / Manual / Timeout 등
     * LAST_EVT_TP   VARCHAR(20 BYTE),
     */
    private String lastEventType;

    /**
     * 마지막 갱신 시각
     * LAST_EVT_DT   DATETIME,
     */
    private Date lastEventDate;

    /**
     * 등록정보
     */
    private Writer writer;

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

    public int getEvseId() {
        return evseId;
    }

    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    public String getSlotState() {
        return slotState;
    }

    public void setSlotState(String slotState) {
        this.slotState = slotState;
    }

    public String getOcppConnectorStatus() {
        return ocppConnectorStatus;
    }

    public void setOcppConnectorStatus(String ocppConnectorStatus) {
        this.ocppConnectorStatus = ocppConnectorStatus;
    }

    public String getBatterySerialNo() {
        return batterySerialNo;
    }

    public void setBatterySerialNo(String batterySerialNo) {
        this.batterySerialNo = batterySerialNo;
    }

    public BigDecimal getCurrentSoC() {
        return currentSoC;
    }

    public void setCurrentSoC(BigDecimal currentSoC) {
        this.currentSoC = currentSoC;
    }

    public BigDecimal getCurrentSoH() {
        return currentSoH;
    }

    public void setCurrentSoH(BigDecimal currentSoH) {
        this.currentSoH = currentSoH;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public String getVendorInfo() {
        return vendorInfo;
    }

    public void setVendorInfo(String vendorInfo) {
        this.vendorInfo = vendorInfo;
    }

    public Date getChargingStartDate() {
        return chargingStartDate;
    }

    public void setChargingStartDate(Date chargingStartDate) {
        this.chargingStartDate = chargingStartDate;
    }

    public Date getEstimatedReadyDate() {
        return estimatedReadyDate;
    }

    public void setEstimatedReadyDate(Date estimatedReadyDate) {
        this.estimatedReadyDate = estimatedReadyDate;
    }

    public Long getReservedRequestId() {
        return reservedRequestId;
    }

    public void setReservedRequestId(Long reservedRequestId) {
        this.reservedRequestId = reservedRequestId;
    }

    public Date getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(Date reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    public Date getFaultedSince() {
        return faultedSince;
    }

    public void setFaultedSince(Date faultedSince) {
        this.faultedSince = faultedSince;
    }

    public String getLastEventType() {
        return lastEventType;
    }

    public void setLastEventType(String lastEventType) {
        this.lastEventType = lastEventType;
    }

    public Date getLastEventDate() {
        return lastEventDate;
    }

    public void setLastEventDate(Date lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

}
