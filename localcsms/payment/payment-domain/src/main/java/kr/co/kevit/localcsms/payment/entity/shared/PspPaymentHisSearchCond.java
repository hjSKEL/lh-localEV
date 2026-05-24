/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.shared;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

public class PspPaymentHisSearchCond extends PageCriteria implements Serializable {

    private static final long serialVersionUID = 5301025001801251104L;

    private String pspRef;
    private String postStatusCd;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date toDate;

    public String getPspRef() { return pspRef; }
    public void setPspRef(String pspRef) { this.pspRef = pspRef; }
    public String getPostStatusCd() { return postStatusCd; }
    public void setPostStatusCd(String postStatusCd) { this.postStatusCd = postStatusCd; }
    public Date getFromDate() { return fromDate; }
    public void setFromDate(Date fromDate) { this.fromDate = fromDate; }
    public Date getToDate() { return toDate; }
    public void setToDate(Date toDate) { this.toDate = toDate; }
}
