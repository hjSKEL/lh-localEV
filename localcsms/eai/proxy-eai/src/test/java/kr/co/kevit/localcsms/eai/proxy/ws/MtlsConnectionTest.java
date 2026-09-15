package kr.co.kevit.localcsms.eai.proxy.ws;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import kr.co.kevit.localcsms.eai.proxy.config.MtlsSocketFactoryBuilder;
import kr.co.kevit.localcsms.eai.proxy.config.ProxyProperties;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 실제 대상 서버에 mTLS(클라이언트 인증서) OCPP1.6 WebSocket 연결이 되는지 확인하는 환경 의존 테스트.
 *
 * <p>
 * Spring 컨텍스트(DB, RabbitMQ 등) 없이 {@link Ocpp16WsClient} 와 {@link MtlsSocketFactoryBuilder}만
 * 사용해 실제 handshake 까지 확인한다 — proxy-eai 가 운영 중 쓰는 것과 동일한 클래스/로직.
 * </p>
 *
 * <p>
 * <b>서버 인증서 체인 검증은 항상 스킵한다({@code trustAllServerCerts=true}).</b> 이 테스트는
 * "우리 클라이언트 인증서가 대상 서버에 mTLS 로 받아들여지는가"만 확인하는 게 목적이라 트러스트스토어를
 * 두지 않는다 — 대상 서버(LH 등)가 실제로 쓰는 TLS 인증서 발급사가 바뀌어도(예: Sectigo → 다른 CA)
 * 이 테스트에는 영향이 없다. 서버 인증서 자체를 신뢰할지는 이 테스트의 관심사가 아니다.
 * </p>
 *
 * <p>
 * 기본값은 {@code src/test/resources}에 번들된 LH 테스트 클라이언트 인증서(발급 2026-09-01,
 * {@code TEST20260901-client.p12} — leaf + 중간 CA(CrossCertIoTCA1) 체인, 비밀번호 {@code lhtest!3})를
 * 사용한다. 다른 인증서/주소로 돌리고 싶으면 아래 시스템 프로퍼티로 덮어쓸 수 있다.
 * </p>
 *
 * <p>
 * 실행 예 (기본 번들 인증서로 바로 실행):
 * <pre>
 * mvn test -pl eai/proxy-eai -Dtest=MtlsConnectionTest
 * </pre>
 * 다른 클라이언트 인증서로 실행:
 * <pre>
 * mvn test -pl eai/proxy-eai -Dtest=MtlsConnectionTest \
 *   -Dproxy.mtls.test.keystore=/path/to/client-keystore.p12 \
 *   -Dproxy.mtls.test.keystore-password=xxxx
 * </pre>
 * {@code -Dproxy.mtls.test.url} 로 접속 주소를 바꿀 수 있다(기본값: LH KEV_CSMS1 엔드포인트).
 * </p>
 *
 * @author bckim
 */
class MtlsConnectionTest {

    private static final String DEFAULT_URL =
            "wss://eepig-dev.lh.or.kr:13080/websocket/CentralSystemService/KEV_CSMS1";

    /** 번들된 테스트 클라이언트 인증서 비밀번호. */
    private static final String DEFAULT_TEST_PASSWORD = "lhtest!3";

    private Ocpp16WsClient client;

    @Test
    void mtlsHandshakeSucceeds() throws Exception {
        String keystorePath = System.getProperty("proxy.mtls.test.keystore",
                classpathResourceFile("TEST20260901-client.p12"));
        Assumptions.assumeTrue(keystorePath != null && new File(keystorePath).isFile(),
                "mTLS 키스토어를 찾을 수 없어 스킵합니다. 번들된 테스트 인증서가 없다면 "
                        + "-Dproxy.mtls.test.keystore=/path/to/client-keystore.p12 로 직접 지정하세요.");

        ProxyProperties.Mtls mtls = new ProxyProperties.Mtls();
        mtls.setKeystorePath(keystorePath);
        mtls.setKeystorePassword(System.getProperty("proxy.mtls.test.keystore-password", DEFAULT_TEST_PASSWORD));
        mtls.setKeyPassword(System.getProperty("proxy.mtls.test.key-password",
                mtls.getKeystorePassword()));
        mtls.setKeystoreType(System.getProperty("proxy.mtls.test.keystore-type", "PKCS12"));
        // 서버 인증서 검증은 항상 스킵 — 트러스트스토어 자체가 없음(의도적으로 삭제)

        String url = System.getProperty("proxy.mtls.test.url", DEFAULT_URL);
        long timeoutSec = Long.getLong("proxy.mtls.test.timeout-sec", 10L);
        String keyManagerAlgorithm = System.getProperty("proxy.mtls.test.key-manager-algorithm",
                javax.net.ssl.KeyManagerFactory.getDefaultAlgorithm());

        SSLSocketFactory sslSocketFactory = MtlsSocketFactoryBuilder.build(mtls, true, keyManagerAlgorithm);

        client = new Ocpp16WsClient("MTLS-CHECK", URI.create(url), sslSocketFactory, message -> {
            // 연결 확인용 — 수신 메시지는 무시
        });

        boolean connected = client.connectBlocking(timeoutSec, TimeUnit.SECONDS);
        assertTrue(connected, "mTLS WebSocket 연결 실패 (handshake 미완료 또는 타임아웃) url=" + url);
        assertTrue(client.isOpenNow(), "연결은 됐으나 OPEN 상태가 아님 url=" + url);
    }

    @AfterEach
    void closeClient() {
        if (client != null) {
            client.close();
        }
    }

    /** test-classes 클래스패스에서 리소스를 찾아 실제 파일시스템 경로를 반환. 없으면 null. */
    private static String classpathResourceFile(String resourceName) {
        URL url = MtlsConnectionTest.class.getClassLoader().getResource(resourceName);
        if (url == null) {
            return null;
        }
        return new File(url.getPath()).getAbsolutePath();
    }
}
