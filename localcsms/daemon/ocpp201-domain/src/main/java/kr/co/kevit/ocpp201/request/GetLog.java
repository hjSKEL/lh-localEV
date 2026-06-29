/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.LogParametersType;
import kr.co.kevit.ocpp201.enumtype.LogEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class GetLog {
    
    /**
     * required
     */
    private LogParametersType log;
    
    /**
     * required
     */
    private LogEnumType logType;
    
    /**
     * required
     */
    private Integer requestId;
    
    private Integer retries;
    
    private Integer retryInterval;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public LogParametersType getLog() {
        return log;
    }

    public void setLog(LogParametersType log) {
        this.log = log;
    }

    public LogEnumType getLogType() {
        return logType;
    }

    public void setLogType(LogEnumType logType) {
        this.logType = logType;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
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
    
}
