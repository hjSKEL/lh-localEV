/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.organization.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.enumtype.organization.TaskType;

import java.io.Serializable;

/**
 * TB_ORZO002
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 20.
 */
public class MinorZone implements Serializable{

    /**  */
    private static final long serialVersionUID = 3767460399668478929L;
    
    /**
     * TASK_TP CHAR(2)
     */
    private TaskType taskType;
    
    /**
     * SIDO CHAR(5)
     */
    private String sidoCode;
    
    /**
     * SIGU CHAR(5)
     */
    private String siguCode;
    
    /**
     * 직원아이디
     * EMP_ID       CHAR(9)                 NOT NULL,
     */
    private String employeeId;

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
