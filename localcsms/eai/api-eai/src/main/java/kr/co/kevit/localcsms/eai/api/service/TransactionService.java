package kr.co.kevit.localcsms.eai.api.service;

import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.model.ChargingProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Transaction 관련 OCPP 1.6 액션 서비스.
 *
 * requestStartTransaction / requestStopTransaction
 */
@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final Daemon16Client daemonClient;

    public TransactionService(Daemon16Client daemonClient) {
        this.daemonClient = daemonClient;
    }

    /** RemoteStartTransaction.req 전송 */
    public ApiResult requestStartTransaction(String csId, int connectorId,
                                             String idTag,
                                             ChargingProfile chargingProfile) {
        log.info("[API] requestStartTransaction csId={} connectorId={} idTag={}", csId, connectorId, idTag);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("connectorId", connectorId);
        payload.put("idTag", idTag);
        if (chargingProfile != null) {
            payload.put("chargingProfile", chargingProfile);
        }
        return daemonClient.send(csId, "RemoteStartTransaction", payload, null);
    }

    /** RemoteStopTransaction.req 전송 */
    public ApiResult requestStopTransaction(String csId, int transactionId) {
        log.info("[API] requestStopTransaction csId={} transactionId={}", csId, transactionId);
        return daemonClient.send(csId, "RemoteStopTransaction", Map.of("transactionId", transactionId), null);
    }
}
