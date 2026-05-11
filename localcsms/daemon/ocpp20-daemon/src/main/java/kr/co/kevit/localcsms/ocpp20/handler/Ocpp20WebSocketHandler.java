package kr.co.kevit.localcsms.ocpp20.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.system.entity.domain.DaemonAccess;
import kr.co.kevit.localcsms.system.entity.domain.RemoteLog;
import kr.co.kevit.localcsms.system.process.DaemonAccessService;
import kr.co.kevit.localcsms.system.process.RemoteLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import kr.co.kevit.ocpp201.exception.OCPPErrorCode;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * OCPP 2.0.1 WebSocket 핸들러.
 *
 * 충전기 접속 URL : ws://host:9001/ocpp20/{cpId}
 * 서브프로토콜   : ocpp2.0.1
 *
 * Security Profile (기동 인자 args[0]):
 *   1, 2 — afterConnectionEstablished() 에서 Basic Auth 이중 검증
 *   그 외  — 인증 없이 허용 (Ocpp20SecurityFilter 에서 먼저 걸러짐)
 */
@Component
public class Ocpp20WebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {

    private static final Logger log             = LoggerFactory.getLogger(Ocpp20WebSocketHandler.class);
    private static final long   IDLE_TIMEOUT_MS = 15 * 60 * 1000L; // 15분

    private boolean authRequired = false;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /** 접속 중인 충전기 세션 (cpId → session) */
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /** 마지막 수신 시각 (cpId → timestamp ms) */
    private final Map<String, Long> lastMessageTime = new ConcurrentHashMap<>();

    /** 서버→충전기 명령 action 추적 (uniqueId → action) — CALLRESULT 디스패치용 */
    private final Map<String, String> pendingActions = new ConcurrentHashMap<>();

    /** 원격 명령 등록 시각 (uniqueId → timestamp ms) - 5분 초과 시 자동 정리 */
    private final Map<String, Long> pendingActionTime = new ConcurrentHashMap<>();

    /**
     * CALL 처리 맵 (action → ControlerBean).
     * @Component("ActionName") 으로 등록된 ControlerBean 구현체를 Spring이 자동 주입.
     * key = bean name(액션명), value = 구현체
     */
    @Autowired
    private Map<String, ControlerBean> actionHandlerMap;

    /**
     * CALLRESULT 처리 맵 (action → ResponderBean).
     * 서버→충전기 명령 응답 처리 빈이 없을 수 있으므로 required = false.
     */
    @Autowired(required = false)
    private Map<String, ResponderBean> responseHandlerMap;

    /** 유휴 세션 점검 스케줄러 */
    private ScheduledExecutorService idleCheckScheduler;

    @Autowired private ChargingStationService chargingStationService;
    @Autowired private ApplicationArguments   applicationArguments;
    @Autowired private DaemonAccessService    daemonAccessService;
    @Autowired private RemoteLogService       remoteLogService;

    @Value("${daemon.ip}")   private String daemonIp;
    @Value("${daemon.port}") private String daemonPort;

    @PostConstruct
    public void init() {
        // actionHandlerMap 은 @Autowired Map<String, ControlerBean> 로 Spring이 자동 주입.
        // @Component("ActionName") 으로 등록된 ControlerBean 구현체가 자동으로 등록됨.
        log.info("[OCPP20] ControlerBean 등록 현황: {}", actionHandlerMap != null ? actionHandlerMap.keySet() : "none");

        // DataTransferResBean 은 빈 이름 충돌 방지를 위해 "DataTransferRes" 로 등록됨 → action 이름으로 재매핑
        if (responseHandlerMap != null && responseHandlerMap.containsKey("DataTransferRes")) {
            responseHandlerMap.put("DataTransfer", responseHandlerMap.get("DataTransferRes"));
        }
        log.info("[OCPP20] ResponderBean 등록 현황: {}", responseHandlerMap != null ? responseHandlerMap.keySet() : "none");

        // Security Profile — sp=1 또는 sp=2 일 때 WebSocket 레벨 인증 활성화
        List<String> args = applicationArguments.getNonOptionArgs();
        String sp = args.isEmpty() ? "" : args.get(0);
        authRequired = "1".equals(sp) || "2".equals(sp);
        if (authRequired) {
            log.info("[OCPP20] Security Profile {} — WebSocket 레벨 Basic Auth 이중 검증 활성화", sp);
        }

        // 유휴 세션 점검 (1분 주기)
        idleCheckScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ocpp20-idle-checker");
            t.setDaemon(true);
            return t;
        });
        idleCheckScheduler.scheduleAtFixedRate(this::checkIdleSessions, 1, 1, TimeUnit.MINUTES);

        log.info("[OCPP20] 핸들러 초기화 완료.");
    }

    @PreDestroy
    public void destroy() {
        if (idleCheckScheduler != null) {
            idleCheckScheduler.shutdown();
        }
    }

    @Override
    public List<String> getSubProtocols() {
        return List.of("ocpp2.1", "ocpp2.0.1");
    }

    // ── 연결 / 해제 ──────────────────────────────────────────────────────────

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String cpId = extractCpId(session);

        // sp=1,2: WebSocket 핸드셰이크 직후 이중 검증
        if (authRequired && !authenticate(session, cpId)) {
            try {
                session.close(CloseStatus.NOT_ACCEPTABLE);
            } catch (Exception e) {
                log.warn("[OCPP20] 인증 실패 세션 종료 오류: cpId={} error={}", cpId, e.getMessage());
            }
            return;
        }

        sessions.put(cpId, session);
        lastMessageTime.put(cpId, System.currentTimeMillis());

        // DaemonAccess 등록/갱신
        try {
            DaemonAccess da = new DaemonAccess();
            da.setCpCsId(cpId);
            da.setIp(daemonIp);
            da.setPort(daemonPort);
            daemonAccessService.registerOrModifyDaemonAccess(da);
            log.info("[OCPP20] DaemonAccess 등록 cpId={} {}:{}", cpId, daemonIp, daemonPort);
        } catch (Exception e) {
            log.warn("[OCPP20] DaemonAccess 등록 실패 cpId={} error={}", cpId, e.getMessage());
        }

        log.info("[OCPP20] OPEN  cpId={} sessionId={}", cpId, session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String cpId = extractCpId(session);
        sessions.remove(cpId);
        lastMessageTime.remove(cpId);
        log.info("[OCPP20] CLOSE cpId={} sessionId={} status={}", cpId, session.getId(), status);
    }

    // ── 메시지 수신 ──────────────────────────────────────────────────────────

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String cpId = extractCpId(session);
        lastMessageTime.put(cpId, System.currentTimeMillis());
        log.info("[OCPP20] REQ cpId={} msg={}", cpId, message.getPayload());

        JsonNode raw = objectMapper.readTree(message.getPayload());
        if (!raw.isArray() || raw.size() < 3) {
            log.warn("[OCPP20] 잘못된 메시지 형식: cpId={}", cpId);
            return;
        }

        int    messageTypeId = raw.get(0).asInt();
        String uniqueId      = raw.get(1).asText();

        switch (messageTypeId) {
            case OcppMessage.CALL: {
                String   action  = raw.get(2).asText();
                JsonNode payload = raw.size() > 3 ? raw.get(3) : objectMapper.createObjectNode();
                log.debug("[OCPP20] CALL cpId={} action={} uniqueId={}", cpId, action, uniqueId);
                dispatchCall(session, cpId, new OcppMessage(messageTypeId, uniqueId, action, payload));
                break;
            }
            case OcppMessage.CALLRESULT: {
                JsonNode payload = raw.size() > 2 ? raw.get(2) : objectMapper.createObjectNode();
                log.debug("[OCPP20] CALLRESULT cpId={} uniqueId={}", cpId, uniqueId);
                String action = pendingActions.remove(uniqueId);
                pendingActionTime.remove(uniqueId);
                if (action != null) dispatchResponse(cpId, action, payload, uniqueId);
                break;
            }
            case OcppMessage.CALLERROR: {
                String errorCode = raw.size() > 2 ? raw.get(2).asText() : "UnknownError";
                String errorDesc = raw.size() > 3 ? raw.get(3).asText() : "";
                log.warn("[OCPP20] CALLERROR cpId={} uniqueId={} code={} desc={}",
                        cpId, uniqueId, errorCode, errorDesc);
                String action = pendingActions.remove(uniqueId);
                pendingActionTime.remove(uniqueId);
                if (action != null) {
                    ObjectNode errorPayload = objectMapper.createObjectNode();
                    errorPayload.put("errorCode", errorCode);
                    errorPayload.put("errorDescription", errorDesc);
                    dispatchResponse(cpId, action, errorPayload, uniqueId);
                }
                break;
            }
            default:
                log.warn("[OCPP20] 알 수 없는 messageTypeId={} cpId={}", messageTypeId, cpId);
        }
    }

    // ── CALL 디스패치 ─────────────────────────────────────────────────────────

    private void dispatchCall(WebSocketSession session, String cpId, OcppMessage msg) throws Exception {
        ControlerBean handler = actionHandlerMap != null ? actionHandlerMap.get(msg.getAction()) : null;
        if (handler == null) {
            log.warn("[OCPP20] 처리 가능한 ControlerBean 없음: action={}", msg.getAction());
            sendError(session, msg.getUniqueId(), "NotImplemented",
                    "Action not supported: " + msg.getAction());
            return;
        }
        ObjectNode result = null;
        try{
            result = handler.control(cpId, msg);
        } catch (SecurityException se) {
            log.error("[OCPP20] ControlerBean.control 오류: action={} cpId={} error={}", msg.getAction(), cpId, se.getMessage(), se);
            sendError(session, msg.getUniqueId(),
                    OCPPErrorCode.SecurityError.toString(),
                    OCPPErrorCode.SecurityError.getDesc());
            return;
        }catch(Exception e){
            log.error("[OCPP20] ControlerBean.control 오류: action={} cpId={} error={}", msg.getAction(), cpId, e.getMessage(), e);
            sendError(session, msg.getUniqueId(), "InternalError",
                    "Internal error: " + e.getMessage());
            return;
        }
        
        sendResult(session, msg.getUniqueId(), result != null ? result : objectMapper.createObjectNode());
    }

    // ── CALLRESULT 디스패치 ───────────────────────────────────────────────────

    private void dispatchResponse(String cpId, String action, JsonNode payload, String uniqueId) {
        // RemoteLog 업데이트 (uniqueId = RemoteLog UUID)
        updateRemoteLog(uniqueId, payload);

        if (responseHandlerMap == null) return;
        ResponderBean handler = responseHandlerMap.get(action);
        if (handler == null) {
            log.debug("[OCPP20] ResponseHandler 없음: action={}", action);
            return;
        }
        try {
            handler.handle(cpId, payload, uniqueId);
        } catch (Exception e) {
            log.error("[OCPP20] 응답 처리 오류: action={} cpId={} error={}", action, cpId, e.getMessage(), e);
        }
    }

    private void updateRemoteLog(String uniqueId, JsonNode payload) {
        try {
            boolean isError = payload.has("errorCode");
            String status = isError ? "RMST03" : "RMST02";
            String resPayload = truncate(objectMapper.writeValueAsString(payload), 255);

            RemoteLog remoteLog = new RemoteLog();
            remoteLog.setUuid(uniqueId);
            remoteLog.setStatus(status);
            remoteLog.setResPayload(resPayload);
            remoteLogService.modifyRemoteLog(remoteLog);
            log.info("[OCPP20] RemoteLog 업데이트: uniqueId={} status={}", uniqueId, status);
        } catch (Exception e) {
            log.warn("[OCPP20] RemoteLog 업데이트 실패: uniqueId={} error={}", uniqueId, e.getMessage());
        }
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() > maxLen ? s.substring(0, maxLen) : s;
    }

    // ── 원격 명령 전송 (REST → WebSocket) ────────────────────────────────────

    /**
     * 충전기에 CALL 메시지를 전송한다 (비동기 — 즉시 반환).
     * 충전기 응답(CALLRESULT)은 handleTextMessage → dispatchResponse 에서 비동기 처리된다.
     */
    public void sendCommand(String cpId, String action, JsonNode payload, String uuid) throws Exception {
        WebSocketSession session = sessions.get(cpId);
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("충전기 세션 없음: " + cpId);
        }

        // api-eai에서 전달된 RemoteLog UUID를 OCPP uniqueId로 사용 (없으면 새로 생성)
        String uniqueId = (uuid != null && !uuid.isEmpty()) ? uuid : UUID.randomUUID().toString();
        pendingActions.put(uniqueId, action);
        pendingActionTime.put(uniqueId, System.currentTimeMillis());

        String callJson = objectMapper.writeValueAsString(
                new Object[]{OcppMessage.CALL, uniqueId, action,
                        payload != null ? payload : objectMapper.createObjectNode()});

        log.info("[OCPP20] CMD cpId={} action={} uniqueId={} msg={}", cpId, action, uniqueId, callJson);
        synchronized (session) {
            session.sendMessage(new TextMessage(callJson));
        }
    }

    public boolean isConnected(String cpId) {
        WebSocketSession session = sessions.get(cpId);
        return session != null && session.isOpen();
    }

    public Set<String> getConnectedCpIds() {
        return sessions.keySet();
    }

    // ── 유휴 세션 점검 ────────────────────────────────────────────────────────

    private void checkIdleSessions() {
        long now = System.currentTimeMillis();
        sessions.entrySet().removeIf(entry -> {
            String cpId = entry.getKey();
            Long   last = lastMessageTime.get(cpId);
            if (last != null && (now - last) <= IDLE_TIMEOUT_MS) return false;

            log.info("[OCPP20] IDLE_TIMEOUT cpId={} - 15분 무수신, 세션 종료", cpId);
            try {
                entry.getValue().close(CloseStatus.SESSION_NOT_RELIABLE);
            } catch (Exception e) {
                log.warn("[OCPP20] 유휴 세션 종료 실패: cpId={} error={}", cpId, e.getMessage());
            }
            lastMessageTime.remove(cpId);
            return true;
        });
        // 5분 초과 미응답 pendingActions 정리
        pendingActionTime.entrySet().removeIf(entry -> {
            if ((now - entry.getValue()) > 5 * 60 * 1000L) {
                String uid = entry.getKey();
                pendingActions.remove(uid);
                log.info("[OCPP20] pendingAction 타임아웃 정리: uniqueId={}", uid);
                return true;
            }
            return false;
        });
    }

    // ── 인증 (Basic Auth + DB 비밀번호 검증) ─────────────────────────────────

    private boolean authenticate(WebSocketSession session, String cpCsId) {
        // cpCsId 예: "IA0002-03" → cpId="IA0002", csId="03"
        String[] parts = cpCsId.split("-", 2);
        if (parts.length < 2) {
            log.warn("[OCPP20] AUTH 실패 — URL에서 cpId/csId 분리 불가: {}", cpCsId);
            return false;
        }
        String cpId = parts[0];
        String csId = parts[1];

        String authHeader = session.getHandshakeHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            log.warn("[OCPP20] AUTH 실패 — Authorization 헤더 없음: cpCsId={}", cpCsId);
            return false;
        }

        String decoded;
        try {
            decoded = new String(Base64.getDecoder().decode(authHeader.substring(6)));
        } catch (Exception e) {
            log.warn("[OCPP20] AUTH 실패 — Base64 디코딩 오류: cpCsId={}", cpCsId);
            return false;
        }

        String[] credentials = decoded.split(":", 2);
        if (credentials.length < 2) {
            log.warn("[OCPP20] AUTH 실패 — 자격증명 형식 오류: cpCsId={}", cpCsId);
            return false;
        }

        ChargingStation cs = chargingStationService.retrieveChargingStationByCpIdNCsId(cpId, csId);
        if (cs == null) {
            log.warn("[OCPP20] AUTH 실패 — 충전소 없음: cpId={} csId={}", cpId, csId);
            return false;
        }

        if (!cs.getCsPassword().equals(credentials[1])) {
            log.warn("[OCPP20] AUTH 실패 — 비밀번호 불일치: cpId={} csId={}", cpId, csId);
            return false;
        }

        log.info("[OCPP20] AUTH 성공: cpCsId={}", cpCsId);
        return true;
    }

    // ── 응답 전송 헬퍼 ────────────────────────────────────────────────────────

    private void sendResult(WebSocketSession session, String uniqueId, ObjectNode payload) throws Exception {
        String json = objectMapper.writeValueAsString(
                new Object[]{OcppMessage.CALLRESULT, uniqueId, payload});
        log.info("[OCPP20] RES cpId={} msg={}", extractCpId(session), json);
        synchronized (session) {
            session.sendMessage(new TextMessage(json));
        }
    }

    private void sendError(WebSocketSession session, String uniqueId,
                           String errorCode, String description) throws Exception {
        String json = objectMapper.writeValueAsString(
                new Object[]{OcppMessage.CALLERROR, uniqueId, errorCode, description, objectMapper.createObjectNode()});
        log.warn("[OCPP20] ERR cpId={} msg={}", extractCpId(session), json);
        synchronized (session) {
            session.sendMessage(new TextMessage(json));
        }
    }

    // ── 유틸 ─────────────────────────────────────────────────────────────────

    private String extractCpId(WebSocketSession session) {
        String path = session.getUri() != null ? session.getUri().getPath() : "";
        int idx = path.lastIndexOf('/');
        return idx >= 0 ? path.substring(idx + 1) : "unknown";
    }
}
