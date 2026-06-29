/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.AttributeEnumType;
import kr.co.kevit.ocpp201.enumtype.GetVariableStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class GetVariableResultType {
    
    /**
     * required
     */
    private GetVariableStatusEnumType attributeStatus;
    
    private StatusInfoType attributeStatusInfo;
    
    private AttributeEnumType attributeType;
    
    /**
     * "type": "string","maxLength": 2500
     */
    private String attributeValue;
    
    /**
     * required
     */
    private ComponentType component;
    
    /**
     * required
     */
    private VariableType variable;
    
    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    /**
     * Get attributeStatus
     * @return attributeStatus
     */
    public GetVariableStatusEnumType getAttributeStatus() {
        return attributeStatus;
    }

    /**
     * Set attributeStatus
     * @param attributeStatus
     */
    public void setAttributeStatus(GetVariableStatusEnumType attributeStatus) {
        this.attributeStatus = attributeStatus;
    }

    /**
     * Get attributeStatusInfo
     * @return attributeStatusInfo
     */
    public StatusInfoType getAttributeStatusInfo() {
        return attributeStatusInfo;
    }

    /**
     * Set attributeStatusInfo
     * @param attributeStatusInfo
     */
    public void setAttributeStatusInfo(StatusInfoType attributeStatusInfo) {
        this.attributeStatusInfo = attributeStatusInfo;
    }

    /**
     * Get attributeType
     * @return attributeType
     */
    public AttributeEnumType getAttributeType() {
        return attributeType;
    }

    /**
     * Set attributeType
     * @param attributeType
     */
    public void setAttributeType(AttributeEnumType attributeType) {
        this.attributeType = attributeType;
    }

    /**
     * Get attributeValue
     * @return attributeValue
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * Set attributeValue
     * @param attributeValue
     */
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    /**
     * Get component
     * @return component
     */
    public ComponentType getComponent() {
        return component;
    }

    /**
     * Set component
     * @param component
     */
    public void setComponent(ComponentType component) {
        this.component = component;
    }

    /**
     * Get variable
     * @return variable
     */
    public VariableType getVariable() {
        return variable;
    }

    /**
     * Set variable
     * @param variable
     */
    public void setVariable(VariableType variable) {
        this.variable = variable;
    }
}
