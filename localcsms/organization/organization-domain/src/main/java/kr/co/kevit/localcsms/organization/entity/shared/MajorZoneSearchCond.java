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
public class MajorZoneSearchCond extends PageCriteria{
        
    private TaskType taskType;
    
    private String sidoCode;
    
    private String companyId;

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

}
