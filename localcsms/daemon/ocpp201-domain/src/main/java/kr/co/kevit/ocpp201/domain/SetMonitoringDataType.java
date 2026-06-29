/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.MonitorEnumType;

/**
 * "description": "Class to hold parameters of SetVariableMonitoring request.\r\n",
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SetMonitoringDataType {
    
    /**
     * "description": "An id SHALL only be given to replace an existing monitor. The Charging Station handles the generation of id's for new monitors.\r\n\r\n",
     */
    private Integer id;
    
    /**
     * "description": "Monitor only active when a transaction is ongoing on a component relevant to this transaction. Default = false.\r\n\r\n",
     */
    private boolean transaction = false;
    
    /**
     * required
     * "description": "Value for threshold or delta monitoring.\r\nFor Periodic or PeriodicClockAligned this is the interval in seconds.\r\n\r\n",
     * "type": "number"
     */
    private Integer value;
    
    /**
     * required
     */
    private MonitorEnumType type;
    
    /**
     * required
     * 
     * "description": "The severity that will be assigned to an event that is triggered by this monitor. The severity range is 0-9, with 0 as the highest and 9 as the lowest severity level.\r\n\r\nThe severity levels have the following meaning: +\r\n*0-Danger* +\r\nIndicates lives are potentially in danger. Urgent attention is needed and action should be taken immediately. +\r\n*1-Hardware Failure* +\r\nIndicates that the Charging Station is unable to continue regular operations due to Hardware issues. Action is required. +\r\n*2-System Failure* +\r\nIndicates that the Charging Station is unable to continue regular operations due to software or minor hardware issues. Action is required. +\r\n*3-Critical* +\r\nIndicates a critical error. Action is required. +\r\n*4-Error* +\r\nIndicates a non-urgent error. Action is required. +\r\n*5-Alert* +\r\nIndicates an alert event. Default severity for any type of monitoring event.  +\r\n*6-Warning* +\r\nIndicates a warning event. Action may be required. +\r\n*7-Notice* +\r\nIndicates an unusual event. No immediate action is required. +\r\n*8-Informational* +\r\nIndicates a regular operational event. May be used for reporting, measuring throughput, etc. No action is required. +\r\n*9-Debug* +\r\nIndicates information useful to developers for debugging, not useful during operations.\r\n\r\n",
     */
    private int severity;
    
    /**
     * required
     */
    private ComponentType component;
    
    /**
     * required
     */
    private VariableType variable;

    /**
     * (2.1)
     */
    private PeriodicEventStreamParamsType periodicEventStream;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public MonitorEnumType getType() {
        return type;
    }

    public void setType(MonitorEnumType type) {
        this.type = type;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public boolean isTransaction() {
        return transaction;
    }

    public void setTransaction(boolean transaction) {
        this.transaction = transaction;
    }

    public ComponentType getComponent() {
        return component;
    }

    public void setComponent(ComponentType component) {
        this.component = component;
    }

    public VariableType getVariable() {
        return variable;
    }

    public void setVariable(VariableType variable) {
        this.variable = variable;
    }

    public PeriodicEventStreamParamsType getPeriodicEventStream() {
        return periodicEventStream;
    }

    public void setPeriodicEventStream(PeriodicEventStreamParamsType periodicEventStream) {
        this.periodicEventStream = periodicEventStream;
    }

}
