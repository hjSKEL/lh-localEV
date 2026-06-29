/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

/**
 * Type of control.  Determines which setting field below is used.
 * @author jhkim <a href=mailto:jhkim@kevit.co.kr>jhkim@kevit.co.kr</a>
 * @since 2024. 10. 21.
 */
public enum DERControlEnumType {
    EnterService,
    FreqDroop,
    FreqWatt,
    FixedPFAbsorb,
    FixedPFInject,
    FixedVar,
    Gradients,
    HFMustTrip,
    HFMayTrip,
    HVMustTrip,
    HVMomCess,
    HVMayTrip,
    LimitMaxDischarge,
    LFMustTrip,
    LVMustTrip,
    LVMomCess,
    LVMayTrip,
    PowerMonitoringMustTrip,
    VoltVar,
    VoltWatt,
    WattPF,
    WattVar
}
