/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.util;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 4.
 */
public enum OcppConfigurationKey{
    //
    AuthorizeRemoteTxRequests,
    ClockAlignedDataInterval,
    ConnectionTimeOut,
    ConnectorPhaseRotation,
    GetConfigurationMaxKeys,
    HeartbeatInterval,
    LocalAuthorizeOffline,
    LocalPreAuthorize,
    MeterValuesAlignedData,
    MeterValuesSampledData,
    MeterValueSampleInterval,
    NumberOfConnectors,
    ResetRetries,
    StopTransactionOnEVSideDisconnect,
    StopTransactionOnInvalidId,
    StopTxnAlignedData,
    StopTxnSampledData,
    SupportedFeatureProfiles,
    TransactionMessageAttempts,
    TransactionMessageRetryInterval,
    UnlockConnectorOnEVSideDisconnect,
    LocalAuthListEnabled,
    LocalAuthListMaxLength,
    SendLocalListMaxLength,
    ChargeProfileMaxStackLevel,
    ChargingScheduleAllowedChargingRateUnit,
    ChargingScheduleMaxPeriods,
    MaxChargingProfilesInstalled;
}