package kr.co.kevit.localcsms.eai.api.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.system.entity.domain.DaemonAccess;
import kr.co.kevit.localcsms.system.entity.domain.RemoteLog;
import kr.co.kevit.localcsms.system.process.DaemonAccessService;
import kr.co.kevit.localcsms.system.process.RemoteLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

/**
 * ocpp16-daemon REST 클라이언트.
 *
 * 동기 방식: daemon 응답까지 대기 후 결과 반환.
 * (충전기의 OCPP CALL 응답은 기다리지 않음 — daemon이 WebSocket 전송만 하고 즉시 응답)
 */
@Component
public class Daemon16Client {

    private static final Logger log = LoggerFactory.getLogger(Daemon16Client.class);
    private static final String OCPP_VERSION = "1.6";

    @Autowired
    private DaemonAccessService daemonAccessService;

    @Autowired
    private RemoteLogService remoteLogService;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public Daemon16Client(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ApiResult send(String csId, String action, Object payload, String keyId) {
        DaemonAccess da = daemonAccessService.retrieveDaemonAccessByCpCsId(csId);
        if (da == null) {
            log.error("[API] DaemonAccess 없음 — csId={} action={}", csId, action);
            return ApiResult.rejected("DaemonAccess 없음: csId=" + csId + " 충전기가 한 번도 접속하지 않았거나 등록되지 않았습니다.");
        }

        String uuid = UUID.randomUUID().toString();

        String url = "http://" + da.getIp() + ":" + da.getPort() + "/command/" + csId;
        // payload를 NON_NULL ObjectMapper로 변환하여 null 필드 제거
        Object cleanPayload = Map.of();
        if (payload != null) {
            try {
                String json = objectMapper.writeValueAsString(payload);
                cleanPayload = objectMapper.readValue(json, Map.class);
            } catch (Exception e) {
                cleanPayload = payload;
            }
        }
        Map<String, Object> body = Map.of(
                "action", action,
                "payload", cleanPayload,
                "uuid", uuid);

        log.info("[API] daemon 호출: csId={} action={} uuid={} url={}", csId, action, uuid, url);
        saveRemoteLog(uuid, action, toJson(payload), OCPP_VERSION, keyId);

        try {
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response =
                    (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>)
                            restTemplate.postForEntity(url, body, Map.class);

            Map<String, Object> result = response.getBody();
            if (result == null) {
                updateRemoteLog(uuid, "RMST03", "daemon 응답 없음");
                return ApiResult.rejected("daemon 응답 없음");
            }

            String status = (String) result.get("status");
            if ("SUCCESS".equals(status)) {
                log.info("[API] daemon 성공: csId={} action={}", csId, action);
                updateRemoteLog(uuid, "RMST02", "전송 완료");
                return ApiResult.accepted(result.get("result"));
            } else {
                String message = (String) result.getOrDefault("message", "FAIL");
                log.warn("[API] daemon 거부: csId={} action={} message={}", csId, action, message);
                updateRemoteLog(uuid, "RMST03", truncate(message));
                return ApiResult.rejected(message);
            }

        } catch (Exception e) {
            log.error("[API] daemon 호출 실패: csId={} action={} error={}", csId, action, e.getMessage(), e);
            updateRemoteLog(uuid, "RMST03", truncate(e.getMessage()));
            return ApiResult.rejected(e.getMessage());
        }
    }

    private void saveRemoteLog(String uuid, String actionName, String reqPayload, String ocppVersion, String keyId) {
        try {
            RemoteLog remoteLog = new RemoteLog();
            remoteLog.setUuid(uuid);
            remoteLog.setActionName(truncate(actionName, 30));
            remoteLog.setReqPayload(truncate(reqPayload, 255));
            remoteLog.setStatus("RMST01");
            remoteLog.setOcppVersion(ocppVersion);
            remoteLog.setKeyId(keyId);
            remoteLogService.registerRemoteLog(remoteLog);
        } catch (Exception e) {
            log.error("[API] RemoteLog 저장 실패: {}", e.getMessage());
        }
    }

    private void updateRemoteLog(String uuid, String status, String resPayload) {
        try {
            RemoteLog remoteLog = new RemoteLog();
            remoteLog.setUuid(uuid);
            remoteLog.setStatus(status);
            remoteLog.setResPayload(resPayload);
            remoteLogService.modifyRemoteLog(remoteLog);
        } catch (Exception e) {
            log.error("[API] RemoteLog 업데이트 실패: {}", e.getMessage());
        }
    }

    private String toJson(Object obj) {
        if (obj == null) return "";
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return obj.toString();
        }
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() > maxLen ? s.substring(0, maxLen) : s;
    }

    private String truncate(String s) {
        return truncate(s, 255);
    }
}
