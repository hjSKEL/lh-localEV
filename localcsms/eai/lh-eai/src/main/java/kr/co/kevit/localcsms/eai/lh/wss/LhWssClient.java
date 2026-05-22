package kr.co.kevit.localcsms.eai.lh.wss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.PreDestroy;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * LH WSS 클라이언트.
 *
 * <h3>접속</h3>
 * <ul>
 *   <li>어플리케이션 기동 직후 외부 WSS 엔드포인트로 접속.</li>
 *   <li>핸드셰이크 시 client 인증서(mTLS) 제시 — {@code lh.wss.keystore.*} 설정 사용.</li>
 *   <li>Subprotocol {@code ocpp1.6} 으로 협상 — {@code Sec-WebSocket-Protocol} 헤더.</li>
 * </ul>
 *
 * <h3>재접속 정책</h3>
 * <ul>
 *   <li>핸드셰이크 실패 / 연결 종료 / 전송 오류 시 현재 세션 정리 후
 *       10~30초 랜덤 지연으로 재접속, {@link #shutdown()} 전까지 무한 반복.</li>
 * </ul>
 *
 * <h3>응답(Pong) 감시</h3>
 * <ul>
 *   <li>{@code HEARTBEAT_INTERVAL_SEC} 마다 Ping 송신.</li>
 *   <li>마지막 Pong 수신 시각이 {@code PONG_TIMEOUT_SEC} 초과 시 세션 강제 종료
 *       (이후 afterConnectionClosed 경로로 재접속 트리거).</li>
 * </ul>
 */
@Component
public class LhWssClient {

    private static final Logger log = LoggerFactory.getLogger(LhWssClient.class);

    /** Tomcat WebSocket 클라이언트가 SSLContext 를 인식하는 user property 키. */
    private static final String TOMCAT_WS_SSL_CONTEXT = "org.apache.tomcat.websocket.SSL_CONTEXT";

    private static final long HEARTBEAT_INTERVAL_SEC = 30L;
    private static final long PONG_TIMEOUT_SEC = 60L;
    private static final int RECONNECT_MIN_SEC = 10;
    private static final int RECONNECT_MAX_SEC = 30;

    @Value("${lh.wss.url:}")
    private String wssUrl;

    @Value("${lh.wss.subprotocol:ocpp1.6}")
    private String subprotocol;

    @Value("${lh.wss.keystore.path:}")
    private String keystorePath;

    @Value("${lh.wss.keystore.password:}")
    private String keystorePassword;

    @Value("${lh.wss.keystore.type:PKCS12}")
    private String keystoreType;

    @Value("${lh.wss.truststore.path:}")
    private String truststorePath;

    @Value("${lh.wss.truststore.password:}")
    private String truststorePassword;

    @Value("${lh.wss.truststore.type:PKCS12}")
    private String truststoreType;

    /** true 면 서버 인증서 검증을 SKIP — 자가서명/임시 인증서 대응. 운영 환경 비권장. */
    @Value("${lh.wss.trust-all:true}")
    private boolean trustAll;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "lh-wss-scheduler");
        t.setDaemon(true);
        return t;
    });

    private final AtomicReference<WebSocketSession> sessionRef = new AtomicReference<>();
    private final AtomicLong lastPongAt = new AtomicLong(0L);
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);
    private final AtomicReference<ScheduledFuture<?>> heartbeatRef = new AtomicReference<>();

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        if (!StringUtils.hasText(wssUrl)) {
            log.warn("[LH-WSS] lh.wss.url 미설정 — WSS 접속 시도하지 않음");
            return;
        }
        connect();
    }

    private void connect() {
        if (shuttingDown.get()) return;

        log.info("[LH-WSS] WSS 접속 시도 url={} subprotocol={}", wssUrl, subprotocol);

        StandardWebSocketClient client;
        try {
            client = buildClient();
        } catch (Exception e) {
            log.error("[LH-WSS] SSLContext 구성 실패 — keystorePath={}", keystorePath, e);
            scheduleReconnect();
            return;
        }

        WebSocketHandler handler = new AbstractWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                sessionRef.set(session);
                lastPongAt.set(System.currentTimeMillis());
                log.info("[LH-WSS] 접속 완료 sessionId={} acceptedProtocol={}",
                        session.getId(), session.getAcceptedProtocol());
                startHeartbeat();
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                log.info("[LH-WSS] 수신 sessionId={} payload={}", session.getId(), message.getPayload());
            }

            @Override
            protected void handlePongMessage(WebSocketSession session, PongMessage message) {
                lastPongAt.set(System.currentTimeMillis());
                if (log.isDebugEnabled()) log.debug("[LH-WSS] pong 수신 sessionId={}", session.getId());
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
                log.warn("[LH-WSS] 연결 종료 sessionId={} status={}", session.getId(), status);
                stopHeartbeat();
                sessionRef.compareAndSet(session, null);
                scheduleReconnect();
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                log.error("[LH-WSS] 전송 오류 sessionId={}", session.getId(), exception);
                closeQuietly(session, CloseStatus.SERVER_ERROR);
                if (sessionRef.compareAndSet(session, null)) {
                    stopHeartbeat();
                    scheduleReconnect();
                }
            }
        };

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.setSecWebSocketProtocol(subprotocol);

        try {
            client.doHandshake(handler, headers, URI.create(wssUrl)).addCallback(
                    session -> log.debug("[LH-WSS] 핸드셰이크 성공 sessionId={}", session.getId()),
                    ex -> {
                        log.error("[LH-WSS] 핸드셰이크 실패: {}", ex.getMessage());
                        scheduleReconnect();
                    });
        } catch (Exception e) {
            log.error("[LH-WSS] 접속 시도 중 오류 url={}", wssUrl, e);
            scheduleReconnect();
        }
    }

    /** 인증서(mTLS) 및 subprotocol 협상이 가능하도록 SSLContext 가 주입된 클라이언트를 만든다. */
    private StandardWebSocketClient buildClient() throws Exception {
        StandardWebSocketClient client = new StandardWebSocketClient();
        SSLContext sslContext = buildSslContext();
        if (sslContext != null) {
            Map<String, Object> userProps = new HashMap<>();
            userProps.put(TOMCAT_WS_SSL_CONTEXT, sslContext);
            client.setUserProperties(userProps);
        }
        return client;
    }

    /**
     * keystore/truststore/trust-all 설정으로 SSLContext 구성.
     * 모든 SSL 설정이 비어있으면 null 반환 → 기본 JVM SSL 사용.
     */
    private SSLContext buildSslContext() throws Exception {
        boolean hasKeystore = StringUtils.hasText(keystorePath);
        boolean hasTruststore = StringUtils.hasText(truststorePath);
        if (!hasKeystore && !hasTruststore && !trustAll) {
            log.warn("[LH-WSS] SSL 설정 없음 — 기본 JVM SSL 사용");
            return null;
        }

        KeyManager[] keyManagers = null;
        if (hasKeystore) {
            KeyStore ks = KeyStore.getInstance(keystoreType);
            try (InputStream in = Files.newInputStream(Paths.get(keystorePath))) {
                ks.load(in, keystorePassword.toCharArray());
            }
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keystorePassword.toCharArray());
            keyManagers = kmf.getKeyManagers();
            log.info("[LH-WSS] client keystore 로드 완료 path={} type={}", keystorePath, keystoreType);
        }

        TrustManager[] trustManagers;
        if (trustAll) {
            log.warn("[LH-WSS] !! 서버 인증서 검증 SKIP (lh.wss.trust-all=true) — 운영 환경 비권장 !!");
            trustManagers = new TrustManager[]{ TRUST_ALL };
        } else if (hasTruststore) {
            KeyStore ts = KeyStore.getInstance(truststoreType);
            try (InputStream in = Files.newInputStream(Paths.get(truststorePath))) {
                ts.load(in, truststorePassword.toCharArray());
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);
            trustManagers = tmf.getTrustManagers();
            log.info("[LH-WSS] truststore 로드 완료 path={} type={}", truststorePath, truststoreType);
        } else {
            trustManagers = null;
        }

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(keyManagers, trustManagers, null);
        return ctx;
    }

    /** 모든 인증서 체인을 무조건 신뢰하는 TrustManager. dev/test 전용. */
    private static final X509TrustManager TRUST_ALL = new X509TrustManager() {
        @Override public void checkClientTrusted(X509Certificate[] chain, String authType) { }
        @Override public void checkServerTrusted(X509Certificate[] chain, String authType) { }
        @Override public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
    };

    private void scheduleReconnect() {
        if (shuttingDown.get()) return;
        int delay = ThreadLocalRandom.current().nextInt(RECONNECT_MIN_SEC, RECONNECT_MAX_SEC + 1);
        log.info("[LH-WSS] {}초 후 재접속 시도", delay);
        try {
            scheduler.schedule(this::connect, delay, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("[LH-WSS] 재접속 스케줄 실패", e);
        }
    }

    private void startHeartbeat() {
        stopHeartbeat();
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(
                this::heartbeatTick, HEARTBEAT_INTERVAL_SEC, HEARTBEAT_INTERVAL_SEC, TimeUnit.SECONDS);
        heartbeatRef.set(task);
    }

    private void stopHeartbeat() {
        ScheduledFuture<?> task = heartbeatRef.getAndSet(null);
        if (task != null) task.cancel(false);
    }

    private void heartbeatTick() {
        WebSocketSession session = sessionRef.get();
        if (session == null || !session.isOpen()) return;

        long elapsed = System.currentTimeMillis() - lastPongAt.get();
        if (elapsed > PONG_TIMEOUT_SEC * 1000L) {
            log.warn("[LH-WSS] Pong 응답 없음 ({}ms 경과) — 세션 강제 종료 sessionId={}", elapsed, session.getId());
            closeQuietly(session, CloseStatus.SESSION_NOT_RELIABLE);
            return;
        }
        try {
            session.sendMessage(new PingMessage());
            if (log.isDebugEnabled()) log.debug("[LH-WSS] ping 송신 sessionId={}", session.getId());
        } catch (Exception e) {
            log.error("[LH-WSS] ping 송신 오류 sessionId={}", session.getId(), e);
            closeQuietly(session, CloseStatus.SERVER_ERROR);
        }
    }

    /**
     * HTTP 컨트롤러에서 받은 본문을 그대로 WSS 세션으로 송신한다.
     * 세션 미연결 시 false 반환.
     */
    public boolean forward(String payload) {
        WebSocketSession session = sessionRef.get();
        if (session == null || !session.isOpen()) {
            log.warn("[LH-WSS] 세션 미연결 — 송신 실패");
            return false;
        }
        try {
            session.sendMessage(new TextMessage(payload));
            log.info("[LH-WSS] 송신 sessionId={} payload={}", session.getId(), payload);
            return true;
        } catch (Exception e) {
            log.error("[LH-WSS] 송신 오류", e);
            closeQuietly(session, CloseStatus.SERVER_ERROR);
            return false;
        }
    }

    private void closeQuietly(WebSocketSession session, CloseStatus status) {
        try {
            if (session.isOpen()) session.close(status);
        } catch (Exception e) {
            log.debug("[LH-WSS] 세션 종료 처리 중 예외", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        shuttingDown.set(true);
        stopHeartbeat();
        scheduler.shutdownNow();
        WebSocketSession session = sessionRef.getAndSet(null);
        if (session != null) closeQuietly(session, CloseStatus.NORMAL);
    }
}
