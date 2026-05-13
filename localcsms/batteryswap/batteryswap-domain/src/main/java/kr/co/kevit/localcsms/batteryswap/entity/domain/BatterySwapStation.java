/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.batteryswap.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 배터리 교환 충전기 (Battery Swap Station)
 *
 * TB : TB_BSCS001
 *
 * 한 BatterySwapPoint(cpId) 에 1..N 으로 종속.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class BatterySwapStation implements Serializable {

    /** UID */
    private static final long serialVersionUID = 8810471320824612008L;

    /**
     * PK
     * 충전소ID (FK -> TB_BSCP001.CP_ID)
     * CP_ID   CHAR(6 BYTE)   NOT NULL,
     */
    private String cpId;

    /**
     * PK
     * 충전기ID
     * CS_ID   CHAR(2 BYTE)   NOT NULL,
     */
    private String csId;

    /**
     * 사용여부 — OCPP BatterySwapCtrlr.Enabled 미러
     * ENABLED   CHAR(1 BYTE)   DEFAULT 'Y',
     */
    private String enabled = StringConstants.Y;

    /**
     * 운영상태
     * 공통코드 : BSOS00 (Operative / Inoperative / Maintenance)
     * OPER_STAT   VARCHAR(15 BYTE)   DEFAULT 'Operative',
     */
    private String operationalStatus;

    /**
     * 물리 슬롯 총 개수 (= OCPP evseId 개수)
     * TOT_SLT_CNT   INT   DEFAULT 0,
     */
    private int totalSlotCount;

    /**
     * 정비/예약으로 잠긴 슬롯 수
     * RSVD_SLT_CNT   INT   DEFAULT 0,
     */
    private int reservedSlotCount = 0;

    /**
     * 지원 배터리 모델(브랜드/규격)
     * BAT_MODEL   VARCHAR(60 BYTE),
     */
    private String supportedBatteryModel;

    /**
     * 기본 IdToken — OCPP BatterySwapCtrlr.Idtoken
     * DFT_ID_TOKEN   VARCHAR(255 BYTE),
     */
    private String defaultIdToken;

    /**
     * 기본 IdToken 유형 (OCPP IdTokenEnumStringType)
     * DFT_ID_TOKEN_TP   VARCHAR(20 BYTE),
     */
    private String defaultIdTokenType;

    /**
     * BatteryOut 미수신 타임아웃(초) — OCPP BatterySwapCtrlr 미러
     * SWAP_TMO_SEC   INT   DEFAULT 600,
     */
    private int swapTimeoutSec = 600;

    /**
     * 회수 임계 SoH(%) — 이 미만이면 자동 PENDING_SWAP 처리
     * MIN_SOH   DECIMAL(5,2),
     */
    private BigDecimal minSoHThreshold;

    /**
     * 출고 가능 최소 SoC(%)
     * MIN_SOC   DECIMAL(5,2),
     */
    private BigDecimal minSoCForDispatch;

    /**
     * 등록정보
     */
    private Writer writer;

    /**
     * Object Relation — 소속 충전소
     */
    private BatterySwapPoint batterySwapPoint;

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

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getOperationalStatus() {
        return operationalStatus;
    }

    public void setOperationalStatus(String operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public int getTotalSlotCount() {
        return totalSlotCount;
    }

    public void setTotalSlotCount(int totalSlotCount) {
        this.totalSlotCount = totalSlotCount;
    }

    public int getReservedSlotCount() {
        return reservedSlotCount;
    }

    public void setReservedSlotCount(int reservedSlotCount) {
        this.reservedSlotCount = reservedSlotCount;
    }

    public String getSupportedBatteryModel() {
        return supportedBatteryModel;
    }

    public void setSupportedBatteryModel(String supportedBatteryModel) {
        this.supportedBatteryModel = supportedBatteryModel;
    }

    public String getDefaultIdToken() {
        return defaultIdToken;
    }

    public void setDefaultIdToken(String defaultIdToken) {
        this.defaultIdToken = defaultIdToken;
    }

    public String getDefaultIdTokenType() {
        return defaultIdTokenType;
    }

    public void setDefaultIdTokenType(String defaultIdTokenType) {
        this.defaultIdTokenType = defaultIdTokenType;
    }

    public int getSwapTimeoutSec() {
        return swapTimeoutSec;
    }

    public void setSwapTimeoutSec(int swapTimeoutSec) {
        this.swapTimeoutSec = swapTimeoutSec;
    }

    public BigDecimal getMinSoHThreshold() {
        return minSoHThreshold;
    }

    public void setMinSoHThreshold(BigDecimal minSoHThreshold) {
        this.minSoHThreshold = minSoHThreshold;
    }

    public BigDecimal getMinSoCForDispatch() {
        return minSoCForDispatch;
    }

    public void setMinSoCForDispatch(BigDecimal minSoCForDispatch) {
        this.minSoCForDispatch = minSoCForDispatch;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public BatterySwapPoint getBatterySwapPoint() {
        return batterySwapPoint;
    }

    public void setBatterySwapPoint(BatterySwapPoint batterySwapPoint) {
        this.batterySwapPoint = batterySwapPoint;
    }

}
