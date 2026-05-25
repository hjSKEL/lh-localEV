/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity.shared;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

public class DerControlSearchCond extends PageCriteria implements Serializable {

    private static final long serialVersionUID = 5302026001801260012L;

    private String controlId;
    private String originCd;
    private String cpId;
    private String csId;
    private String isDefault;
    private String controlType;
    private String statusCd;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toDate;

    public String getControlId() { return controlId; }
    public void setControlId(String controlId) { this.controlId = controlId; }
    public String getOriginCd() { return originCd; }
    public void setOriginCd(String originCd) { this.originCd = originCd; }
    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }
    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }
    public String getIsDefault() { return isDefault; }
    public void setIsDefault(String isDefault) { this.isDefault = isDefault; }
    public String getControlType() { return controlType; }
    public void setControlType(String controlType) { this.controlType = controlType; }
    public String getStatusCd() { return statusCd; }
    public void setStatusCd(String statusCd) { this.statusCd = statusCd; }
    public Date getFromDate() { return fromDate; }
    public void setFromDate(Date fromDate) { this.fromDate = fromDate; }
    public Date getToDate() { return toDate; }
    public void setToDate(Date toDate) { this.toDate = toDate; }
}
