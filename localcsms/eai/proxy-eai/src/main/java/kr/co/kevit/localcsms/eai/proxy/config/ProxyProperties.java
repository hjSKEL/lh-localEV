package kr.co.kevit.localcsms.eai.proxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@code proxy-eai.*} 설정.
 *
 * @author bckim
 */
@ConfigurationProperties(prefix = "proxy-eai")
public class ProxyProperties {

    private final Target target = new Target();
    private final Mtls mtls = new Mtls();
    private final Queue queue = new Queue();
    private final Notify notify = new Notify();

    public Target getTarget() {
        return target;
    }

    public Mtls getMtls() {
        return mtls;
    }

    public Queue getQueue() {
        return queue;
    }

    public Notify getNotify() {
        return notify;
    }

    /**
     * 프록시 대상(외부 OCPP1.6 서버) 접속 정보.
     *
     * <p>
     * 접속 base 주소({@code wss://host:port/path})는 더 이상 여기 설정하지 않는다 — 기동 시
     * {@code ConnConfig}(TB_SYCN001, 싱글턴)에서 {@code localOperationType} 에 따라
     * {@code lhCsmsAddress}/{@code cpoCsmsAddress} 중 하나를 읽어 결정한다. 실제 접속 URL은
     * {@code base + "/" + <identifier>} 로 조립된다 — CS0 세션은 identifier=localSystemId,
     * 충전기별 세션(CPO 모드에서만)은 identifier=cpCsId.
     * </p>
     */
    public static class Target {
        /** WS 연결(handshake) 타임아웃(ms). */
        private long connectTimeoutMs = 10000L;

        /** 연결 끊김 후 재연결 대기 간격(ms) — CS0 등 고정 간격 재연결에 사용. */
        private long reconnectIntervalMs = 5000L;

        /** 충전기별(CPO 모드) 세션 재연결 대기 최소값(ms) — 매 시도마다 이 범위에서 랜덤 선택. */
        private long reconnectMinMs = 10000L;

        /** 충전기별(CPO 모드) 세션 재연결 대기 최대값(ms). */
        private long reconnectMaxMs = 30000L;

        public long getConnectTimeoutMs() {
            return connectTimeoutMs;
        }

        public void setConnectTimeoutMs(long connectTimeoutMs) {
            this.connectTimeoutMs = connectTimeoutMs;
        }

        public long getReconnectIntervalMs() {
            return reconnectIntervalMs;
        }

        public void setReconnectIntervalMs(long reconnectIntervalMs) {
            this.reconnectIntervalMs = reconnectIntervalMs;
        }

        public long getReconnectMinMs() {
            return reconnectMinMs;
        }

        public void setReconnectMinMs(long reconnectMinMs) {
            this.reconnectMinMs = reconnectMinMs;
        }

        public long getReconnectMaxMs() {
            return reconnectMaxMs;
        }

        public void setReconnectMaxMs(long reconnectMaxMs) {
            this.reconnectMaxMs = reconnectMaxMs;
        }
    }

    /**
     * CS0(로컬 시스템 대표) 세션이 구독하는 단방향 notify 채널.
     * ocpp16-daemon 이 LH 모드일 때 발행하는 {@code daemon.relay.notify.exchange} 와
     * 반드시 동일한 exchange 이름이어야 한다.
     */
    public static class Notify {
        private String exchange = "ocpp16.notify";

        /** CS0 이 구독할 큐 이름(고정 1개, 라우팅키 전체 구독). */
        private String queueName = "notify.cs0";

        private boolean durable = true;

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        public String getQueueName() {
            return queueName;
        }

        public void setQueueName(String queueName) {
            this.queueName = queueName;
        }

        public boolean isDurable() {
            return durable;
        }

        public void setDurable(boolean durable) {
            this.durable = durable;
        }
    }

    /** mTLS 클라이언트 인증서(모든 세션 공통 1개 공유). */
    public static class Mtls {
        private String keystorePath;
        private String keystorePassword;
        private String keyPassword;
        private String keystoreType = "PKCS12";
        private String truststorePath;
        private String truststorePassword;
        private String truststoreType = "PKCS12";

        public String getKeystorePath() {
            return keystorePath;
        }

        public void setKeystorePath(String keystorePath) {
            this.keystorePath = keystorePath;
        }

        public String getKeystorePassword() {
            return keystorePassword;
        }

        public void setKeystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
        }

        public String getKeyPassword() {
            return keyPassword;
        }

        public void setKeyPassword(String keyPassword) {
            this.keyPassword = keyPassword;
        }

        public String getKeystoreType() {
            return keystoreType;
        }

        public void setKeystoreType(String keystoreType) {
            this.keystoreType = keystoreType;
        }

        public String getTruststorePath() {
            return truststorePath;
        }

        public void setTruststorePath(String truststorePath) {
            this.truststorePath = truststorePath;
        }

        public String getTruststorePassword() {
            return truststorePassword;
        }

        public void setTruststorePassword(String truststorePassword) {
            this.truststorePassword = truststorePassword;
        }

        public String getTruststoreType() {
            return truststoreType;
        }

        public void setTruststoreType(String truststoreType) {
            this.truststoreType = truststoreType;
        }
    }

    /** 충전기별 RabbitMQ 큐 이름 규칙. */
    public static class Queue {
        /** 요청 큐 접두사 — 실제 큐 이름 = {@code requestPrefix + cpCsId} (예: {@code req.111111-01}). */
        private String requestPrefix = "req.";

        /** 응답 큐 접두사 — 실제 큐 이름 = {@code responsePrefix + cpCsId}. */
        private String responsePrefix = "res.";

        private boolean durable = true;

        public String getRequestPrefix() {
            return requestPrefix;
        }

        public void setRequestPrefix(String requestPrefix) {
            this.requestPrefix = requestPrefix;
        }

        public String getResponsePrefix() {
            return responsePrefix;
        }

        public void setResponsePrefix(String responsePrefix) {
            this.responsePrefix = responsePrefix;
        }

        public boolean isDurable() {
            return durable;
        }

        public void setDurable(boolean durable) {
            this.durable = durable;
        }
    }
}
