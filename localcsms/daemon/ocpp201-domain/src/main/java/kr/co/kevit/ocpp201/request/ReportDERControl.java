/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.DERCurveGetType;
import kr.co.kevit.ocpp201.domain.EnterServiceGetType;
import kr.co.kevit.ocpp201.domain.FixedPFGetType;
import kr.co.kevit.ocpp201.domain.FixedVarGetType;
import kr.co.kevit.ocpp201.domain.FreqDroopGetType;
import kr.co.kevit.ocpp201.domain.GradientGetType;
import kr.co.kevit.ocpp201.domain.LimitMaxDischargeGetType;

/**
 * (2.1)
 */
public class ReportDERControl {

    /**
     * required
     */
    private int requestId;

    private Boolean tbc;

    private List<DERCurveGetType> curve;

    private List<EnterServiceGetType> enterService;

    private List<FixedPFGetType> fixedPFAbsorb;

    private List<FixedPFGetType> fixedPFInject;

    private List<FixedVarGetType> fixedVar;

    private List<FreqDroopGetType> freqDroop;

    private List<GradientGetType> gradient;

    private List<LimitMaxDischargeGetType> limitMaxDischarge;

    private Map<String, Object> customData;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Boolean getTbc() {
        return tbc;
    }

    public void setTbc(Boolean tbc) {
        this.tbc = tbc;
    }

    public List<DERCurveGetType> getCurve() {
        return curve;
    }

    public void setCurve(List<DERCurveGetType> curve) {
        this.curve = curve;
    }

    public List<EnterServiceGetType> getEnterService() {
        return enterService;
    }

    public void setEnterService(List<EnterServiceGetType> enterService) {
        this.enterService = enterService;
    }

    public List<FixedPFGetType> getFixedPFAbsorb() {
        return fixedPFAbsorb;
    }

    public void setFixedPFAbsorb(List<FixedPFGetType> fixedPFAbsorb) {
        this.fixedPFAbsorb = fixedPFAbsorb;
    }

    public List<FixedPFGetType> getFixedPFInject() {
        return fixedPFInject;
    }

    public void setFixedPFInject(List<FixedPFGetType> fixedPFInject) {
        this.fixedPFInject = fixedPFInject;
    }

    public List<FixedVarGetType> getFixedVar() {
        return fixedVar;
    }

    public void setFixedVar(List<FixedVarGetType> fixedVar) {
        this.fixedVar = fixedVar;
    }

    public List<FreqDroopGetType> getFreqDroop() {
        return freqDroop;
    }

    public void setFreqDroop(List<FreqDroopGetType> freqDroop) {
        this.freqDroop = freqDroop;
    }

    public List<GradientGetType> getGradient() {
        return gradient;
    }

    public void setGradient(List<GradientGetType> gradient) {
        this.gradient = gradient;
    }

    public List<LimitMaxDischargeGetType> getLimitMaxDischarge() {
        return limitMaxDischarge;
    }

    public void setLimitMaxDischarge(List<LimitMaxDischargeGetType> limitMaxDischarge) {
        this.limitMaxDischarge = limitMaxDischarge;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
