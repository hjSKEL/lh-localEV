package kr.co.kevit.localcsms.eai.api.service;

import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.model.LogParameters;
import kr.co.kevit.localcsms.eai.api.dto.type.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Diagnostics 관련 OCPP 1.6 액션 서비스.
 *
 * getDiagnostics / getLog
 */
@Service
public class DiagnosticsService {

    private static final Logger log = LoggerFactory.getLogger(DiagnosticsService.class);

    private final Daemon16Client daemonClient;

    public DiagnosticsService(Daemon16Client daemonClient) {
        this.daemonClient = daemonClient;
    }

    /** GetDiagnostics.req 전송 */
    public ApiResult getDiagnostics(String csId, String location,
                                    Integer retries, Integer retryInterval,
                                    OffsetDateTime startTime, OffsetDateTime stopTime) {
        log.info("[API] getDiagnostics csId={} location={}", csId, location);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("location", location);
        if (retries != null) payload.put("retries", retries);
        if (retryInterval != null) payload.put("retryInterval", retryInterval);
        if (startTime != null) payload.put("startTime", startTime);
        if (stopTime != null) payload.put("stopTime", stopTime);
        return daemonClient.send(csId, "GetDiagnostics", payload, null);
    }

    /** GetLog.req 전송 (Security 확장) */
    public ApiResult getLog(String csId, LogType logType, int requestId,
                            LogParameters log_,
                            Integer retries, Integer retryInterval) {
        log.info("[API] getLog csId={} logType={} requestId={}", csId, logType, requestId);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("logType", logType.name());
        payload.put("requestId", requestId);
        if (log_ != null) payload.put("log", log_);
        if (retries != null) payload.put("retries", retries);
        if (retryInterval != null) payload.put("retryInterval", retryInterval);
        return daemonClient.send(csId, "GetLog", payload, null);
    }
}
