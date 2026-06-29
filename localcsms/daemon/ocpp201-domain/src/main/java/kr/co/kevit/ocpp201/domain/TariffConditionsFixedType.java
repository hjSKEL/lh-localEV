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
public class TariffConditionsFixedType {

    private String startTimeOfDay;

    private String endTimeOfDay;

    private List<DayOfWeekEnumType> dayOfWeek;

    private String validFromDate;

    private String validToDate;

    private EvseKindEnumType evseKind;

    private String paymentBrand;

    private String paymentRecognition;

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

    public String getPaymentBrand() {
        return paymentBrand;
    }

    public void setPaymentBrand(String paymentBrand) {
        this.paymentBrand = paymentBrand;
    }

    public String getPaymentRecognition() {
        return paymentRecognition;
    }

    public void setPaymentRecognition(String paymentRecognition) {
        this.paymentRecognition = paymentRecognition;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}
