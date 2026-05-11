package kr.co.kevit.localcsms.eai.api.service;

import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.model.Firmware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Firmware Management 관련 OCPP 1.6 액션 서비스.
 *
 * updateFirmware / signedUpdateFirmware
 */
@Service
public class FirmwareService {

    private static final Logger log = LoggerFactory.getLogger(FirmwareService.class);

    private final Daemon16Client daemonClient;

    public FirmwareService(Daemon16Client daemonClient) {
        this.daemonClient = daemonClient;
    }

    /** UpdateFirmware.req 전송 */
    public ApiResult updateFirmware(String csId, String location,
                                    OffsetDateTime retrieveDate,
                                    Integer retries, Integer retryInterval) {
        log.info("[API] updateFirmware csId={} location={}", csId, location);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("location", location);
        payload.put("retrieveDate", retrieveDate);
        if (retries != null) payload.put("retries", retries);
        if (retryInterval != null) payload.put("retryInterval", retryInterval);
        return daemonClient.send(csId, "UpdateFirmware", payload, null);
    }

    /** SignedUpdateFirmware.req 전송 */
    public ApiResult signedUpdateFirmware(String csId, int requestId,
                                          Firmware firmware,
                                          Integer retries, Integer retryInterval) {
        log.info("[API] signedUpdateFirmware csId={} requestId={}", csId, requestId);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("requestId", requestId);
        payload.put("firmware", firmware);
        if (retries != null) payload.put("retries", retries);
        if (retryInterval != null) payload.put("retryInterval", retryInterval);
        return daemonClient.send(csId, "SignedUpdateFirmware", payload, null);
    }
}
