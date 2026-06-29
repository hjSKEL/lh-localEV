/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.IdTokenType;

/**
 * (2.1)
 */
public class RequestBatterySwap {

    /**
     * required
     */
    private int requestId;

    /**
     * required
     */
    private IdTokenType idToken;

    private Map<String, Object> customData;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public IdTokenType getIdToken() {
        return idToken;
    }

    public void setIdToken(IdTokenType idToken) {
        this.idToken = idToken;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
