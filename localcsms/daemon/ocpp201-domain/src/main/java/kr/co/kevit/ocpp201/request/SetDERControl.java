/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import kr.co.kevit.ocpp201.domain.*;
import kr.co.kevit.ocpp201.enumtype.DERControlEnumType;

import java.util.Map;

/**
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 21.
 */
public class SetDERControl {
    //
    private Map<String, Object> customData;

    /**
     * True if this is a default DER control
     * required
     */
    private boolean isDefault;

    /**
     * Unique id of this control, e.g. UUID
     * maxLength": 36
     * required
     */
    private String controlId;

    /**
     * required
     */
    private DERControlEnumType controlType;

    private DERCurveType curve;
    private EnterServiceType enterService;
    private FixedPFType fixedPFAbsorb;
    private FixedPFType fixedPFInject;
    private FixedVarType fixedVar;
    private FreqDroopType freqDroop;
    private GradientType gradient;
    private LimitMaxDischargeType limitMaxDischarge;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public DERControlEnumType getControlType() {
        return controlType;
    }

    public void setControlType(DERControlEnumType controlType) {
        this.controlType = controlType;
    }

    public DERCurveType getCurve() {
        return curve;
    }

    public void setCurve(DERCurveType curve) {
        this.curve = curve;
    }

    public EnterServiceType getEnterService() {
        return enterService;
    }

    public void setEnterService(EnterServiceType enterService) {
        this.enterService = enterService;
    }

    public FixedPFType getFixedPFAbsorb() {
        return fixedPFAbsorb;
    }

    public void setFixedPFAbsorb(FixedPFType fixedPFAbsorb) {
        this.fixedPFAbsorb = fixedPFAbsorb;
    }

    public FixedPFType getFixedPFInject() {
        return fixedPFInject;
    }

    public void setFixedPFInject(FixedPFType fixedPFInject) {
        this.fixedPFInject = fixedPFInject;
    }

    public FixedVarType getFixedVar() {
        return fixedVar;
    }

    public void setFixedVar(FixedVarType fixedVar) {
        this.fixedVar = fixedVar;
    }

    public FreqDroopType getFreqDroop() {
        return freqDroop;
    }

    public void setFreqDroop(FreqDroopType freqDroop) {
        this.freqDroop = freqDroop;
    }

    public GradientType getGradient() {
        return gradient;
    }

    public void setGradient(GradientType gradient) {
        this.gradient = gradient;
    }

    public LimitMaxDischargeType getLimitMaxDischarge() {
        return limitMaxDischarge;
    }

    public void setLimitMaxDischarge(LimitMaxDischargeType limitMaxDischarge) {
        this.limitMaxDischarge = limitMaxDischarge;
    }
}
