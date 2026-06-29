/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.SetVariableResultType;

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
    private List<SetVariableResultType> setVariableResult;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<SetVariableResultType> getSetVariableResult() {
        return setVariableResult;
    }

    public void setSetVariableResult(List<SetVariableResultType> setVariableResult) {
        this.setVariableResult = setVariableResult;
    }

}
