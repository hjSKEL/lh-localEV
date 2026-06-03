/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 배터리 교환형 충전기 확장 정보
 * TB_BSCS001 (ChargingStation 의 1:1 확장)
 *
 * ChargingStation.csServiceType == 'CSST02'(BatterySwap) 인 충전기에만 종속.
 * ChargingStation 의 중첩 객체로 매핑되며, 일반 충전기(CSST01)에서는 null 이다.
 * 식별(cpId/csId)·사용여부·등록정보 등 공통 속성은 기반 테이블 TB_CHCS001 이 보유한다.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 6. 4.
 */
public class BatterySwapInfo implements Serializable {

    /** UID */
    private static final long serialVersionUID = 8810471320824612008L;

    /**
     * 운영상태
     * 공통코드 : BSOS00 (Operative / Inoperative / Maintenance)
     * OPER_STAT VARCHAR(15) DEFAULT 'Operative'
     */
    private String operationalStatus = "Operative";

    /**
     * 물리 슬롯 총 개수 (= OCPP evseId 개수)
     * TOT_SLT_CNT INT DEFAULT 0
     */
    private int totalSlotCount;

    /**
     * 정비/예약으로 잠긴 슬롯 수
     * RSVD_SLT_CNT INT DEFAULT 0
     */
    private int reservedSlotCount = 0;

    /**
     * 지원 배터리 모델(브랜드/규격)
     * BAT_MODEL VARCHAR(60)
     */
    private String supportedBatteryModel;

    /**
     * 기본 IdToken — OCPP BatterySwapCtrlr.Idtoken
     * DFT_ID_TOKEN VARCHAR(255)
     */
    private String defaultIdToken;

    /**
     * 기본 IdToken 유형 (OCPP IdTokenEnumStringType)
     * DFT_ID_TOKEN_TP VARCHAR(20)
     */
    private String defaultIdTokenType;

    /**
     * BatteryOut 미수신 타임아웃(초) — OCPP BatterySwapCtrlr 미러
     * SWAP_TMO_SEC INT DEFAULT 600
     */
    private int swapTimeoutSec = 600;

    /**
     * 회수 임계 SoH(%)
     * MIN_SOH DECIMAL(5,2)
     */
    private BigDecimal minSoHThreshold;

    /**
     * 출고 가능 최소 SoC(%)
     * MIN_SOC DECIMAL(5,2)
     */
    private BigDecimal minSoCThreshold;

    /**
     * Get operationalStatus
     *
     * @return operationalStatus
     */
    public String getOperationalStatus() {
        return operationalStatus;
    }

    /**
     * Set operationalStatus
     *
     * @param operationalStatus
     */
    public void setOperationalStatus(String operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    /**
     * Get totalSlotCount
     *
     * @return totalSlotCount
     */
    public int getTotalSlotCount() {
        return totalSlotCount;
    }

    /**
     * Set totalSlotCount
     *
     * @param totalSlotCount
     */
    public void setTotalSlotCount(int totalSlotCount) {
        this.totalSlotCount = totalSlotCount;
    }

    /**
     * Get reservedSlotCount
     *
     * @return reservedSlotCount
     */
    public int getReservedSlotCount() {
        return reservedSlotCount;
    }

    /**
     * Set reservedSlotCount
     *
     * @param reservedSlotCount
     */
    public void setReservedSlotCount(int reservedSlotCount) {
        this.reservedSlotCount = reservedSlotCount;
    }

    /**
     * Get supportedBatteryModel
     *
     * @return supportedBatteryModel
     */
    public String getSupportedBatteryModel() {
        return supportedBatteryModel;
    }

    /**
     * Set supportedBatteryModel
     *
     * @param supportedBatteryModel
     */
    public void setSupportedBatteryModel(String supportedBatteryModel) {
        this.supportedBatteryModel = supportedBatteryModel;
    }

    /**
     * Get defaultIdToken
     *
     * @return defaultIdToken
     */
    public String getDefaultIdToken() {
        return defaultIdToken;
    }

    /**
     * Set defaultIdToken
     *
     * @param defaultIdToken
     */
    public void setDefaultIdToken(String defaultIdToken) {
        this.defaultIdToken = defaultIdToken;
    }

    /**
     * Get defaultIdTokenType
     *
     * @return defaultIdTokenType
     */
    public String getDefaultIdTokenType() {
        return defaultIdTokenType;
    }

    /**
     * Set defaultIdTokenType
     *
     * @param defaultIdTokenType
     */
    public void setDefaultIdTokenType(String defaultIdTokenType) {
        this.defaultIdTokenType = defaultIdTokenType;
    }

    /**
     * Get swapTimeoutSec
     *
     * @return swapTimeoutSec
     */
    public int getSwapTimeoutSec() {
        return swapTimeoutSec;
    }

    /**
     * Set swapTimeoutSec
     *
     * @param swapTimeoutSec
     */
    public void setSwapTimeoutSec(int swapTimeoutSec) {
        this.swapTimeoutSec = swapTimeoutSec;
    }

    /**
     * Get minSoHThreshold
     *
     * @return minSoHThreshold
     */
    public BigDecimal getMinSoHThreshold() {
        return minSoHThreshold;
    }

    /**
     * Set minSoHThreshold
     *
     * @param minSoHThreshold
     */
    public void setMinSoHThreshold(BigDecimal minSoHThreshold) {
        this.minSoHThreshold = minSoHThreshold;
    }

    /**
     * Get minSoCThreshold
     *
     * @return minSoCThreshold
     */
    public BigDecimal getMinSoCThreshold() {
        return minSoCThreshold;
    }

    /**
     * Set minSoCThreshold
     *
     * @param minSoCThreshold
     */
    public void setMinSoCThreshold(BigDecimal minSoCThreshold) {
        this.minSoCThreshold = minSoCThreshold;
    }

}
