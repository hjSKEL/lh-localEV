/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 3. 18.
 */
public class CustomerCertSearchCond extends PageCriteria{
    
    /**
     * 사용자아이디
     * CUT_ID        CHAR(9 BYTE)               NOT NULL,
     */
    private String customerId;
    
    /**
     * 고객명
     * CUT_NM      VARCHAR2(20 BYTE)               NOT NULL,
     */
    private String custName;

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

    /** eMaid */
    private String eMaid;

    /** EV PCID */
    private String pcid;

    /** 인증서 상태 (CERT01~CERT05) */
    private String status;

    public String geteMaid() { return eMaid; }
    public void seteMaid(String eMaid) { this.eMaid = eMaid; }

    public String getPcid() { return pcid; }
    public void setPcid(String pcid) { this.pcid = pcid; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
