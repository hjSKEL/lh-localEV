/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

public class TariffAssignmentSearchCond extends PageCriteria implements Serializable {

    private static final long serialVersionUID = 5301025001801251214L;

    private String tariffId;
    private String assignType;
    private String cpId;
    private String csId;
    private Integer evseId;
    private String customerId;
    private String statusCd;

    public String getTariffId() { return tariffId; }
    public void setTariffId(String tariffId) { this.tariffId = tariffId; }
    public String getAssignType() { return assignType; }
    public void setAssignType(String assignType) { this.assignType = assignType; }
    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }
    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }
    public Integer getEvseId() { return evseId; }
    public void setEvseId(Integer evseId) { this.evseId = evseId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getStatusCd() { return statusCd; }
    public void setStatusCd(String statusCd) { this.statusCd = statusCd; }
}
