/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.ConstantStreamDataType;

/**
 * (2.1)
 */
public class OpenPeriodicEventStream {

    /**
     * required
     */
    private ConstantStreamDataType constantStreamData;

    private Map<String, Object> customData;

    public ConstantStreamDataType getConstantStreamData() {
        return constantStreamData;
    }

    public void setConstantStreamData(ConstantStreamDataType constantStreamData) {
        this.constantStreamData = constantStreamData;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
