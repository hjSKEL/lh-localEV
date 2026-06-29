/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.EventNotificationEnumType;
import kr.co.kevit.ocpp201.enumtype.EventTriggerEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class EventDataType {
    
    /**
     * required
     */
    private int eventId;
    
    /**
     * required
     * "type": "string","format": "date-time"
     */
    private String timestamp;
    
    /**
     * required
     */
    private EventTriggerEnumType trigger;
    
    /**
     * 
     */
    private int cause;
    
    /**
     * required
     * "type": "string","maxLength": 2500
     */
    private String actualValue;
    
    /**
     * "type": "string","maxLength": 50
     */
    private String techCode;
    
    /**
     * "type": "string","maxLength": 500
     */
    private String techInfo;
    
    private boolean cleared;
    
    /**
     * "type": "string","maxLength": 36
     */
    private String transactionId;
    
    /**
     * required
     */
    private ComponentType component;
    
    private int variableMonitoringId;
    
    /**
     * required
     */
    private EventNotificationEnumType eventNotificationType;
    
    /**
     * required
     */
    private VariableType variable;

    /**
     * (2.1)
     */
    private Integer severity;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public EventTriggerEnumType getTrigger() {
        return trigger;
    }

    public void setTrigger(EventTriggerEnumType trigger) {
        this.trigger = trigger;
    }

    public int getCause() {
        return cause;
    }

    public void setCause(int cause) {
        this.cause = cause;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getTechCode() {
        return techCode;
    }

    public void setTechCode(String techCode) {
        this.techCode = techCode;
    }

    public String getTechInfo() {
        return techInfo;
    }

    public void setTechInfo(String techInfo) {
        this.techInfo = techInfo;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public ComponentType getComponent() {
        return component;
    }

    public void setComponent(ComponentType component) {
        this.component = component;
    }

    public int getVariableMonitoringId() {
        return variableMonitoringId;
    }

    public void setVariableMonitoringId(int variableMonitoringId) {
        this.variableMonitoringId = variableMonitoringId;
    }

    public EventNotificationEnumType getEventNotificationType() {
        return eventNotificationType;
    }

    public void setEventNotificationType(EventNotificationEnumType eventNotificationType) {
        this.eventNotificationType = eventNotificationType;
    }

    public VariableType getVariable() {
        return variable;
    }

    public void setVariable(VariableType variable) {
        this.variable = variable;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

}