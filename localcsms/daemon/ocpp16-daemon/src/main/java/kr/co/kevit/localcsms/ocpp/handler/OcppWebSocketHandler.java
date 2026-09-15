package kr.co.kevit.localcsms.ocpp.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.system.entity.domain.DaemonAccess;
import kr.co.kevit.localcsms.system.entity.domain.RemoteLog;
import kr.co.kevit.localcsms.system.process.DaemonAccessService;
import kr.co.kevit.localcsms.system.process.RemoteLogService;
import org.springframework.beans.factory.annotation.Value;
import kr.co.kevit.localcsms.ocpp.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp.bean.ResponderBean;
import kr.co.kevit.localcsms.ocpp.bean.req.AuthorizeBean;
import kr.co.kevit.localcsms.ocpp.bean.req.BootNotificationBean;
import kr.co.kevit.localcsms.ocpp.bean.req.DataTransferReqBean;
import kr.co.kevit.localcsms.ocpp.bean.req.DiagnosticsStatusNotificationBean;
import kr.co.kevit.localcsms.ocpp.bean.req.FirmwareStatusNotificationBean;
import kr.co.kevit.localcsms.ocpp.bean.req.HeartbeatBean;
import kr.co.kevit.localcsms.ocpp.bean.req.LogStatusNotificationBean;
import kr.co.kevit.localcsms.ocpp.bean.req.MeterValuesBean;
import kr.co.kevit.localcsms.ocpp.bean.req.SecurityEventNotificationBean;
import kr.co.kevit.localcsms.ocpp.bean.req.SignCertificateBean;
import kr.co.kevit.localcsms.ocpp.bean.req.SignedFirmwareStatusNotificationBean;
import kr.co.kevit.localcsms.ocpp.bean.req.StartTransactionBean;
import kr.co.kevit.localcsms.ocpp.bean.req.StatusNotificationBean;
import kr.co.kevit.localcsms.ocpp.bean.req.StopTransactionBean;
import kr.co.kevit.localcsms.ocpp.bean.res.CancelReservationBean;
import kr.co.kevit.localcsms.ocpp.bean.res.CertificateSignedBean;
import kr.co.kevit.localcsms.ocpp.bean.res.DeleteCertificateBean;
import kr.co.kevit.localcsms.ocpp.bean.res.ExtendedTriggerMessageBean;
import kr.co.kevit.localcsms.ocpp.bean.res.GetInstalledCertificateIdsBean;
import kr.co.kevit.localcsms.ocpp.bean.res.InstallCertificateBean;
import kr.co.kevit.localcsms.ocpp.bean.res.SignedUpdateFirmwareBean;
import kr.co.kevit.localcsms.ocpp.bean.res.ChangeAvailabilityBean;
import kr.co.kevit.localcsms.ocpp.bean.res.ChangeConfigurationBean;
import kr.co.kevit.localcsms.ocpp.bean.res.ClearCacheBean;
import kr.co.kevit.localcsms.ocpp.bean.res.ClearChargingProfileBean;
import kr.co.kevit.localcsms.ocpp.bean.res.GetCompositeScheduleBean;
import kr.co.kevit.localcsms.ocpp.bean.res.GetConfigurationBean;
import kr.co.kevit.localcsms.ocpp.bean.res.GetDiagnosticsBean;
import kr.co.kevit.localcsms.ocpp.bean.res.GetLocalListVersionBean;
import kr.co.kevit.localcsms.ocpp.bean.res.GetLogBean;
import kr.co.kevit.localcsms.ocpp.bean.res.RemoteStartTransactionBean;
import kr.co.kevit.localcsms.ocpp.bean.res.RemoteStopTransactionBean;
import kr.co.kevit.localcsms.ocpp.bean.res.ReserveNowBean;
import kr.co.kevit.localcsms.ocpp.bean.res.ResetBean;
import kr.co.kevit.localcsms.ocpp.bean.res.SendLocalListBean;
import kr.co.kevit.localcsms.ocpp.bean.res.SetChargingProfileBean;
import kr.co.kevit.localcsms.ocpp.bean.res.TriggerMessageBean;
import kr.co.kevit.localcsms.ocpp.bean.res.UnlockConnectorBean;
import kr.co.kevit.localcsms.ocpp.bean.res.DataTransferResBean;
import kr.co.kevit.localcsms.ocpp.bean.res.UpdateFirmwareBean;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;
import kr.co.kevit.localcsms.ocpp.mq.ChargerRelayPublisher;
import kr.co.kevit.localcsms.ocpp.mq.ChargerReverseRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class OcppWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {

    private static final Logger log             = LoggerFactory.getLogger(OcppWebSocketHandler.class);
    private static final long   IDLE_TIMEOUT_MS = 15 * 60 * 1000L; // 15분

    private final ObjectMapper objectMapper = new ObjectMapper();

    /** 접속 중인 충전기 세션 (cpId → session) */
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /** 마지막 수신 시각 (cpId → timestamp ms) */
    private final Map<String, Long> lastMessageTime = new ConcurrentHashMap<>();

    /** 원격 명령 action 추적 (uniqueId → action) - CALLRESULT 수신 시 res/ 빈 호출용 */
    private final Map<String, String> pendingActions = new ConcurrentHashMap<>();

    /** 원격 명령 등록 시각 (uniqueId → timestamp ms) - 5분 초과 시 자동 정리 */
    private final Map<String, Long> pendingActionTime = new ConcurrentHashMap<>();

    /** CALL 처리 빈 맵 (action → ControlerBean) */
    private Map<String, ControlerBean> callerBeanMap;

    /** CALLRESULT 처리 빈 맵 (action → ResponderBean) */
    private Map<String, ResponderBean> responderBeanMap;

    /** 유휴 세션 점검 스케줄러 */
    private ScheduledExecutorService idleCheckScheduler;

    // ---- req/ 빈 (충전기 → 서버 CALL 처리) ----
    @Autowired private AuthorizeBean                     authorizeBean;
    @Autowired private BootNotificationBean              bootNotificationBean;
    @Autowired private HeartbeatBean                     heartbeatBean;
    @Autowired private StatusNotificationBean            statusNotificationBean;
    @Autowired private MeterValuesBean                   meterValuesBean;
    @Autowired private StartTransactionBean              startTransactionBean;
    @Autowired private StopTransactionBean               stopTransactionBean;
    @Autowired private DiagnosticsStatusNotificationBean diagnosticsStatusNotificationBean;
    @Autowired private FirmwareStatusNotificationBean    firmwareStatusNotificationBean;
    @Autowired private LogStatusNotificationBean         logStatusNotificationBean;
    @Autowired private SecurityEventNotificationBean     securityEventNotificationBean;
    @Autowired private SignCertificateBean               signCertificateBean;
    @Autowired private SignedFirmwareStatusNotificationBean signedFirmwareStatusNotificationBean;
    @Autowired private DataTransferReqBean               dataTransferReqBean;

    // ---- res/ 빈 (서버 → 충전기 CALL에 대한 CALLRESULT 처리) ----
    @Autowired private CancelReservationBean     cancelReservationBean;
    @Autowired private ChangeAvailabilityBean    changeAvailabilityBean;
    @Autowired private ChangeConfigurationBean   changeConfigurationBean;
    @Autowired private ClearCacheBean            clearCacheBean;
    @Autowired private ClearChargingProfileBean  clearChargingProfileBean;
    @Autowired private GetCompositeScheduleBean  getCompositeScheduleBean;
    @Autowired private GetConfigurationBean      getConfigurationBean;
    @Autowired private GetDiagnosticsBean        getDiagnosticsBean;
    @Autowired private GetLocalListVersionBean   getLocalListVersionBean;
    @Autowired private GetLogBean                getLogBean;
    @Autowired private RemoteStartTransactionBean remoteStartTransactionBean;
    @Autowired private RemoteStopTransactionBean  remoteStopTransactionBean;
    @Autowired private ReserveNowBean            reserveNowBean;
    @Autowired private ResetBean                 resetBean;
    @Autowired private SendLocalListBean         sendLocalListBean;
    @Autowired private SetChargingProfileBean    setChargingProfileBean;
    @Autowired private TriggerMessageBean        triggerMessageBean;
    @Autowired private UnlockConnectorBean       unlockConnectorBean;
    @Autowired private UpdateFirmwareBean              updateFirmwareBean;
    @Autowired private DataTransferResBean             dataTransferResBean;
    @Autowired private CertificateSignedBean           certificateSignedBean;
    @Autowired private DeleteCertificateBean           deleteCertificateBean;
    @Autowired private ExtendedTriggerMessageBean      extendedTriggerMessageBean;
    @Autowired private GetInstalledCertificateIdsBean  getInstalledCertificateIdsBean;
    @Autowired private InstallCertificateBean          installCertificateBean;
    @Autowired private SignedUpdateFirmwareBean        signedUpdateFirmwareBean;

    @Autowired private ChargingStationService chargingStationService;
    @Autowired private DaemonAccessService    daemonAccessService;
    @Autowired private RemoteLogService       remoteLogService;

    // ---- CPO 모드 릴레이 / LH 모드 단방향 notify ----
    @Autowired private ChargerRelayPublisher relayPublisher;
    @Autowired private ChargerReverseRelay   reverseRelay;

    @Value("${daemon.ip}")
    private String daemonIp;

    @Value("${daemon.port}")
    private String daemonPort;

    /** 실행 4번째 인자(LH|CPO). 생략 시 기존 배포 호환을 위해 LH. */
    @Value("${daemon.mode:LH}")
    private String daemonMode;

    @PostConstruct
    public void initBeanMaps() {
        callerBeanMap = new HashMap<>();
        callerBeanMap.put("Authorize",                     authorizeBean);
        callerBeanMap.put("BootNotification",              bootNotificationBean);
        callerBeanMap.put("Heartbeat",                     heartbeatBean);
        callerBeanMap.put("StatusNotification",            statusNotificationBean);
        callerBeanMap.put("MeterValues",                   meterValuesBean);
        callerBeanMap.put("StartTransaction",              startTransactionBean);
        callerBeanMap.put("StopTransaction",               stopTransactionBean);
        callerBeanMap.put("DiagnosticsStatusNotification", diagnosticsStatusNotificationBean);
        callerBeanMap.put("FirmwareStatusNotification",    firmwareStatusNotificationBean);
        callerBeanMap.put("LogStatusNotification",         logStatusNotificationBean);
        callerBeanMap.put("SecurityEventNotification",     securityEventNotificationBean);
        callerBeanMap.put("SignCertificate",               signCertificateBean);
        callerBeanMap.put("SignedFirmwareStatusNotification", signedFirmwareStatusNotificationBean);
        callerBeanMap.put("DataTransfer",                  dataTransferReqBean);

        responderBeanMap = new HashMap<>();
        responderBeanMap.put("CancelReservation",      cancelReservationBean);
        responderBeanMap.put("ChangeAvailability",     changeAvailabilityBean);
        responderBeanMap.put("ChangeConfiguration",    changeConfigurationBean);
        responderBeanMap.put("ClearCache",             clearCacheBean);
        responderBeanMap.put("ClearChargingProfile",   clearChargingProfileBean);
        responderBeanMap.put("GetCompositeSchedule",   getCompositeScheduleBean);
        responderBeanMap.put("GetConfiguration",       getConfigurationBean);
        responderBeanMap.put("GetDiagnostics",         getDiagnosticsBean);
        responderBeanMap.put("GetLocalListVersion",    getLocalListVersionBean);
        responderBeanMap.put("GetLog",                 getLogBean);
        responderBeanMap.put("RemoteStartTransaction", remoteStartTransactionBean);
        responderBeanMap.put("RemoteStopTransaction",  remoteStopTransactionBean);
        responderBeanMap.put("ReserveNow",             reserveNowBean);
        responderBeanMap.put("Reset",                  resetBean);
        responderBeanMap.put("SendLocalList",          sendLocalListBean);
        responderBeanMap.put("SetChargingProfile",     setChargingProfileBean);
        responderBeanMap.put("TriggerMessage",         triggerMessageBean);
        responderBeanMap.put("UnlockConnector",        unlockConnectorBean);
        responderBeanMap.put("UpdateFirmware",              updateFirmwareBean);
        responderBeanMap.put("DataTransfer",                dataTransferResBean);
        responderBeanMap.put("CertificateSigned",           certificateSignedBean);
        responderBeanMap.put("DeleteCertificate",           deleteCertificateBean);
        responderBeanMap.put("ExtendedTriggerMessage",      extendedTriggerMessageBean);
        responderBeanMap.put("GetInstalledCertificateIds",  getInstalledCertificateIdsBean);
        responderBeanMap.put("InstallCertificate",          installCertificateBean);
        responderBeanMap.put("SignedUpdateFirmware",        signedUpdateFirmwareBean);

        // 유휴 세션 점검: 1분마다 실행
        idleCheckScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ocpp-idle-checker");
            t.setDaemon(true);
            return t;
        });
        idleCheckScheduler.scheduleAtFixedRate(this::checkIdleSessions, 1, 1, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void destroy() {
        if (idleCheckScheduler != null) {
            idleCheckScheduler.shutdown();
        }
    }

    @Override
    public List<String> getSubProtocols() {
        return List.of("ocpp1.6");
    }

    // -------------------------------------------------------------------------
    // 연결 / 해제
    // -------------------------------------------------------------------------

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String cpCsId = extractCpId(session);
        sessions.put(cpCsId, session);
        lastMessageTime.put(cpCsId, System.currentTimeMillis());
        log.info("[OCPP] OPEN cpId={} sessionId={}", cpCsId, session.getId());

        try {
            DaemonAccess da = new DaemonAccess();
            da.setCpCsId(cpCsId);
            da.setIp(daemonIp);
            da.setPort(daemonPort);
            daemonAccessService.registerOrModifyDaemonAccess(da);
            log.info("[OCPP] DaemonAccess 업데이트 cpCsId={} ip={} port={}", cpCsId, daemonIp, daemonPort);
        } catch (Exception e) {
            log.error("[OCPP] DaemonAccess 업데이트 실패 cpCsId={} error={}", cpCsId, e.getMessage(), e);
        }

        // CPO 모드 — 대상서버(외부 CSMS)발 메시지를 이 충전기에 그대로 릴레이하는 구독 시작 (CSMS→CS 방향)
        if (isCpoMode()) {
            try {
                reverseRelay.start(cpCsId, rawText -> sendRaw(cpCsId, rawText));
            } catch (Exception e) {
                log.error("[proxy-relay] res 구독 시작 실패 cpCsId={}: {}", cpCsId, e.getMessage(), e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String cpId = extractCpId(session);
        sessions.remove(cpId);
        lastMessageTime.remove(cpId);
        log.info("[OCPP] CLOSE cpId={} sessionId={} status={}", cpId, session.getId(), status);

        if (isCpoMode()) {
            reverseRelay.stop(cpId);
        }
    }

    private boolean isCpoMode() {
        return "CPO".equalsIgnoreCase(daemonMode);
    }

    // -------------------------------------------------------------------------
    // 메시지 수신
    // -------------------------------------------------------------------------

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String cpId = extractCpId(session);
        lastMessageTime.put(cpId, System.currentTimeMillis());
        log.info("[OCPP] REQ cpId={} msg={}", cpId, message.getPayload());

        // CPO 모드 — 메시지 종류(CALL/CALLRESULT/CALLERROR) 구분 없이 원문 그대로 req.<cpCsId> 로 릴레이.
        // daemon 자체는 어떤 전문도 해석/처리하지 않는다 (아래 dispatch 로직 전체를 건너뜀).
        if (isCpoMode()) {
            relayPublisher.publishToTarget(cpId, message.getPayload());
            return;
        }

        JsonNode raw = objectMapper.readTree(message.getPayload());

        if (!raw.isArray() || raw.size() < 3) {
            log.warn("[OCPP] 잘못된 메시지 형식: cpId={}", cpId);
            return;
        }

        int    messageTypeId = raw.get(0).asInt();
        String uniqueId      = raw.get(1).asText();

        if (messageTypeId == OcppMessage.CALL) {
            String   action  = raw.get(2).asText();
            JsonNode payload = raw.size() > 3 ? raw.get(3) : objectMapper.createObjectNode();
            log.debug("[OCPP] CALL cpId={} action={} uniqueId={}", cpId, action, uniqueId);

            // LH 모드 — BootNotification/StatusNotification 은 기존 처리(dispatchCall)는 그대로 수행하면서,
            // 원문을 단방향으로도 발행 (fire-and-forget, 응답 소비 없음)
            if (!isCpoMode() && ("BootNotification".equals(action) || "StatusNotification".equals(action))) {
                try {
                    relayPublisher.publishNotify(cpId, action, message.getPayload());
                } catch (Exception e) {
                    log.warn("[notify] 발행 실패 cpId={} action={}: {}", cpId, action, e.getMessage());
                }
            }

            dispatchCall(session, cpId, new OcppMessage(messageTypeId, uniqueId, action, payload));

        } else if (messageTypeId == OcppMessage.CALLRESULT) {
            JsonNode payload = raw.size() > 2 ? raw.get(2) : objectMapper.createObjectNode();
            log.debug("[OCPP] CALLRESULT cpId={} uniqueId={}", cpId, uniqueId);
            String action = pendingActions.remove(uniqueId);
            pendingActionTime.remove(uniqueId);
            if (action != null) dispatchResponse(cpId, action, payload, uniqueId);

        } else if (messageTypeId == OcppMessage.CALLERROR) {
            String errorCode = raw.size() > 2 ? raw.get(2).asText() : "UnknownError";
            String errorDesc = raw.size() > 3 ? raw.get(3).asText() : "";
            log.warn("[OCPP] CALLERROR cpId={} uniqueId={} code={} desc={}", cpId, uniqueId, errorCode, errorDesc);
            String action = pendingActions.remove(uniqueId);
            pendingActionTime.remove(uniqueId);
            if (action != null) {
                ObjectNode errorPayload = objectMapper.createObjectNode();
                errorPayload.put("errorCode", errorCode);
                errorPayload.put("errorDescription", errorDesc);
                dispatchResponse(cpId, action, errorPayload, uniqueId);
            }
        }
    }

    // -------------------------------------------------------------------------
    // CALL 디스패치 (충전기 → 서버)
    // -------------------------------------------------------------------------

    private void dispatchCall(WebSocketSession session, String cpCsId, OcppMessage msg) throws Exception {
        ControlerBean bean = callerBeanMap.get(msg.getAction());
        if (bean == null) {
            log.warn("[OCPP] 처리 가능한 Handler 없음: action={}", msg.getAction());
            sendError(session, msg.getUniqueId(), "NotImplemented",
                    "Action not supported: " + msg.getAction());
            return;
        }
        sendResult(session, msg.getUniqueId(), bean.control(cpCsId, msg));
    }

    // -------------------------------------------------------------------------
    // CALLRESULT 디스패치 (서버→충전기 명령 응답 처리)
    // -------------------------------------------------------------------------

    private void dispatchResponse(String cpCsId, String action, JsonNode payload, String uniqueId) {
        // RemoteLog 업데이트 (uniqueId = RemoteLog UUID)
        updateRemoteLog(uniqueId, payload);

        ResponderBean bean = responderBeanMap.get(action);
        if (bean == null) {
            log.warn("[OCPP] 응답 처리 Bean 없음: action={}", action);
            return;
        }
        try {
            bean.handle(cpCsId, payload, uniqueId);
        } catch (Exception e) {
            log.error("[OCPP] 응답 처리 오류: action={} cpCsId={} error={}", action, cpCsId, e.getMessage(), e);
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
            log.info("[OCPP] RemoteLog 업데이트: uniqueId={} status={}", uniqueId, status);
        } catch (Exception e) {
            log.warn("[OCPP] RemoteLog 업데이트 실패: uniqueId={} error={}", uniqueId, e.getMessage());
        }
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() > maxLen ? s.substring(0, maxLen) : s;
    }

    // -------------------------------------------------------------------------
    // 원격 명령 전송 (HTTP → WebSocket)
    // -------------------------------------------------------------------------

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

        log.info("[OCPP] CMD cpId={} action={} uniqueId={} msg={}", cpId, action, uniqueId, callJson);
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

    // -------------------------------------------------------------------------
    // 유휴 세션 점검 (15분 무수신 시 강제 종료)
    // -------------------------------------------------------------------------

    private void checkIdleSessions() {
        long now = System.currentTimeMillis();
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            String cpId = entry.getKey();
            WebSocketSession session = entry.getValue();
            Long last = lastMessageTime.get(cpId);
            if (last == null || (now - last) > IDLE_TIMEOUT_MS) {
                log.info("[OCPP] IDLE_TIMEOUT cpId={} sessionId={} - 15분간 메시지 없음, 세션 종료",
                        cpId, session.getId());
                try {
                    session.close(CloseStatus.SESSION_NOT_RELIABLE);
                } catch (Exception e) {
                    log.warn("[OCPP] 유휴 세션 종료 실패: cpId={} error={}", cpId, e.getMessage());
                }
                sessions.remove(cpId);
                lastMessageTime.remove(cpId);
            }
        }
        // 5분 초과 미응답 pendingActions 정리
        pendingActionTime.entrySet().removeIf(entry -> {
            if ((now - entry.getValue()) > 5 * 60 * 1000L) {
                String uid = entry.getKey();
                pendingActions.remove(uid);
                log.info("[OCPP] pendingAction 타임아웃 정리: uniqueId={}", uid);
                return true;
            }
            return false;
        });
    }

    // -------------------------------------------------------------------------
    // 응답 전송 헬퍼
    // -------------------------------------------------------------------------

    private void sendResult(WebSocketSession session, String uniqueId, ObjectNode payload) throws Exception {
        String json = objectMapper.writeValueAsString(
                new Object[]{OcppMessage.CALLRESULT, uniqueId, payload});
        log.info("[OCPP] RES cpId={} msg={}", extractCpId(session), json);
        synchronized (session) {
            session.sendMessage(new TextMessage(json));
        }
    }

    private void sendError(WebSocketSession session, String uniqueId,
                           String errorCode, String description) throws Exception {
        String json = objectMapper.writeValueAsString(
                new Object[]{OcppMessage.CALLERROR, uniqueId, errorCode, description, new Object()});
        log.info("[OCPP] ERR cpId={} msg={}", extractCpId(session), json);
        synchronized (session) {
            session.sendMessage(new TextMessage(json));
        }
    }

    /**
     * CPO 모드 전용 — RabbitMQ({@code res.<cpCsId>})로 받은 원문을 파싱 없이 그대로 충전기에 전달
     * (CSMS→CS 방향). RabbitMQ 리스너 스레드에서 호출되므로, WS 자체 read 스레드와 동시에 같은
     * session 에 쓰지 않도록 {@code sendResult}/{@code sendError}/{@code sendCommand} 와 동일하게
     * session 에 synchronized 한다.
     */
    private void sendRaw(String cpCsId, String rawJson) {
        WebSocketSession session = sessions.get(cpCsId);
        if (session == null || !session.isOpen()) {
            log.warn("[proxy-relay] sendRaw 실패 — 세션 없음/닫힘 cpCsId={}", cpCsId);
            return;
        }
        try {
            log.info("[proxy-relay] res → CS cpCsId={} msg={}", cpCsId, rawJson);
            synchronized (session) {
                session.sendMessage(new TextMessage(rawJson));
            }
        } catch (Exception e) {
            log.warn("[proxy-relay] sendRaw 전송 실패 cpCsId={}: {}", cpCsId, e.getMessage(), e);
        }
    }

    private String extractCpId(WebSocketSession session) {
        String path = session.getUri() != null ? session.getUri().getPath() : "";
        int idx = path.lastIndexOf('/');
        return idx >= 0 ? path.substring(idx + 1) : "unknown";
    }
}
