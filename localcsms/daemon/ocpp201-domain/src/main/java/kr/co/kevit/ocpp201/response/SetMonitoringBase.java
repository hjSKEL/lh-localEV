/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.StatusInfoType;
import kr.co.kevit.ocpp201.enumtype.GenericDeviceModelStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SetMonitoringBase {
    
    /**
     * required
     */
    private GenericDeviceModelStatusEnumType status;
    
    private StatusInfoType statusInfo;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public GenericDeviceModelStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(GenericDeviceModelStatusEnumType status) {
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