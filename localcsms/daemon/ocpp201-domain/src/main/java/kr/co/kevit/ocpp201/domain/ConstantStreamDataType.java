/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * (2.1)
 */
public class ConstantStreamDataType {

    /**
     * required
     */
    private int id;

    /**
     * required
     */
    private int variableMonitoringId;

    /**
     * required
     */
    private PeriodicEventStreamParamsType params;

    private Map<String, Object> customData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVariableMonitoringId() {
        return variableMonitoringId;
    }

    public void setVariableMonitoringId(int variableMonitoringId) {
        this.variableMonitoringId = variableMonitoringId;
    }

    public PeriodicEventStreamParamsType getParams() {
        return params;
    }

    public void setParams(PeriodicEventStreamParamsType params) {
        this.params = params;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}
