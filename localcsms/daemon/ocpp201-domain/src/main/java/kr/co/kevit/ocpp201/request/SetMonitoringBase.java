/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.MonitoringBaseEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SetMonitoringBase {
    
    /**
     * required
     */
    private MonitoringBaseEnumType monitoringBase;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public MonitoringBaseEnumType getMonitoringBase() {
        return monitoringBase;
    }

    public void setMonitoringBase(MonitoringBaseEnumType monitoringBase) {
        this.monitoringBase = monitoringBase;
    }

}