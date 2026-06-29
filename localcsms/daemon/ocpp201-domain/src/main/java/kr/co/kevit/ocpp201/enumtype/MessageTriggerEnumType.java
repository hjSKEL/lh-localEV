/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

/**
 * description: Type of message to be triggered.\r\n,
 * @author bckim <a href=mailto:bckim@kevit.co.kr>bckim@kevit.co.kr</a> 
 * @since 2020. 1. 16.
 */
public enum MessageTriggerEnumType {
    BootNotification,
    LogStatusNotification,
    FirmwareStatusNotification,
    Heartbeat,
    MeterValues,
    SignChargingStationCertificate,
    SignV2GCertificate,
    StatusNotification,
    TransactionEvent,
    SignCombinedCertificate,
    PublishFirmwareStatusNotification,
    // (2.1)
    SignV2G20Certificate,
    CustomTrigger
}
