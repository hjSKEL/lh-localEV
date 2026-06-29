/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ChargingRateUnitEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class ChargingScheduleType {
    
    /**
     * required
     * "type": "integer"
     */
    private Integer id;
    
    /**
     * "type": "string","format": "date-time"
     */
    private String startSchedule;
    
    private Integer duration;
    
    /**
     * required
     */
    private ChargingRateUnitEnumType chargingRateUnit;
    
    /**
     * required
     * "minItems": 1,"maxItems": 1024
     */
    private List<ChargingSchedulePeriodType> chargingSchedulePeriod;
    
    /**
     * "type": "number"
     */
    private Integer minChargingRate;
    
    private SalesTariffType salesTariff;

    private LimitAtSoCType limitAtSoC;

    private AbsolutePriceScheduleType absolutePriceSchedule;

    private PriceLevelScheduleType priceLevelSchedule;

    /**
     * *(2.1)* Power tolerance when following EVPowerProfile.
     */
    private Integer powerTolerance;

    /**
     * *(2.1)* Id of this element for referencing in a signature.
     */
    private Integer signatureId;

    /**
     * *(2.1)* Base64 encoded hash (SHA256 for ISO 15118-2, SHA512 for ISO 15118-20) of the EXI price schedule element. Used in signature.
     */
    private String digestValue;

    /**
     * *(2.1)* Defaults to false. When true, disregard time zone offset in dateTime fields of  _ChargingScheduleType_ and use unqualified local time at Charging Station instead.
     * This allows the same `Absolute` or `Recurring` charging profile to be used in both summer and winter time.
     */
    private Boolean useLocalTime;

    /**
     * *(2.1)* Defaults to 0. When _randomizedDelay_ not equals zero, then the start of each &lt;&lt;cmn_chargingscheduleperiodtype,ChargingSchedulePeriodType&gt;&gt;
     * is delayed by a randomly chosen number of seconds between 0 and _randomizedDelay_.  Only allowed for TxProfile and TxDefaultProfile.
     */
    private Integer randomizedDelay;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getStartSchedule() {
        return startSchedule;
    }

    public void setStartSchedule(String startSchedule) {
        this.startSchedule = startSchedule;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ChargingRateUnitEnumType getChargingRateUnit() {
        return chargingRateUnit;
    }

    public void setChargingRateUnit(ChargingRateUnitEnumType chargingRateUnit) {
        this.chargingRateUnit = chargingRateUnit;
    }

    public List<ChargingSchedulePeriodType> getChargingSchedulePeriod() {
        return chargingSchedulePeriod;
    }

    public void setChargingSchedulePeriod(List<ChargingSchedulePeriodType> chargingSchedulePeriod) {
        this.chargingSchedulePeriod = chargingSchedulePeriod;
    }

    public Integer getMinChargingRate() {
        return minChargingRate;
    }

    public void setMinChargingRate(Integer minChargingRate) {
        this.minChargingRate = minChargingRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SalesTariffType getSalesTariff() {
        return salesTariff;
    }

    public void setSalesTariff(SalesTariffType salesTariff) {
        this.salesTariff = salesTariff;
    }

    public LimitAtSoCType getLimitAtSoC() {
        return limitAtSoC;
    }

    public void setLimitAtSoC(LimitAtSoCType limitAtSoC) {
        this.limitAtSoC = limitAtSoC;
    }

    public AbsolutePriceScheduleType getAbsolutePriceSchedule() {
        return absolutePriceSchedule;
    }

    public void setAbsolutePriceSchedule(AbsolutePriceScheduleType absolutePriceSchedule) {
        this.absolutePriceSchedule = absolutePriceSchedule;
    }

    public PriceLevelScheduleType getPriceLevelSchedule() {
        return priceLevelSchedule;
    }

    public void setPriceLevelSchedule(PriceLevelScheduleType priceLevelSchedule) {
        this.priceLevelSchedule = priceLevelSchedule;
    }

    public Integer getPowerTolerance() {
        return powerTolerance;
    }

    public void setPowerTolerance(Integer powerTolerance) {
        this.powerTolerance = powerTolerance;
    }

    public Integer getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(Integer signatureId) {
        this.signatureId = signatureId;
    }

    public String getDigestValue() {
        return digestValue;
    }

    public void setDigestValue(String digestValue) {
        this.digestValue = digestValue;
    }

    public Boolean getUseLocalTime() {
        return useLocalTime;
    }

    public void setUseLocalTime(Boolean useLocalTime) {
        this.useLocalTime = useLocalTime;
    }

    public Integer getRandomizedDelay() {
        return randomizedDelay;
    }

    public void setRandomizedDelay(Integer randomizedDelay) {
        this.randomizedDelay = randomizedDelay;
    }
}