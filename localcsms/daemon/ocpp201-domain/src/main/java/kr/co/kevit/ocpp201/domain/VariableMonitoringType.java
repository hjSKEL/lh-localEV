/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.EventNotificationEnumType;
import kr.co.kevit.ocpp201.enumtype.MonitorEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class VariableMonitoringType {
    
    /**
     * required
     */
    private int id;
    
    /**
     * required
     * "type": "number"
     */
    private int value;
    
    /**
     * required
     */
    private MonitorEnumType type;
    
    /**
     * required
     * "type": "integer"
     */
    private int severity;
    
    /**
     * required
     * "type": "boolean"
     */
    private boolean transaction;

    /**
     * (2.1)
     */
    private EventNotificationEnumType eventNotificationType;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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

    public EventNotificationEnumType getEventNotificationType() {
        return eventNotificationType;
    }

    public void setEventNotificationType(EventNotificationEnumType eventNotificationType) {
        this.eventNotificationType = eventNotificationType;
    }

}