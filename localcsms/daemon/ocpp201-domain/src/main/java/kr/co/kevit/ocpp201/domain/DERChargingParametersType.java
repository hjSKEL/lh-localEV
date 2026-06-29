/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import kr.co.kevit.ocpp201.enumtype.DERControlEnumType;
import kr.co.kevit.ocpp201.enumtype.IslandingDetectionEnumType;

import java.util.List;
import java.util.Map;

/**
 * *(2.1)* DERChargingParametersType is used in ChargingNeedsType during an ISO 15118-20 session
 * for AC_BPT_DER to report the inverter settings related to DER control that were agreed between EVSE and EV.
 * Fields starting with \"ev\" contain values from the EV.\r\nOther fields contain a value that is supported by both EV and EVSE.
 * DERChargingParametersType type is only relevant in case of an ISO 15118-20 AC_BPT_DER/AC_DER charging session.
 * NOTE: All these fields have values greater or equal to zero (i.e. are non-negative)
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class DERChargingParametersType {
    
    /**
     * DER control functions supported by EV.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType:DERControlFunctions (bitmap)
     */
    private List<DERControlEnumType> evSupportedDERControl;
    
    /**
     * Rated maximum injected active power by EV, at specified over-excited power factor (overExcitedPowerFactor).
     * It can also be defined as the rated maximum discharge power at the rated minimum injected reactive power value.
     * This means that if the EV is providing reactive power support, and it is requested to discharge at max power (e.g. to satisfy an EMS request),
     * the EV may override the request and discharge up to overExcitedMaximumDischargePower to meet the minimum reactive power requirements.
     * Corresponds to the WOvPF attribute in IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVOverExcitedMaximumDischargePower
     */
    private int evOverExcitedMaxDischargePower;
    
    /**
     * EV power factor when injecting (over excited) the minimum reactive power.
     * orresponds to the OvPF attribute in IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVOverExcitedPowerFactor
     */
    private int evOverExcitedPowerFactor;

    /**
     * Rated maximum injected active power by EV supported at specified under-excited power factor (EVUnderExcitedPowerFactor).
     * It can also be defined as the rated maximum dischargePower at the rated minimum absorbed reactive power value.
     * This means that if the EV is providing reactive power support, and it is requested to discharge at max power
     * (e.g. to satisfy an EMS request), the EV may override the request and discharge up to underExcitedMaximumDischargePower to meet the minimum reactive power requirements.
     * This corresponds to the WUnPF attribute in the IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVUnderExcitedMaximumDischargePower
     */
    private int evUnderExcitedMaxDischargePower;

    /**
     * EV power factor when injecting (under excited) the minimum reactive power.
     * Corresponds to the OvPF attribute in IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVUnderExcitedPowerFactor
     */
    private int evUnderExcitedPowerFactor;

    /**
     * ated maximum total apparent power, defined by min(EV, EVSE) in va.
     * Corresponds to the VAMaxRtg in IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumApparentPower
     */
    private int maxApparentPower;

    /**
     * Rated maximum absorbed apparent power, defined by min(EV, EVSE) in va.
     * This field represents the sum of all phases, unless values are provided for L2 and L3,
     * in which case this field represents phase L1.
     * Corresponds to the ChaVAMaxRtg in IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumChargeApparentPower
     */
    private int maxChargeApparentPower;

    /**
     * Rated maximum absorbed apparent power on phase L2, defined by min(EV, EVSE) in va.
     * Corresponds to the ChaVAMaxRtg in IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumChargeApparentPower_L2
     */
    private int maxChargeApparentPower_L2;

    /**
     * Rated maximum absorbed apparent power on phase L3, defined by min(EV, EVSE) in va.
     * Corresponds to the ChaVAMaxRtg in IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumChargeApparentPower_L3
     */
    private int maxChargeApparentPower_L3;

    /**
     * Rated maximum injected apparent power, defined by min(EV, EVSE) in va.
     * This field represents the sum of all phases, unless values are provided for L2 and L3,
     * in which case this field represents phase L1.
     * Corresponds to the DisVAMaxRtg in IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumDischargeApparentPower
     */
    private int maxDischargeApparentPower;

    /**
     * Rated maximum injected apparent power on phase L2, defined by min(EV, EVSE) in va.
     * Corresponds to the DisVAMaxRtg in IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumDischargeApparentPower_L2
     */
    private int maxDischargeApparentPower_L2;

    /**
     * Rated maximum injected apparent power on phase L3, defined by min(EV, EVSE) in va.
     * Corresponds to the DisVAMaxRtg in IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumDischargeApparentPower_L3
     */
    private int maxDischargeApparentPower_L3;

    /**
     * Rated maximum absorbed reactive power, defined by min(EV, EVSE), in vars.
     * This field represents the sum of all phases, unless values are provided for L2 and L3,
     * in which case this field represents phase L1.
     * Corresponds to the AvarMax attribute in the IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumChargeReactivePower
     */
    private int maxChargeReactivePower;

    /**
     * Rated maximum absorbed reactive power, defined by min(EV, EVSE), in vars on phase L2.
     * Corresponds to the AvarMax attribute in the IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumChargeReactivePower_L2
     */
    private int maxChargeReactivePower_L2;

    /**
     * Rated maximum absorbed reactive power, defined by min(EV, EVSE), in vars on phase L3.
     * Corresponds to the AvarMax attribute in the IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumChargeReactivePower_L3
     */
    private int maxChargeReactivePower_L3;

    /**
     * Rated minimum absorbed reactive power, defined by max(EV, EVSE), in vars.
     * This field represents the sum of all phases, unless values are provided for L2 and L3,
     * in which case this field represents phase L1.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMinimumChargeReactivePower
     */
    private int minChargeReactivePower;

    /**
     * Rated minimum absorbed reactive power, defined by max(EV, EVSE), in vars on phase L2.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMinimumChargeReactivePower_L2
     */
    private int minChargeReactivePower_L2;

    /**
     * Rated minimum absorbed reactive power, defined by max(EV, EVSE), in vars on phase L3.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMinimumChargeReactivePower_L3
     */
    private int minChargeReactivePower_L3;

    /**
     * Rated maximum injected reactive power, defined by min(EV, EVSE), in vars.
     * This field represents the sum of all phases, unless values are provided for L2 and L3,
     * in which case this field represents phase L1.
     * Corresponds to the IvarMax attribute in the IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumDischargeReactivePower
     */
    private int maxDischargeReactivePower;

    /**
     * Rated maximum injected reactive power, defined by min(EV, EVSE), in vars on phase L2.
     * Corresponds to the IvarMax attribute in the IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumDischargeReactivePower_L2
     */
    private int maxDischargeReactivePower_L2;

    /**
     * ated maximum injected reactive power, defined by min(EV, EVSE), in vars on phase L3.
     * Corresponds to the IvarMax attribute in the IEC 61850.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumDischargeReactivePower_L3
     */
    private int maxDischargeReactivePower_L3;

    /**
     * Rated minimum injected reactive power, defined by max(EV, EVSE), in vars.
     * This field represents the sum of all phases, unless values are provided for L2 and L3,
     * in which case this field represents phase L1.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMinimumDischargeReactivePower
     */
    private int minDischargeReactivePower;

    /**
     * Rated minimum injected reactive power, defined by max(EV, EVSE), in var on phase L2.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMinimumDischargeReactivePower_L2
     */
    private int minDischargeReactivePower_L2;

    /**
     * Rated minimum injected reactive power, defined by max(EV, EVSE), in var on phase L3.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMinimumDischargeReactivePower_L3
     */
    private int minDischargeReactivePower_L3;

    /**
     * Line voltage supported by EVSE and EV.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVNominalVoltage
     */
    private int nominalVoltage;

    /**
     * The nominal AC voltage (rms) offset between the Charging Station's electrical connection point and the utility\u2019s point of common coupling.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVNominalVoltageOffset
     */
    private int nominalVoltageOffset;

    /**
     * Maximum AC rms voltage, as defined by min(EV, EVSE)  to operate with.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumNominalVoltage
     */
    private int maxNominalVoltage;

    /**
     * Minimum AC rms voltage, as defined by max(EV, EVSE)  to operate with.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMinimumNominalVoltage
     */
    private int minNominalVoltage;

    /**
     * Manufacturer of the EV inverter.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVInverterManufacturer
     */
    private String evInverterManufacturer;

    /**
     * Model name of the EV inverter.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVInverterModel
     */
    private String evInverterModel;

    /**
     * Serial number of the EV inverter.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVInverterSerialNumber
     */
    private String evInverterSerialNumber;

    /**
     * Software version of EV inverter.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVInverterSwVersion
     */
    private String evInverterSwVersion;

    /**
     * Hardware version of EV inverter.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVInverterHwVersion
     */
    private String evInverterHwVersion;

    /**
     * Type of islanding detection method. Only mandatory when islanding detection is required at the site, as set in the ISO 15118 Service Details configuration.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVIslandingDetectionMethod
     * "minItems": 1
     */
    private List<IslandingDetectionEnumType> evIslandingDetectionMethod;

    /**
     * Time after which EV will trip if an island has been detected.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVIslandingTripTime
     */
    private int evIslandingTripTime;

    /**
     * Maximum injected DC current allowed at level 1 charging.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumLevel1DCInjection
     */
    private int evMaximumLevel1DCInjection;

    /**
     * Maximum allowed duration of DC injection at level 1 charging.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVDurationLevel1DCInjection
     */
    private int evDurationLevel1DCInjection;

    /**
     * Maximum injected DC current allowed at level 2 charging.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVMaximumLevel2DCInjection
     */
    private int evMaximumLevel2DCInjection;

    /**
     * "Maximum allowed duration of DC injection at level 2 charging.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVDurationLevel2DCInjection
     */
    private int evDurationLevel2DCInjection;

    /**
     * tMeasure of the susceptibility of the circuit to reactance, in Siemens (S).
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVReactiveSusceptance
     */
    private int evReactiveSusceptance;

    /**
     * Total energy value, in Wh, that EV is allowed to provide during the entire V2G session.
     * The value is independent of the V2X Cycling area. Once this value reaches the value of 0,
     * the EV may block any attempt to discharge in order to protect the battery health.
     * *ISO 15118-20*: DER_BPT_AC_CPDReqEnergyTransferModeType: EVSessionTotalDischargeEnergyAvailable
     */
    private int evSessionTotalDischargeEnergyAvailable;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<DERControlEnumType> getEvSupportedDERControl() {
        return evSupportedDERControl;
    }

    public void setEvSupportedDERControl(List<DERControlEnumType> evSupportedDERControl) {
        this.evSupportedDERControl = evSupportedDERControl;
    }

    public int getEvOverExcitedMaxDischargePower() {
        return evOverExcitedMaxDischargePower;
    }

    public void setEvOverExcitedMaxDischargePower(int evOverExcitedMaxDischargePower) {
        this.evOverExcitedMaxDischargePower = evOverExcitedMaxDischargePower;
    }

    public int getEvOverExcitedPowerFactor() {
        return evOverExcitedPowerFactor;
    }

    public void setEvOverExcitedPowerFactor(int evOverExcitedPowerFactor) {
        this.evOverExcitedPowerFactor = evOverExcitedPowerFactor;
    }

    public int getEvUnderExcitedMaxDischargePower() {
        return evUnderExcitedMaxDischargePower;
    }

    public void setEvUnderExcitedMaxDischargePower(int evUnderExcitedMaxDischargePower) {
        this.evUnderExcitedMaxDischargePower = evUnderExcitedMaxDischargePower;
    }

    public int getEvUnderExcitedPowerFactor() {
        return evUnderExcitedPowerFactor;
    }

    public void setEvUnderExcitedPowerFactor(int evUnderExcitedPowerFactor) {
        this.evUnderExcitedPowerFactor = evUnderExcitedPowerFactor;
    }

    public int getMaxApparentPower() {
        return maxApparentPower;
    }

    public void setMaxApparentPower(int maxApparentPower) {
        this.maxApparentPower = maxApparentPower;
    }

    public int getMaxChargeApparentPower() {
        return maxChargeApparentPower;
    }

    public void setMaxChargeApparentPower(int maxChargeApparentPower) {
        this.maxChargeApparentPower = maxChargeApparentPower;
    }

    public int getMaxChargeApparentPower_L2() {
        return maxChargeApparentPower_L2;
    }

    public void setMaxChargeApparentPower_L2(int maxChargeApparentPower_L2) {
        this.maxChargeApparentPower_L2 = maxChargeApparentPower_L2;
    }

    public int getMaxChargeApparentPower_L3() {
        return maxChargeApparentPower_L3;
    }

    public void setMaxChargeApparentPower_L3(int maxChargeApparentPower_L3) {
        this.maxChargeApparentPower_L3 = maxChargeApparentPower_L3;
    }

    public int getMaxDischargeApparentPower() {
        return maxDischargeApparentPower;
    }

    public void setMaxDischargeApparentPower(int maxDischargeApparentPower) {
        this.maxDischargeApparentPower = maxDischargeApparentPower;
    }

    public int getMaxDischargeApparentPower_L2() {
        return maxDischargeApparentPower_L2;
    }

    public void setMaxDischargeApparentPower_L2(int maxDischargeApparentPower_L2) {
        this.maxDischargeApparentPower_L2 = maxDischargeApparentPower_L2;
    }

    public int getMaxDischargeApparentPower_L3() {
        return maxDischargeApparentPower_L3;
    }

    public void setMaxDischargeApparentPower_L3(int maxDischargeApparentPower_L3) {
        this.maxDischargeApparentPower_L3 = maxDischargeApparentPower_L3;
    }

    public int getMaxChargeReactivePower() {
        return maxChargeReactivePower;
    }

    public void setMaxChargeReactivePower(int maxChargeReactivePower) {
        this.maxChargeReactivePower = maxChargeReactivePower;
    }

    public int getMaxChargeReactivePower_L2() {
        return maxChargeReactivePower_L2;
    }

    public void setMaxChargeReactivePower_L2(int maxChargeReactivePower_L2) {
        this.maxChargeReactivePower_L2 = maxChargeReactivePower_L2;
    }

    public int getMaxChargeReactivePower_L3() {
        return maxChargeReactivePower_L3;
    }

    public void setMaxChargeReactivePower_L3(int maxChargeReactivePower_L3) {
        this.maxChargeReactivePower_L3 = maxChargeReactivePower_L3;
    }

    public int getMinChargeReactivePower() {
        return minChargeReactivePower;
    }

    public void setMinChargeReactivePower(int minChargeReactivePower) {
        this.minChargeReactivePower = minChargeReactivePower;
    }

    public int getMinChargeReactivePower_L2() {
        return minChargeReactivePower_L2;
    }

    public void setMinChargeReactivePower_L2(int minChargeReactivePower_L2) {
        this.minChargeReactivePower_L2 = minChargeReactivePower_L2;
    }

    public int getMinChargeReactivePower_L3() {
        return minChargeReactivePower_L3;
    }

    public void setMinChargeReactivePower_L3(int minChargeReactivePower_L3) {
        this.minChargeReactivePower_L3 = minChargeReactivePower_L3;
    }

    public int getMaxDischargeReactivePower() {
        return maxDischargeReactivePower;
    }

    public void setMaxDischargeReactivePower(int maxDischargeReactivePower) {
        this.maxDischargeReactivePower = maxDischargeReactivePower;
    }

    public int getMaxDischargeReactivePower_L2() {
        return maxDischargeReactivePower_L2;
    }

    public void setMaxDischargeReactivePower_L2(int maxDischargeReactivePower_L2) {
        this.maxDischargeReactivePower_L2 = maxDischargeReactivePower_L2;
    }

    public int getMaxDischargeReactivePower_L3() {
        return maxDischargeReactivePower_L3;
    }

    public void setMaxDischargeReactivePower_L3(int maxDischargeReactivePower_L3) {
        this.maxDischargeReactivePower_L3 = maxDischargeReactivePower_L3;
    }

    public int getMinDischargeReactivePower() {
        return minDischargeReactivePower;
    }

    public void setMinDischargeReactivePower(int minDischargeReactivePower) {
        this.minDischargeReactivePower = minDischargeReactivePower;
    }

    public int getMinDischargeReactivePower_L2() {
        return minDischargeReactivePower_L2;
    }

    public void setMinDischargeReactivePower_L2(int minDischargeReactivePower_L2) {
        this.minDischargeReactivePower_L2 = minDischargeReactivePower_L2;
    }

    public int getMinDischargeReactivePower_L3() {
        return minDischargeReactivePower_L3;
    }

    public void setMinDischargeReactivePower_L3(int minDischargeReactivePower_L3) {
        this.minDischargeReactivePower_L3 = minDischargeReactivePower_L3;
    }

    public int getNominalVoltage() {
        return nominalVoltage;
    }

    public void setNominalVoltage(int nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public int getNominalVoltageOffset() {
        return nominalVoltageOffset;
    }

    public void setNominalVoltageOffset(int nominalVoltageOffset) {
        this.nominalVoltageOffset = nominalVoltageOffset;
    }

    public int getMaxNominalVoltage() {
        return maxNominalVoltage;
    }

    public void setMaxNominalVoltage(int maxNominalVoltage) {
        this.maxNominalVoltage = maxNominalVoltage;
    }

    public int getMinNominalVoltage() {
        return minNominalVoltage;
    }

    public void setMinNominalVoltage(int minNominalVoltage) {
        this.minNominalVoltage = minNominalVoltage;
    }

    public String getEvInverterManufacturer() {
        return evInverterManufacturer;
    }

    public void setEvInverterManufacturer(String evInverterManufacturer) {
        this.evInverterManufacturer = evInverterManufacturer;
    }

    public String getEvInverterModel() {
        return evInverterModel;
    }

    public void setEvInverterModel(String evInverterModel) {
        this.evInverterModel = evInverterModel;
    }

    public String getEvInverterSerialNumber() {
        return evInverterSerialNumber;
    }

    public void setEvInverterSerialNumber(String evInverterSerialNumber) {
        this.evInverterSerialNumber = evInverterSerialNumber;
    }

    public String getEvInverterSwVersion() {
        return evInverterSwVersion;
    }

    public void setEvInverterSwVersion(String evInverterSwVersion) {
        this.evInverterSwVersion = evInverterSwVersion;
    }

    public String getEvInverterHwVersion() {
        return evInverterHwVersion;
    }

    public void setEvInverterHwVersion(String evInverterHwVersion) {
        this.evInverterHwVersion = evInverterHwVersion;
    }

    public List<IslandingDetectionEnumType> getEvIslandingDetectionMethod() {
        return evIslandingDetectionMethod;
    }

    public void setEvIslandingDetectionMethod(List<IslandingDetectionEnumType> evIslandingDetectionMethod) {
        this.evIslandingDetectionMethod = evIslandingDetectionMethod;
    }

    public int getEvIslandingTripTime() {
        return evIslandingTripTime;
    }

    public void setEvIslandingTripTime(int evIslandingTripTime) {
        this.evIslandingTripTime = evIslandingTripTime;
    }

    public int getEvMaximumLevel1DCInjection() {
        return evMaximumLevel1DCInjection;
    }

    public void setEvMaximumLevel1DCInjection(int evMaximumLevel1DCInjection) {
        this.evMaximumLevel1DCInjection = evMaximumLevel1DCInjection;
    }

    public int getEvDurationLevel1DCInjection() {
        return evDurationLevel1DCInjection;
    }

    public void setEvDurationLevel1DCInjection(int evDurationLevel1DCInjection) {
        this.evDurationLevel1DCInjection = evDurationLevel1DCInjection;
    }

    public int getEvMaximumLevel2DCInjection() {
        return evMaximumLevel2DCInjection;
    }

    public void setEvMaximumLevel2DCInjection(int evMaximumLevel2DCInjection) {
        this.evMaximumLevel2DCInjection = evMaximumLevel2DCInjection;
    }

    public int getEvDurationLevel2DCInjection() {
        return evDurationLevel2DCInjection;
    }

    public void setEvDurationLevel2DCInjection(int evDurationLevel2DCInjection) {
        this.evDurationLevel2DCInjection = evDurationLevel2DCInjection;
    }

    public int getEvReactiveSusceptance() {
        return evReactiveSusceptance;
    }

    public void setEvReactiveSusceptance(int evReactiveSusceptance) {
        this.evReactiveSusceptance = evReactiveSusceptance;
    }

    public int getEvSessionTotalDischargeEnergyAvailable() {
        return evSessionTotalDischargeEnergyAvailable;
    }

    public void setEvSessionTotalDischargeEnergyAvailable(int evSessionTotalDischargeEnergyAvailable) {
        this.evSessionTotalDischargeEnergyAvailable = evSessionTotalDischargeEnergyAvailable;
    }
}
