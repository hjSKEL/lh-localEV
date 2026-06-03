/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import kr.co.kevit.localcsms.recharger.entity.domain.Discharging;

/**
 * 화면 표시용 확장 DTO — Discharging + 조인된 명칭/포맷 필드.
 */
public class DischargingDto extends Discharging {

    private static final long serialVersionUID = 1L;

    private String cpName;
    private String dchStartDateStr;
    private String dchEndDateStr;

    /* 고객 정보 */
    private String companyName;
    private String custName;
    private String mblPhoneNo;

    /* 차량 정보 (EVCCID 조인) */
    private String carName;
    private String carNo;

    public String getCpName() { return cpName; }
    public void setCpName(String cpName) { this.cpName = cpName; }

    public String getDchStartDateStr() { return dchStartDateStr; }
    public void setDchStartDateStr(String dchStartDateStr) { this.dchStartDateStr = dchStartDateStr; }

    public String getDchEndDateStr() { return dchEndDateStr; }
    public void setDchEndDateStr(String dchEndDateStr) { this.dchEndDateStr = dchEndDateStr; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCustName() { return custName; }
    public void setCustName(String custName) { this.custName = custName; }

    public String getMblPhoneNo() { return mblPhoneNo; }
    public void setMblPhoneNo(String mblPhoneNo) { this.mblPhoneNo = mblPhoneNo; }

    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }

    public String getCarNo() { return carNo; }
    public void setCarNo(String carNo) { this.carNo = carNo; }
}
