/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import kr.co.kevit.ocpp201.enumtype.DERControlEnumType;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 11. 18.
 */
public class ClearDERControl {


    /**
     *
     */
    private Map<String, Object> customData;

    /**
     * required
     */
    private boolean isDefault;

    /**
     * Name of control settings to clear. Not used when _controlId_ is provided.
     */
    private DERControlEnumType controlType;


    /**
     * Id of control setting to clear. When omitted all settings for _controlType_ are cleared.
     *  "maxLength": 36
     */
    private String controlId;



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
}
