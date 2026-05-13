/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * 배터리 교체 기록 디테일 (배터리 1개 = 1행)
 *
 * TB : TB_RCBD001
 *
 * BatterySwapRecord 의 cycle 안에서 슬롯(evseId) 별 1행.
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public class BatterySwapRecordDetail implements Serializable {

    /** UID */
    private static final long serialVersionUID = 6720418821031241127L;

    /**
     * PK part 1 — OCPP requestId (FK -> TB_RCBS001)
     * REQ_ID   BIGINT   NOT NULL,
     */
    private Long requestId;

    /**
     * PK part 2 — 슬롯번호 (OCPP BatteryData.evseId)
     * EVSE_ID   INT   NOT NULL,
     */
    private int evseId;

    /**
     * 유형 — IN / OUT
     * TYPE_CD   VARCHAR(5 BYTE)   NOT NULL,
     */
    private String type;

    /**
     * State of Charge (0.0 ~ 100.0)
     * SOC   DECIMAL(5,2),
     */
    private BigDecimal soc;

    /**
     * State of Health (0.0 ~ 100.0)
     * SOH   DECIMAL(5,2),
     */
    private BigDecimal soh;

    /**
     * 배터리 시리얼번호
     * SR_NO   VARCHAR(50 BYTE),
     */
    private String serialNumber;

    /**
     * 배터리 생산일
     * PROD_DT   DATETIME,
     */
    private Date productionDate;

    /**
     * 등록정보
     */
    private Writer writer;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public int getEvseId() {
        return evseId;
    }

    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getSoc() {
        return soc;
    }

    public void setSoc(BigDecimal soc) {
        this.soc = soc;
    }

    public BigDecimal getSoh() {
        return soh;
    }

    public void setSoh(BigDecimal soh) {
        this.soh = soh;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

}
