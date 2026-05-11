package kr.co.kevit.localcsms.eai.api.dto.type;

/** OCPP 1.6 MessageTrigger (TriggerMessage.req) */
public enum MessageTriggerType {
    BootNotification,
    DiagnosticsStatusNotification,
    FirmwareStatusNotification,
    Heartbeat,
    MeterValues,
    StatusNotification
}
