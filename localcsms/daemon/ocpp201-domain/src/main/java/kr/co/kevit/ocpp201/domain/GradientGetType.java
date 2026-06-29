/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class GradientGetType {
    //
    private Map<String, Object> customData;


    /**
     * required
     */
    private GradientType gradient;

    /**
     * required
     * Id of setting
     * maxLength : 36
     */
    private String id;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public GradientType getGradient() {
        return gradient;
    }

    public void setGradient(GradientType gradient) {
        this.gradient = gradient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
