/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.shared;

import java.io.Serializable;
import java.util.List;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 21.
 */
public class EmployeeSearchCond extends PageCriteria implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1566604627705243729L;

    private String emplName;
    
    private String companyId;

    private String companyName;
    
    private List<String> roleTypes;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEmplName() {
        return emplName;
    }

    public void setEmplName(String emplName) {
        this.emplName = emplName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Get roleTypes
     * @return roleTypes
     */
    public List<String> getRoleTypes() {
        return roleTypes;
    }

    /**
     * Set roleTypes
     * @param roleTypes
     */
    public void setRoleTypes(List<String> roleTypes) {
        this.roleTypes = roleTypes;
    }
}
