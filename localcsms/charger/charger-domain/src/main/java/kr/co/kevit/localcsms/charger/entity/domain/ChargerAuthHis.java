/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation.
 * All rights reserved. This software is the proprietary information of
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

/**
 * 충전기 인증이력 (TB_CHCS006 중 EVENT_CD가 EVT0A1/EVT0A2인 이력 + 조인 정보)
 *
 * @since 2026. 8. 28.
 */
public class ChargerAuthHis extends ChargerStatusInfoHis {

    /**
     * UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 충전소명 (TB_CHCP001 조인 조회 전용, 저장 안 함)
     */
    private String cpName;

    /**
     * 회원번호 (TB_CUCA001 조인 조회 전용, 저장 안 함)
     */
    private String customerId;

    /**
     * 회원명 (TB_CUCU001 조인 조회 전용, 저장 안 함)
     */
    private String custName;

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

}
