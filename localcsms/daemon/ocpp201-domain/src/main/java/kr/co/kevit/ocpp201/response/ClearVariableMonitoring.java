/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.ClearMonitoringResultType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class ClearVariableMonitoring {
    
    /**
     * required
     * "minItems": 1
     */
    private List<ClearMonitoringResultType> clearMonitoringResult;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<ClearMonitoringResultType> getClearMonitoringResult() {
        return clearMonitoringResult;
    }

    public void setClearMonitoringResult(List<ClearMonitoringResultType> clearMonitoringResult) {
        this.clearMonitoringResult = clearMonitoringResult;
    }

}