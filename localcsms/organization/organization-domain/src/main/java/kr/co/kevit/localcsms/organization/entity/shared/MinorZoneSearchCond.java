/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.shared;

import kr.co.kevit.localcsms.common.util.enumtype.organization.TaskType;
import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 20.
 */
public class MinorZoneSearchCond extends PageCriteria{
    
    /**
     */
    private TaskType taskType;
    
    /**
     */
    private String sidoCode;
    
    /**
     */
    private String siguCode;
    
    /**
     * 吏곸썝?꾩씠??
     */
    private String employeeId;
    
    /**
     * ?뚯궗 ID
     */
    private String companyId;
    
    /**
     * 
     */
    private String companyName;
    
    /**
     * 
     */
    private String employeeName;

    /**
     * Get taskType
     * @return taskType
     */
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * Set taskType
     * @param taskType
     */
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    /**
     * Get sidoCode
     * @return sidoCode
     */
    public String getSidoCode() {
        return sidoCode;
    }

    /**
     * Set sidoCode
     * @param sidoCode
     */
    public void setSidoCode(String sidoCode) {
        this.sidoCode = sidoCode;
    }

    /**
     * Get siguCode
     * @return siguCode
     */
    public String getSiguCode() {
        return siguCode;
    }

    /**
     * Set siguCode
     * @param siguCode
     */
    public void setSiguCode(String siguCode) {
        this.siguCode = siguCode;
    }

    /**
     * Get employeeId
     * @return employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Set employeeId
     * @param employeeId
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Get employeeName
     * @return employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Set employeeName
     * @param employeeName
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * Get companyId
     * @return companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Set companyId
     * @param companyId
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * Get companyName
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set companyName
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
}
