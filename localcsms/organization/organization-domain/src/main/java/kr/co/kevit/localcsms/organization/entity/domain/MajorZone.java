/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.enumtype.organization.TaskType;

import java.io.Serializable;

/**
 * TB_ORZO001
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 20.
 */
public class MajorZone implements Serializable{
    
    /**  */
    private static final long serialVersionUID = 6044512699845392083L;
    
    /**
     * TASK_TP CHAR(2)
     */
    private TaskType taskType;
    
    /**
     * SIDO CHAR(5)
     */
    private String sidoCode;
    
    /**
     * 회사ID
     * CO_ID       CHAR(9)                 NOT NULL,
     */
    private String companyId;
    
    /**
     * 
     */
    private Writer writer;

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

    /**
     * Get writer
     * @return writer
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * Set writer
     * @param writer
     */
    public void setWriter(Writer writer) {
        this.writer = writer;
    }
    
}
