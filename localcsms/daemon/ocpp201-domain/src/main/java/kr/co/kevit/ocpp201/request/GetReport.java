/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.ComponentVariableType;
import kr.co.kevit.ocpp201.enumtype.ComponentCriterionEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class GetReport {
    
    /**
     * "minItems": 1
     */
    private List<ComponentVariableType> componentVariable;
    
    /**
     * required
     */
    private Integer requestId;
    
    /**
     * "minItems": 1,"maxItems": 4
     */
    private List<ComponentCriterionEnumType> componentCriteria;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<ComponentVariableType> getComponentVariable() {
        return componentVariable;
    }

    public void setComponentVariable(List<ComponentVariableType> componentVariable) {
        this.componentVariable = componentVariable;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public List<ComponentCriterionEnumType> getComponentCriteria() {
        return componentCriteria;
    }

    public void setComponentCriteria(List<ComponentCriterionEnumType> componentCriteria) {
        this.componentCriteria = componentCriteria;
    }

}