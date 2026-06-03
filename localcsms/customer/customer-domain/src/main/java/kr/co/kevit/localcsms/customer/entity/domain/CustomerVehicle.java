/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.domain;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 고객 차량 (EVCCID 관리)
 *
 * TB : TB_CUEV001
 *
 * <p>한 고객(TB_CUCU001)이 여러 EV 를 보유할 수 있도록 1:N 관계로 분리.
 * ISO 15118 PnC / V2X 매칭 기준 단위. EVCCID 자체를 PK 로 사용한다.</p>
 *
 * @author bckim
 */
public class CustomerVehicle implements Serializable {

    /** UID */
    private static final long serialVersionUID = 1L;

    /**
     * PK — ISO 15118 EVCCID (EV Communication Controller ID, MAC 기반 16~50자)
     * EVCC_ID VARCHAR(50) NOT NULL
     */
    private String evccId;

    /**
     * 고객 아이디 (TB_CUCU001.CUT_ID)
     * CUT_ID CHAR(9) NOT NULL
     */
    private String customerId;

    /**
     * 차대번호 (Vehicle Identification Number, ISO 3779, 17자)
     * VIN_NO VARCHAR(17)
     */
    private String vinNo;

    /**
     * 차량 모델 코드 (공통코드 CARM00 등)
     * CAR_MODEL_ID CHAR(6)
     */
    private String carModelId;

    /**
     * 차량명 (사용자 입력)
     * CAR_NM VARCHAR(60)
     */
    private String carName;

    /**
     * 차량 번호판 (예: 12가3456)
     * CAR_NO VARCHAR(50)
     */
    private String carNo;

    /**
     * V2X 가능 여부 Y/N (양방향 충/방전 지원 차량)
     * V2X_YN CHAR(1) DEFAULT 'N'
     */
    private String v2xYn = StringConstants.N;

    /**
     * 누적 방전 보상금 (V2G) — 보상 정책: 누적 적립
     * ACC_DCH_REWARD DECIMAL(15,2) DEFAULT 0
     */
    private java.math.BigDecimal accDchReward = java.math.BigDecimal.ZERO;

    /** 등록정보 */
    private Writer writer;

    public String getEvccId() {
        return evccId;
    }

    public void setEvccId(String evccId) {
        this.evccId = evccId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVinNo() {
        return vinNo;
    }

    public void setVinNo(String vinNo) {
        this.vinNo = vinNo;
    }

    public String getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(String carModelId) {
        this.carModelId = carModelId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getV2xYn() {
        return v2xYn;
    }

    public void setV2xYn(String v2xYn) {
        this.v2xYn = v2xYn;
    }

    public java.math.BigDecimal getAccDchReward() {
        return accDchReward;
    }

    public void setAccDchReward(java.math.BigDecimal accDchReward) {
        this.accDchReward = accDchReward;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }
}
