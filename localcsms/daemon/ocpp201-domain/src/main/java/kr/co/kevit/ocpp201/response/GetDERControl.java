/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import kr.co.kevit.ocpp201.domain.*;
import kr.co.kevit.ocpp201.enumtype.DERControlStatusEnumType;

import java.util.List;
import java.util.Map;

/**
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class GetDERControl {
    //
    private Map<String, Object> customData;

    /**
     *     "minItems": 1,
     *     "maxItems": 24
     */
    private List<DERCurveType> curve;

    /**
     *     "minItems": 1,
     *     "maxItems": 24
     */
    private List<EnterServiceGetType> enterService;

    /**
     *     "minItems": 1,
     *     "maxItems": 24
     */
    private List<FixedPFGetType> fixedPFAbsorb;

    /**
     *     "minItems": 1,
     *     "maxItems": 24
     */
    private List<FixedPFGetType> fixedPFInject;

    /**
     *     "minItems": 1,
     *     "maxItems": 24
     */
    private List<FixedVarGetType> fixedVar;

    /**
     *     "minItems": 1,
     *     "maxItems": 24
     */
    private List<FreqDroopGetType> freqDroop;

    /**
     *     "minItems": 1,
     *     "maxItems": 24
     */
    private List<GradientGetType> gradient;

    /**
     *     "minItems": 1,
     *     "maxItems": 24
     */
    private List<LimitMaxDischargeGetType> limitMaxDischarge;

    /**
     * required
     */
    private DERControlStatusEnumType status;

    /**
     * (2.1)
     */
    private StatusInfoType statusInfo;


    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<DERCurveType> getCurve() {
        return curve;
    }

    public void setCurve(List<DERCurveType> curve) {
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

    public DERControlStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(DERControlStatusEnumType status) {
        this.status = status;
    }

    public StatusInfoType getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfoType statusInfo) {
        this.statusInfo = statusInfo;
    }
}
