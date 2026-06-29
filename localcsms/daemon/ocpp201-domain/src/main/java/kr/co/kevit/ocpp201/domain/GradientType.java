/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 21.
 */
public class GradientType {
    //
    private Map<String, Object> customData;

    /**
     * required
     * Id of setting
     * "minimum": 0.0
     */
    private double priority;

    /**
     * required
     * Default ramp rate in seconds (0 if not applicable)
     */
    private int gradient;

    /**
     * required
     * Soft-start ramp rate in seconds (0 if not applicable)
     */
    private int softGradient;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public int getGradient() {
        return gradient;
    }

    public void setGradient(int gradient) {
        this.gradient = gradient;
    }

    public int getSoftGradient() {
        return softGradient;
    }

    public void setSoftGradient(int softGradient) {
        this.softGradient = softGradient;
    }
}
