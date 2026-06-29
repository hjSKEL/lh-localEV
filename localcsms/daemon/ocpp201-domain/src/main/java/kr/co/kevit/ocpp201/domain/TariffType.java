/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

/**
 * (2.1)
 */
public class TariffType {

    /**
     * required
     */
    private String tariffId;

    /**
     * required
     */
    private String currency;

    private List<MessageContentType> description;

    private TariffEnergyType energy;

    private String validFrom;

    private TariffTimeType chargingTime;

    private TariffTimeType idleTime;

    private TariffFixedType fixedFee;

    private TariffTimeType reservationTime;

    private TariffFixedType reservationFixed;

    private PriceType minCost;

    private PriceType maxCost;

    private Map<String, Object> customData;

    public String getTariffId() {
        return tariffId;
    }

    public void setTariffId(String tariffId) {
        this.tariffId = tariffId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<MessageContentType> getDescription() {
        return description;
    }

    public void setDescription(List<MessageContentType> description) {
        this.description = description;
    }

    public TariffEnergyType getEnergy() {
        return energy;
    }

    public void setEnergy(TariffEnergyType energy) {
        this.energy = energy;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public TariffTimeType getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(TariffTimeType chargingTime) {
        this.chargingTime = chargingTime;
    }

    public TariffTimeType getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(TariffTimeType idleTime) {
        this.idleTime = idleTime;
    }

    public TariffFixedType getFixedFee() {
        return fixedFee;
    }

    public void setFixedFee(TariffFixedType fixedFee) {
        this.fixedFee = fixedFee;
    }

    public TariffTimeType getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(TariffTimeType reservationTime) {
        this.reservationTime = reservationTime;
    }

    public TariffFixedType getReservationFixed() {
        return reservationFixed;
    }

    public void setReservationFixed(TariffFixedType reservationFixed) {
        this.reservationFixed = reservationFixed;
    }

    public PriceType getMinCost() {
        return minCost;
    }

    public void setMinCost(PriceType minCost) {
        this.minCost = minCost;
    }

    public PriceType getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(PriceType maxCost) {
        this.maxCost = maxCost;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}
