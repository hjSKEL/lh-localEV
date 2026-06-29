/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import kr.co.kevit.ocpp201.domain.*;

import java.util.List;
import java.util.Map;

/**
 * (2.1)
 */
public class GetPeriodicEventStream {

    private List<ConstantStreamDataType> constantStreamData;

    private Map<String, Object> customData;

    public List<ConstantStreamDataType> getConstantStreamData() {
        return constantStreamData;
    }

    public void setConstantStreamData(List<ConstantStreamDataType> constantStreamData) {
        this.constantStreamData = constantStreamData;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
