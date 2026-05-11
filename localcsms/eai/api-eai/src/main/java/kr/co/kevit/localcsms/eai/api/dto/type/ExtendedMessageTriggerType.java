package kr.co.kevit.localcsms.eai.api.dto.type;

/** OCPP 1.6 Security ExtendedMessageTrigger */
public enum ExtendedMessageTriggerType {
    BootNotification,
    LogStatusNotification,
    FirmwareStatusNotification,
    Heartbeat,
    MeterValues,
    SignChargePointCertificate,
    StatusNotification
}
