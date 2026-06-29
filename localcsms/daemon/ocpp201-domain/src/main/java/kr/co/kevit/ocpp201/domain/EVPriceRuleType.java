/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * "*(2.1)* An entry in price schedule over time for which EV is willing to discharge.
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class EVPriceRuleType {
    //
    private Map<String, Object> customData;

    /**
     * required
     * Cost per kWh.
     */
    private int energyFee;

    /**
     * required
     * The EnergyFee applies between this value and the value of the PowerRangeStart of the subsequent EVPriceRule.
     * If the power is below this value, the EnergyFee of the previous EVPriceRule applies. Negative values are used for discharging.
     */
    private int powerRangeStart;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getEnergyFee() {
        return energyFee;
    }

    public void setEnergyFee(int energyFee) {
        this.energyFee = energyFee;
    }

    public int getPowerRangeStart() {
        return powerRangeStart;
    }

    public void setPowerRangeStart(int powerRangeStart) {
        this.powerRangeStart = powerRangeStart;
    }
}
