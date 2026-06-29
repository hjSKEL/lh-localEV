/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.SetVariableDataType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SetVariables {
    
    /**
     * required
     * "minItems": 1
     */
    private List<SetVariableDataType> setVariableData;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<SetVariableDataType> getSetVariableData() {
        return setVariableData;
    }

    public void setSetVariableData(List<SetVariableDataType> setVariableData) {
        this.setVariableData = setVariableData;
    }

}