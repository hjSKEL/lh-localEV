/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

/**
 * *(2.1)* Charging operation mode to use during this time interval. When absent defaults to `ChargingOnly`.
 * @author jhkim <a href=mailto:jhkim@kevit.co.kr>jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public enum OperationModeEnumType {
    Idle,
    ChargingOnly,
    CentralSetpoint,
    ExternalSetpoint,
    ExternalLimits,
    CentralFrequency,
    LocalFrequency,
    LocalLoadBalancing
}
