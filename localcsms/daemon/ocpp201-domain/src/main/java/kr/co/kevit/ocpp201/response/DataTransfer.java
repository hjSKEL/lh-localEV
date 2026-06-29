/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.StatusInfoType;
import kr.co.kevit.ocpp201.enumtype.DataTransferStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class DataTransfer {
    
    /**
     * required
     */
    private DataTransferStatusEnumType status;
    
    /**
     * 
     */
    private StatusInfoType statusInfo;
    
    /**
     * 
     */
    private Object data;

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
    public DataTransferStatusEnumType getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(DataTransferStatusEnumType status) {
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
     * Get data
     * @return data
     */
    public Object getData() {
        return data;
    }

    /**
     * Set data
     * @param data
     */
    public void setData(Object data) {
        this.data = data;
    }
}
