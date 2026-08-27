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

    /**
     * OPERATION 역할 조회 제한용(자기 자신만) - 화면 검색조건이 아니라 서비스 레이어가 강제로 채워 넣는 값.
     */
    private String employeeId;

    private String companyId;

    private String companyName;
    
    private List<String> roleTypes;

    /**
     * 정렬 (A/B=직원명 오름/내림, C/D=소속법인 오름/내림, E/F=마지막로그인일 오름/내림, 기본은 REG_DT DESC)
     */
    private String sortOrder;

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
