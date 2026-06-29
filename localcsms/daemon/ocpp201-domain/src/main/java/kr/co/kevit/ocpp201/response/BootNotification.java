/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.StatusInfoType;
import kr.co.kevit.ocpp201.enumtype.RegistrationStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class BootNotification {
    
    /**
     * required
     * "type": "string","format": "date-time"
     */
    private String currentTime;
    
    /**
     * required
     */
    private Integer interval;
    
    /**
     * required
     */
    private RegistrationStatusEnumType status;
    
    /**
     * Option
     */
    private StatusInfoType statusInfo;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public RegistrationStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatusEnumType status) {
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

}
