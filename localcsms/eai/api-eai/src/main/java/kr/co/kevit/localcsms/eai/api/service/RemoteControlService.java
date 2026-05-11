package kr.co.kevit.localcsms.eai.api.service;

import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.type.AvailabilityType;
import kr.co.kevit.localcsms.eai.api.dto.type.ExtendedMessageTriggerType;
import kr.co.kevit.localcsms.eai.api.dto.type.MessageTriggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Remote Control 관련 OCPP 1.6 액션 서비스.
 *
 * unlockConnector / triggerMessage / extendedTriggerMessage / changeAvailability
 */
@Service
public class RemoteControlService {

    private static final Logger log = LoggerFactory.getLogger(RemoteControlService.class);

    private final Daemon16Client daemonClient;

    public RemoteControlService(Daemon16Client daemonClient) {
        this.daemonClient = daemonClient;
    }

    /** UnlockConnector.req 전송 */
    public ApiResult unlockConnector(String csId, int connectorId) {
        log.info("[API] unlockConnector csId={} connectorId={}", csId, connectorId);
        return daemonClient.send(csId, "UnlockConnector", Map.of("connectorId", connectorId), null);
    }

    /** TriggerMessage.req 전송 */
    public ApiResult triggerMessage(String csId, MessageTriggerType requestedMessage,
                                    Integer connectorId) {
        log.info("[API] triggerMessage csId={} message={} connectorId={}", csId, requestedMessage, connectorId);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("requestedMessage", requestedMessage.name());
        if (connectorId != null) {
            payload.put("connectorId", connectorId);
        }
        return daemonClient.send(csId, "TriggerMessage", payload, null);
    }

    /** ExtendedTriggerMessage.req 전송 */
    public ApiResult extendedTriggerMessage(String csId, ExtendedMessageTriggerType requestedMessage,
                                            Integer connectorId) {
        log.info("[API] extendedTriggerMessage csId={} message={}", csId, requestedMessage);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("requestedMessage", requestedMessage.name());
        if (connectorId != null) {
            payload.put("connectorId", connectorId);
        }
        return daemonClient.send(csId, "ExtendedTriggerMessage", payload, null);
    }

    /** ChangeAvailability.req 전송 */
    public ApiResult changeAvailability(String csId, int connectorId, AvailabilityType type) {
        log.info("[API] changeAvailability csId={} connectorId={} type={}", csId, connectorId, type);
        return daemonClient.send(csId, "ChangeAvailability",
                Map.of("connectorId", connectorId, "type", type.name()), null);
    }
}
