/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 12.
 */
public class CompanySearchCond extends PageCriteria{
    
    private String companyId;

    private String companyName;

    private String bizRegNo;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBizRegNo() {
        return bizRegNo;
    }

    public void setBizRegNo(String bizRegNo) {
        this.bizRegNo = bizRegNo;
    }

}
