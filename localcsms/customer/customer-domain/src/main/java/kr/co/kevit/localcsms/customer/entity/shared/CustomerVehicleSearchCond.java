/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 고객 차량(EVCCID) 검색 조건.
 *
 * @author bckim
 */
public class CustomerVehicleSearchCond extends PageCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    /** EVCCID (부분일치) */
    private String evccId;

    /** 고객 ID */
    private String customerId;

    /** 차량 번호판 (부분일치) */
    private String carNo;

    /** V2X 가능여부 Y/N */
    private String v2xYn;

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
}
