/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

/**
 * Type of grid event that caused this
 * @author jhkim <a href=mailto:jhkim@kevit.co.kr>jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public enum GridEventFaultEnumType {
    CurrentImbalance,
    LocalEmergency,
    LowInputPower,
    OverCurrent,
    OverFrequency,
    OverVoltage,
    PhaseRotation,
    RemoteEmergency,
    UnderFrequency,
    UnderVoltage,
    VoltageImbalance
}
