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

    public Target getTarget() {
        return target;
    }

    public Mtls getMtls() {
        return mtls;
    }

    public Queue getQueue() {
        return queue;
    }

    /** 프록시 대상(외부 OCPP1.6 서버) 접속 정보. */
    public static class Target {
        /**
         * 접속 URL 템플릿. {@code {cpCsId}} 플레이스홀더가 실제 충전기 식별자(cpId-csId)로 치환된다.
         * 예: {@code wss://target.example.com/ocpp16/{cpCsId}}
         */
        private String urlTemplate;

        /** WS 연결(handshake) 타임아웃(ms). */
        private long connectTimeoutMs = 10000L;

        /** 연결 끊김 후 재연결 대기 간격(ms). */
        private long reconnectIntervalMs = 5000L;

        public String getUrlTemplate() {
            return urlTemplate;
        }

        public void setUrlTemplate(String urlTemplate) {
            this.urlTemplate = urlTemplate;
        }

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
