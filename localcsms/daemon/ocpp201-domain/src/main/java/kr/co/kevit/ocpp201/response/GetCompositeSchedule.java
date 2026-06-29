/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.CompositeScheduleType;
import kr.co.kevit.ocpp201.domain.StatusInfoType;
import kr.co.kevit.ocpp201.enumtype.GenericStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class GetCompositeSchedule {
    
    /**
     * required
     */
    private GenericStatusEnumType status;
    
    private StatusInfoType statusInfo;
    
    private CompositeScheduleType schedule;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    /**
     * Get status
     * @return status
     */
    public GenericStatusEnumType getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(GenericStatusEnumType status) {
        this.status = status;
    }

    /**
     * Get statusInfo
     * @return statusInfo
     */
    public StatusInfoType getStatusInfo() {
        return statusInfo;
    }

    /**
     * Set statusInfo
     * @param statusInfo
     */
    public void setStatusInfo(StatusInfoType statusInfo) {
        this.statusInfo = statusInfo;
    }

    /**
     * Get schedule
     * @return schedule
     */
    public CompositeScheduleType getSchedule() {
        return schedule;
    }

    /**
     * Set schedule
     * @param schedule
     */
    public void setSchedule(CompositeScheduleType schedule) {
        this.schedule = schedule;
    }

}
