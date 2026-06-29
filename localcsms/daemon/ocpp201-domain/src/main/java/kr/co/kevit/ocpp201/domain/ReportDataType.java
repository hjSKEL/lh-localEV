/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class ReportDataType {
    
    /**
     * required
     */
    private ComponentType component;
    
    /**
     * required
     */
    private VariableType variable;
    
    /**
     * required
     * "minItems": 1,"maxItems": 4
     */
    private List<VariableAttributeType> variableAttribute;
    
    private VariableCharacteristicsType variableCharacteristics;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
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

    public List<VariableAttributeType> getVariableAttribute() {
        return variableAttribute;
    }

    public void setVariableAttribute(List<VariableAttributeType> variableAttribute) {
        this.variableAttribute = variableAttribute;
    }

    public VariableCharacteristicsType getVariableCharacteristics() {
        return variableCharacteristics;
    }

    public void setVariableCharacteristics(VariableCharacteristicsType variableCharacteristics) {
        this.variableCharacteristics = variableCharacteristics;
    }

}