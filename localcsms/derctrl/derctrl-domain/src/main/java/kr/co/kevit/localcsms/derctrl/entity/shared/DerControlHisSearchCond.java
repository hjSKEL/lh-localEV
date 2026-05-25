/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity.shared;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

public class DerControlHisSearchCond extends PageCriteria implements Serializable {

    private static final long serialVersionUID = 5302026001801260014L;

    private String controlId;
    private String actionCd;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toDate;

    public String getControlId() { return controlId; }
    public void setControlId(String controlId) { this.controlId = controlId; }
    public String getActionCd() { return actionCd; }
    public void setActionCd(String actionCd) { this.actionCd = actionCd; }
    public Date getFromDate() { return fromDate; }
    public void setFromDate(Date fromDate) { this.fromDate = fromDate; }
    public Date getToDate() { return toDate; }
    public void setToDate(Date toDate) { this.toDate = toDate; }
}
