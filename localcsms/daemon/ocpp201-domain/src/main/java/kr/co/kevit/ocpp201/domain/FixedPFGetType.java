/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class FixedPFGetType {
    //
    private Map<String, Object> customData;

    /**
     * required
     */
    private FixedPFType fixedPF;

    /**
     * required
     * Id of setting
     * maxLength : 36
     */
    private String id;

    /**
     * required
     * rue if setting is a default control.
     */
    private boolean isDefault;

    /**
     * required
     * True if this setting is superseded by a lower priority setting.
     */
    private boolean isSuperseded;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public FixedPFType getFixedPF() {
        return fixedPF;
    }

    public void setFixedPF(FixedPFType fixedPF) {
        this.fixedPF = fixedPF;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isSuperseded() {
        return isSuperseded;
    }

    public void setSuperseded(boolean superseded) {
        isSuperseded = superseded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
