/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.domain.LogParameters;
import kr.co.kevit.ocpp16.enumtype.LogEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class GetLog {
    
    /**
     * required
     */
    private LogParameters log;
    
    /**
     * required
     */
    private LogEnum logType;
    
    /**
     * required
     */
    private Integer requestId;
    
    private Integer retries;
    
    private Integer retryInterval;

    public LogParameters getLog() {
        return log;
    }

    public void setLog(LogParameters log) {
        this.log = log;
    }

    public LogEnum getLogType() {
        return logType;
    }

    public void setLogType(LogEnum logType) {
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
