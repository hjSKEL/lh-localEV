/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * EV DC charging parameters
 * NOTE: This dataType is based on dataTypes from <<ref-ISOIEC15118-2,ISO15118-2>>.
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class DCChargingParametersType {
    
    /**
     * required
     * Maximum current (in A) supported by the electric vehicle. Includes cable capacity.
     * Relates to:
     * *ISO 15118-2*: DC_EVChargeParameterType:EVMaximumCurrentLimit
     * ISO 15118-20*: DC_CPDReqEnergyTransferModeType: EVMaximumChargeCurrent
     */
    private int evMaxCurrent;
    
    /**
     * required
     * Maximum voltage supported by the electric vehicle.
     * Relates to:
     * ISO 15118-2: DC_EVChargeParameterType: EVMaximumVoltageLimit
     * ISO 15118-20: DC_EVChargeParameterType: EVMaximumVoltage
     */
    private int evMaxVoltage;

    /**
     * Amount of energy requested (in Wh). This inludes energy required for preconditioning.
     * Relates to:
     * *ISO 15118-2*: DC_EVChargeParameterType: EVEnergyRequest
     * *ISO 15118-20*: Dynamic/Scheduled_SEReqControlModeType: EVTargetEnergyRequest
     */
    private int energyAmount;

    /**
     * Maximum power (in W) supported by the electric vehicle. Required for DC charging.
     * Relates to:
     * ISO 15118-2 : DC_EVChargeParameterType: EVMaximumPowerLimit
     * ISO15118-20 : DC_CPDReqEnergyTransferModeType: EVMaximumChargePower
     */
    private Integer evMaxPower;
    
    /**
     * Energy available in the battery (in percent of the battery capacity)\r\nRelates to:
     * *ISO 15118-2*: DC_EVChargeParameterType: DC_EVStatus: EVRESSSOC
     * "type": "integer","minimum": 0.0,"maximum": 100.0
     */
    private int stateOfCharge;

    /**
     * Capacity of the electric vehicle battery (in Wh).
     * Relates to:
     * ISO 15118-2: DC_EVChargeParameterType: EVEnergyCapacity
     */
    private int evEnergyCapacity;
    
    /**
     * Percentage of SoC at which the EV considers the battery fully charged. (possible values: 0 - 100)
     * Relates to:
     * *ISO 15118-2*: DC_EVChargeParameterType: FullSOC
     * *ISO 15118-20*: DC_CPDReqEnergyTransferModeType: TargetSOC
     * "type": "integer","minimum": 0.0,"maximum": 100.0
     */
    private int fullSoC;
    
    /**
     * Percentage of SoC at which the EV considers a fast charging process to end. (possible values: 0 - 100)
     * Relates to:
     * *ISO 15118-2*: DC_EVChargeParameterType: BulkSOC
     * "type": "integer","minimum": 0.0,"maximum": 100.0
     */
    private int bulkSoC;

    private Map<String, Object> customData;

    public void setEnergyAmount(int energyAmount) {
        this.energyAmount = energyAmount;
    }

    public void setEvEnergyCapacity(int evEnergyCapacity) {
        this.evEnergyCapacity = evEnergyCapacity;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getEvMaxCurrent() {
        return evMaxCurrent;
    }

    public void setEvMaxCurrent(int evMaxCurrent) {
        this.evMaxCurrent = evMaxCurrent;
    }

    public int getEvMaxVoltage() {
        return evMaxVoltage;
    }

    public void setEvMaxVoltage(int evMaxVoltage) {
        this.evMaxVoltage = evMaxVoltage;
    }

    public Integer getEnergyAmount() {
        return energyAmount;
    }

    public void setEnergyAmount(Integer energyAmount) {
        this.energyAmount = energyAmount;
    }

    public Integer getEvMaxPower() {
        return evMaxPower;
    }

    public void setEvMaxPower(Integer evMaxPower) {
        this.evMaxPower = evMaxPower;
    }

    public int getStateOfCharge() {
        return stateOfCharge;
    }

    public void setStateOfCharge(int stateOfCharge) {
        this.stateOfCharge = stateOfCharge;
    }

    public Integer getEvEnergyCapacity() {
        return evEnergyCapacity;
    }

    public void setEvEnergyCapacity(Integer evEnergyCapacity) {
        this.evEnergyCapacity = evEnergyCapacity;
    }

    public int getFullSoC() {
        return fullSoC;
    }

    public void setFullSoC(int fullSoC) {
        this.fullSoC = fullSoC;
    }

    public int getBulkSoC() {
        return bulkSoC;
    }

    public void setBulkSoC(int bulkSoC) {
        this.bulkSoC = bulkSoC;
    }
}
