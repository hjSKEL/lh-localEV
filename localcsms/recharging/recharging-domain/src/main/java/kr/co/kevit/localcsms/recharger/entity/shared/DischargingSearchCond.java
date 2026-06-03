/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.shared;

import java.util.List;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 방전 거래 검색 조건.
 */
public class DischargingSearchCond extends PageCriteria {

    /** DCSS00 공통코드 */
    private List<String> status;

    private List<String> cpIds;

    private String cpId;
    private String cpName;
    private String csId;

    /** S : Start E : End */
    private String dateType = "S";
    private String fromDate;
    private String toDate;

    private String companyId;
    private String customerId;
    private String evccId;
    private String dcId;

    /** A:START ASC, B:START DESC, C:END ASC, D:END DESC */
    private String dateOrder = "D";

    public List<String> getStatus() { return status; }
    public void setStatus(List<String> status) { this.status = status; }

    public List<String> getCpIds() { return cpIds; }
    public void setCpIds(List<String> cpIds) { this.cpIds = cpIds; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCpName() { return cpName; }
    public void setCpName(String cpName) { this.cpName = cpName; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getDateType() { return dateType; }
    public void setDateType(String dateType) { this.dateType = dateType; }

    public String getFromDate() { return fromDate; }
    public void setFromDate(String fromDate) { this.fromDate = fromDate; }

    public String getToDate() { return toDate; }
    public void setToDate(String toDate) { this.toDate = toDate; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getEvccId() { return evccId; }
    public void setEvccId(String evccId) { this.evccId = evccId; }

    public String getDcId() { return dcId; }
    public void setDcId(String dcId) { this.dcId = dcId; }

    public String getDateOrder() { return dateOrder; }
    public void setDateOrder(String dateOrder) { this.dateOrder = dateOrder; }
}
