/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import kr.co.kevit.ocpp201.enumtype.DERControlEnumType;
import kr.co.kevit.ocpp201.enumtype.GridEventFaultEnumType;

import java.util.Map;

/**
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class GetDERControl {
    //
    private Map<String, Object> customData;

    /**
     * required
     */
    private boolean isDefault;

    /**
     */
    private DERControlEnumType controlType;


    /**
     * Id of setting to get. When omitted all settings for _controlType_ are retrieved.
     */
    private String controlId;

    /**
     * required (2.1)
     */
    private int requestId;


    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public DERControlEnumType getControlType() {
        return controlType;
    }

    public void setControlType(DERControlEnumType controlType) {
        this.controlType = controlType;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
