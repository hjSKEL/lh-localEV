/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.BatteryDataType;
import kr.co.kevit.ocpp201.domain.IdTokenType;
import kr.co.kevit.ocpp201.enumtype.BatterySwapEventEnumType;

/**
 * (2.1)
 */
public class BatterySwap {

    /**
     * required
     */
    private List<BatteryDataType> batteryData;

    /**
     * required
     */
    private BatterySwapEventEnumType eventType;

    /**
     * required
     */
    private IdTokenType idToken;

    /**
     * required
     */
    private int requestId;

    private Map<String, Object> customData;

    public List<BatteryDataType> getBatteryData() {
        return batteryData;
    }

    public void setBatteryData(List<BatteryDataType> batteryData) {
        this.batteryData = batteryData;
    }

    public BatterySwapEventEnumType getEventType() {
        return eventType;
    }

    public void setEventType(BatterySwapEventEnumType eventType) {
        this.eventType = eventType;
    }

    public IdTokenType getIdToken() {
        return idToken;
    }

    public void setIdToken(IdTokenType idToken) {
        this.idToken = idToken;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
