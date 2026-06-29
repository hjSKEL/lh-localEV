/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.FirmwareType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class UpdateFirmware {
    
    /**
     * "description": "This specifies how many times Charging Station must try to download the firmware before giving up. If this field is not present, it is left to Charging Station to decide how many times it wants to retry.\r\n",
     */
    private Integer retries;
    
    /**
     * "description": "The interval in seconds after which a retry may be attempted. If this field is not present, it is left to Charging Station to decide how long to wait between attempts.\r\n",
     */
    private Integer retryInterval;
    
    /**
     * required
     * "description": "The Id of this request\r\n",
     */
    private int requestId;
    
    /**
     * required
     */
    private FirmwareType firmware;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public FirmwareType getFirmware() {
        return firmware;
    }

    public void setFirmware(FirmwareType firmware) {
        this.firmware = firmware;
    }
}
