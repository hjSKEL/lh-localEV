/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.LocationEnumType;
import kr.co.kevit.ocpp201.enumtype.MeasurandEnumType;
import kr.co.kevit.ocpp201.enumtype.PhaseEnumType;
import kr.co.kevit.ocpp201.enumtype.ReadingContextEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class SampledValueType {
    
    /**
     * required
     * 
     * type:number
     */
    private double value;
    
    private ReadingContextEnumType context;
    
    private MeasurandEnumType measurand;
    
    private PhaseEnumType phase;
    
    private LocationEnumType location;
    
    private SignedMeterValueType signedMeterValue;
    
    private UnitOfMeasureType unitOfMeasure;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ReadingContextEnumType getContext() {
        return context;
    }

    public void setContext(ReadingContextEnumType context) {
        this.context = context;
    }

    public MeasurandEnumType getMeasurand() {
        return measurand;
    }

    public void setMeasurand(MeasurandEnumType measurand) {
        this.measurand = measurand;
    }

    public PhaseEnumType getPhase() {
        return phase;
    }

    public void setPhase(PhaseEnumType phase) {
        this.phase = phase;
    }

    public LocationEnumType getLocation() {
        return location;
    }

    public void setLocation(LocationEnumType location) {
        this.location = location;
    }

    public SignedMeterValueType getSignedMeterValue() {
        return signedMeterValue;
    }

    public void setSignedMeterValue(SignedMeterValueType signedMeterValue) {
        this.signedMeterValue = signedMeterValue;
    }

    public UnitOfMeasureType getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasureType unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
}