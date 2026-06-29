/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

/**
 * @author jhkim <a href=mailto:jhkim@kevit.co.kr>jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public enum IslandingDetectionEnumType {
    NoAntiIslandingSupport,
    RoCoF,
    UVP_OVP,
    UFP_OFP,
    VoltageVectorShift,
    ZeroCrossingDetection,
    OtherPassive,
    ImpedanceMeasurement,
    ImpedanceAtFrequency,
    SlipModeFrequencyShift,
    SandiaFrequencyShift,
    SandiaVoltageShift,
    FrequencyJump,
    RCLQFactor,
    OtherActive
}
