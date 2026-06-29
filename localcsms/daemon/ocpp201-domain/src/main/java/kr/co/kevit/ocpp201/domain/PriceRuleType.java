/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 23.
 */
public class PriceRuleType {
    //
    private Map<String, Object> customData;

    /**
     * The duration of the parking fee period (in seconds).
     * When the time enters into a ParkingFeePeriod, the ParkingFee will apply to the session.
     */
    private Integer parkingFeePeriod;

    /**
     * Number of grams of CO2 per kWh.
     * minimum": 0.0
     */
    private Integer carbonDioxideEmission;

    /**
     * Percentage of the power that is created by renewable resources.
     * minimum": 0.0
     */
    private Integer renewableGenerationPercentage;

    /**
     * required
     */
    private  RationalNumberType energyFee;

    private  RationalNumberType parkingFee;

    /**
     * required
     */
    private  RationalNumberType powerRangeStart;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getParkingFeePeriod() {
        return parkingFeePeriod;
    }

    public void setParkingFeePeriod(Integer parkingFeePeriod) {
        this.parkingFeePeriod = parkingFeePeriod;
    }

    public Integer getCarbonDioxideEmission() {
        return carbonDioxideEmission;
    }

    public void setCarbonDioxideEmission(Integer carbonDioxideEmission) {
        this.carbonDioxideEmission = carbonDioxideEmission;
    }

    public Integer getRenewableGenerationPercentage() {
        return renewableGenerationPercentage;
    }

    public void setRenewableGenerationPercentage(Integer renewableGenerationPercentage) {
        this.renewableGenerationPercentage = renewableGenerationPercentage;
    }

    public RationalNumberType getEnergyFee() {
        return energyFee;
    }

    public void setEnergyFee(RationalNumberType energyFee) {
        this.energyFee = energyFee;
    }

    public RationalNumberType getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(RationalNumberType parkingFee) {
        this.parkingFee = parkingFee;
    }

    public RationalNumberType getPowerRangeStart() {
        return powerRangeStart;
    }

    public void setPowerRangeStart(RationalNumberType powerRangeStart) {
        this.powerRangeStart = powerRangeStart;
    }
}
