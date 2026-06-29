/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.StatusInfoType;
import kr.co.kevit.ocpp201.enumtype.FirmwareStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class FirmwareStatusNotification {
    
    /**
     * required
     */
    private FirmwareStatusEnumType status;
    
    /**
     * 
     */
    private Integer requestId;

    /**
     * (2.1)
     */
    private StatusInfoType statusInfo;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public FirmwareStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(FirmwareStatusEnumType status) {
        this.status = status;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public StatusInfoType getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfoType statusInfo) {
        this.statusInfo = statusInfo;
    }

}
