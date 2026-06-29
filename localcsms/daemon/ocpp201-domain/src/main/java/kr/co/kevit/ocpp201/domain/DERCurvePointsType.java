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
public class DERCurvePointsType {
    //
    private Map<String, Object> customData;

    /**
     * The data value of the X-axis (independent) variable, depending on the curve type.
     * required
     */
    private double x;

    /**
     * The data value of the Y-axis (dependent) variable, depending on the <<cmn_derunitenumtype>> of the curve.
     * If _y_ is power factor, then a positive value means DER is absorbing reactive power (under-excited),
     * a negative value when DER is injecting reactive power (over-excited).
     * required
     */
    private double y;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
