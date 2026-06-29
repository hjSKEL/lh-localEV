/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.domain;

import kr.co.kevit.ocpp16.enumtype.ContextTypeEnum;
import kr.co.kevit.ocpp16.enumtype.FormatEnum;
import kr.co.kevit.ocpp16.enumtype.LocationEnum;
import kr.co.kevit.ocpp16.enumtype.MeasurandTypeEnum;
import kr.co.kevit.ocpp16.enumtype.PhaseTypeEnum;
import kr.co.kevit.ocpp16.enumtype.UnitEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 26.
 */
public class SampledValue {
    
    /**
     * required
     */
    private String value;
    
    /**
     * 
     */
    private ContextTypeEnum context;
    
    /**
     * 
     */
    private FormatEnum format;

    /**
     * 
     */
    private MeasurandTypeEnum measurand;
    
    /**
     * 
     */
    private PhaseTypeEnum phase;
    
    /**
     * 
     */
    private LocationEnum location;
    
    /**
     * 
     */
    private UnitEnum unit;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ContextTypeEnum getContext() {
        return context;
    }

    public void setContext(ContextTypeEnum context) {
        this.context = context;
    }

    public FormatEnum getFormat() {
        return format;
    }

    public void setFormat(FormatEnum format) {
        this.format = format;
    }

    public MeasurandTypeEnum getMeasurand() {
        return measurand;
    }

    public void setMeasurand(MeasurandTypeEnum measurand) {
        this.measurand = measurand;
    }

    public PhaseTypeEnum getPhase() {
        return phase;
    }

    public void setPhase(PhaseTypeEnum phase) {
        this.phase = phase;
    }

    public LocationEnum getLocation() {
        return location;
    }

    public void setLocation(LocationEnum location) {
        this.location = location;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public void setUnit(UnitEnum unit) {
        this.unit = unit;
    }
}
