/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.TariffCostEnumType;

/**
 * (2.1)
 */
public class TotalCostType {

    /**
     * required
     */
    private String currency;

    /**
     * required
     */
    private TariffCostEnumType typeOfCost;

    private PriceType fixed;

    private PriceType energy;

    private PriceType chargingTime;

    private PriceType idleTime;

    private PriceType reservationTime;

    private PriceType reservationFixed;

    /**
     * required
     */
    private TotalPriceType total;

    private Map<String, Object> customData;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TariffCostEnumType getTypeOfCost() {
        return typeOfCost;
    }

    public void setTypeOfCost(TariffCostEnumType typeOfCost) {
        this.typeOfCost = typeOfCost;
    }

    public PriceType getFixed() {
        return fixed;
    }

    public void setFixed(PriceType fixed) {
        this.fixed = fixed;
    }

    public PriceType getEnergy() {
        return energy;
    }

    public void setEnergy(PriceType energy) {
        this.energy = energy;
    }

    public PriceType getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(PriceType chargingTime) {
        this.chargingTime = chargingTime;
    }

    public PriceType getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(PriceType idleTime) {
        this.idleTime = idleTime;
    }

    public PriceType getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(PriceType reservationTime) {
        this.reservationTime = reservationTime;
    }

    public PriceType getReservationFixed() {
        return reservationFixed;
    }

    public void setReservationFixed(PriceType reservationFixed) {
        this.reservationFixed = reservationFixed;
    }

    public TotalPriceType getTotal() {
        return total;
    }

    public void setTotal(TotalPriceType total) {
        this.total = total;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}
