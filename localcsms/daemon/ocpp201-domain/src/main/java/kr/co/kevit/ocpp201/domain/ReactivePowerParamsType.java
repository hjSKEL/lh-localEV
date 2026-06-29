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
public class ReactivePowerParamsType {
    //
    private Map<String, Object> customData;

    /**
     * Only for VoltVar curve: The nominal ac voltage (rms) adjustment to the voltage curve points for Volt-Var curves (percentage).
     */
    private Double vRef;

    /**
     * Only for VoltVar: Enable/disable autonomous VRef adjustment
     */
    private Boolean autonomousVRefEnable;

    /**
     * Only for VoltVar: Adjustment range for VRef time constant
     */
    private Double autonomousVRefTimeConstant;

    /**
     * Get customData
     * @return customData
     */
    public Map<String, Object> getCustomData() {
        return customData;
    }

    /**
     * Set customData
     * @param customData
     */
    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    /**
     * Get vRef
     * @return vRef
     */
    public Double getvRef() {
        return vRef;
    }

    /**
     * Set vRef
     * @param vRef
     */
    public void setvRef(Double vRef) {
        this.vRef = vRef;
    }

    /**
     * Get autonomousVRefEnable
     * @return autonomousVRefEnable
     */
    public Boolean getAutonomousVRefEnable() {
        return autonomousVRefEnable;
    }

    /**
     * Set autonomousVRefEnable
     * @param autonomousVRefEnable
     */
    public void setAutonomousVRefEnable(Boolean autonomousVRefEnable) {
        this.autonomousVRefEnable = autonomousVRefEnable;
    }

    /**
     * Get autonomousVRefTimeConstant
     * @return autonomousVRefTimeConstant
     */
    public Double getAutonomousVRefTimeConstant() {
        return autonomousVRefTimeConstant;
    }

    /**
     * Set autonomousVRefTimeConstant
     * @param autonomousVRefTimeConstant
     */
    public void setAutonomousVRefTimeConstant(Double autonomousVRefTimeConstant) {
        this.autonomousVRefTimeConstant = autonomousVRefTimeConstant;
    }

}
