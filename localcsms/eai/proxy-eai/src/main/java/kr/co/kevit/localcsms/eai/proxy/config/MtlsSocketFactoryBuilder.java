package kr.co.kevit.localcsms.eai.proxy.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * {@link ProxyProperties.Mtls} 설정으로부터 mTLS {@link SSLSocketFactory} 를 만드는 로직.
 *
 * <p>
 * {@link MtlsClientConfig}(Spring Bean)과, Spring 컨텍스트 없이 연결만 확인하는 테스트
 * (예: {@code MtlsConnectionTest})가 동일한 로직을 공유하기 위해 static 유틸로 분리했다.
 * </p>
 *
 * @author bckim
 */
public final class MtlsSocketFactoryBuilder {

    private MtlsSocketFactoryBuilder() {
    }

    /** 서버 인증서 검증 포함 — 운영에서 쓰는 기본 경로. */
    public static SSLSocketFactory build(ProxyProperties.Mtls mtls) throws Exception {
        return build(mtls, false);
    }

    /**
     * @param trustAllServerCerts true 면 서버 인증서 체인 검증을 완전히 건너뛴다(개발/디버깅 전용 —
     *                            대상 서버 인증서 체인 문제와 클라이언트(mTLS) 인증서 문제를 분리해서
     *                            확인할 때만 사용. 운영 경로({@link MtlsClientConfig})에서는 절대 true 로 두지 말 것).
     */
    public static SSLSocketFactory build(ProxyProperties.Mtls mtls, boolean trustAllServerCerts) throws Exception {
        return build(mtls, trustAllServerCerts, KeyManagerFactory.getDefaultAlgorithm());
    }

    /**
     * @param keyManagerAlgorithm {@link KeyManagerFactory} 알고리즘. 기본(SunX509)은 클라이언트
     *                             인증서 별칭 선택 시 CertificateRequest 의 CA 목록을 인증서의
     *                             "직접 발급자"만 비교한다(체인 상위 CA는 고려 안 함) — 서버가 루트 CA
     *                             이름으로 요청하는데 우리 인증서의 직접 발급자가 중간 CA면 별칭 매칭에
     *                             실패해 빈 인증서를 보낼 수 있다. {@code "PKIX"} 는 체인 전체를 검증하므로
     *                             이런 경우에 더 안정적이다.
     */
    public static SSLSocketFactory build(ProxyProperties.Mtls mtls, boolean trustAllServerCerts,
            String keyManagerAlgorithm) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(mtls.getKeystoreType());
        try (InputStream is = new FileInputStream(mtls.getKeystorePath())) {
            keyStore.load(is, mtls.getKeystorePassword().toCharArray());
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(keyManagerAlgorithm);
        String keyPassword = mtls.getKeyPassword() != null ? mtls.getKeyPassword() : mtls.getKeystorePassword();
        kmf.init(keyStore, keyPassword.toCharArray());

        TrustManager[] trustManagers = trustAllServerCerts
                ? new TrustManager[] { trustAllTrustManager() }
                : buildTrustManagers(mtls);

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(kmf.getKeyManagers(), trustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    private static TrustManager[] buildTrustManagers(ProxyProperties.Mtls mtls) throws Exception {
        KeyStore trustStore = KeyStore.getInstance(mtls.getTruststoreType());
        try (InputStream is = new FileInputStream(mtls.getTruststorePath())) {
            trustStore.load(is, mtls.getTruststorePassword().toCharArray());
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        return tmf.getTrustManagers();
    }

    private static X509TrustManager trustAllTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                // no-op — 개발/디버깅 전용
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                // no-op — 서버 인증서 체인 검증 스킵 (개발/디버깅 전용)
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }
}
