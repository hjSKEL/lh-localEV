package kr.co.kevit.localcsms.eai.api.service;

import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reservation 관련 OCPP 1.6 액션 서비스.
 *
 * reserveNow / cancelReservation
 */
@Service
public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final Daemon16Client daemonClient;

    public ReservationService(Daemon16Client daemonClient) {
        this.daemonClient = daemonClient;
    }

    /** ReserveNow.req 전송 */
    public ApiResult reserveNow(String csId, int connectorId, OffsetDateTime expiryDate,
                                String idTag, int reservationId, String parentIdTag) {
        log.info("[API] reserveNow csId={} connectorId={} reservationId={}", csId, connectorId, reservationId);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("connectorId", connectorId);
        payload.put("expiryDate", expiryDate);
        payload.put("idTag", idTag);
        payload.put("reservationId", reservationId);
        if (parentIdTag != null) {
            payload.put("parentIdTag", parentIdTag);
        }
        return daemonClient.send(csId, "ReserveNow", payload, null);
    }

    /** CancelReservation.req 전송 */
    public ApiResult cancelReservation(String csId, int reservationId) {
        log.info("[API] cancelReservation csId={} reservationId={}", csId, reservationId);
        return daemonClient.send(csId, "CancelReservation", Map.of("reservationId", reservationId), null);
    }
}
