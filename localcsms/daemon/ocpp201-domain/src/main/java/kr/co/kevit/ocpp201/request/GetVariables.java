/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.GetVariableDataType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class GetVariables {
    
    /**
     * required
     * "minItems": 1
     */
    private List<GetVariableDataType> getVariableData;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<GetVariableDataType> getGetVariableData() {
        return getVariableData;
    }

    public void setGetVariableData(List<GetVariableDataType> getVariableData) {
        this.getVariableData = getVariableData;
    }

}
