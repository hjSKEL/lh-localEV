/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.DayOfWeekEnumType;
import kr.co.kevit.ocpp201.enumtype.EvseKindEnumType;

/**
 * (2.1)
 */
public class TariffConditionsType {

    private String startTimeOfDay;

    private String endTimeOfDay;

    private List<DayOfWeekEnumType> dayOfWeek;

    private String validFromDate;

    private String validToDate;

    private EvseKindEnumType evseKind;

    private Double minEnergy;

    private Double maxEnergy;

    private Double minCurrent;

    private Double maxCurrent;

    private Double minPower;

    private Double maxPower;

    private Integer minTime;

    private Integer maxTime;

    private Integer minChargingTime;

    private Integer maxChargingTime;

    private Integer minIdleTime;

    private Integer maxIdleTime;

    private Map<String, Object> customData;

    public String getStartTimeOfDay() {
        return startTimeOfDay;
    }

    public void setStartTimeOfDay(String startTimeOfDay) {
        this.startTimeOfDay = startTimeOfDay;
    }

    public String getEndTimeOfDay() {
        return endTimeOfDay;
    }

    public void setEndTimeOfDay(String endTimeOfDay) {
        this.endTimeOfDay = endTimeOfDay;
    }

    public List<DayOfWeekEnumType> getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(List<DayOfWeekEnumType> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(String validFromDate) {
        this.validFromDate = validFromDate;
    }

    public String getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(String validToDate) {
        this.validToDate = validToDate;
    }

    public EvseKindEnumType getEvseKind() {
        return evseKind;
    }

    public void setEvseKind(EvseKindEnumType evseKind) {
        this.evseKind = evseKind;
    }

    public Double getMinEnergy() {
        return minEnergy;
    }

    public void setMinEnergy(Double minEnergy) {
        this.minEnergy = minEnergy;
    }

    public Double getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(Double maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public Double getMinCurrent() {
        return minCurrent;
    }

    public void setMinCurrent(Double minCurrent) {
        this.minCurrent = minCurrent;
    }

    public Double getMaxCurrent() {
        return maxCurrent;
    }

    public void setMaxCurrent(Double maxCurrent) {
        this.maxCurrent = maxCurrent;
    }

    public Double getMinPower() {
        return minPower;
    }

    public void setMinPower(Double minPower) {
        this.minPower = minPower;
    }

    public Double getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(Double maxPower) {
        this.maxPower = maxPower;
    }

    public Integer getMinTime() {
        return minTime;
    }

    public void setMinTime(Integer minTime) {
        this.minTime = minTime;
    }

    public Integer getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
    }

    public Integer getMinChargingTime() {
        return minChargingTime;
    }

    public void setMinChargingTime(Integer minChargingTime) {
        this.minChargingTime = minChargingTime;
    }

    public Integer getMaxChargingTime() {
        return maxChargingTime;
    }

    public void setMaxChargingTime(Integer maxChargingTime) {
        this.maxChargingTime = maxChargingTime;
    }

    public Integer getMinIdleTime() {
        return minIdleTime;
    }

    public void setMinIdleTime(Integer minIdleTime) {
        this.minIdleTime = minIdleTime;
    }

    public Integer getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(Integer maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}
