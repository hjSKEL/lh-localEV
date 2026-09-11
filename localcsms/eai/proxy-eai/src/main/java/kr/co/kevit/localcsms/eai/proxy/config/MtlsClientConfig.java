package kr.co.kevit.localcsms.eai.proxy.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mTLS 클라이언트 인증서 설정 — 모든 {@code ChargerSession} 이 공유하는 단일 게이트웨이 인증서.
 *
 * <p>
 * 대상 서버가 충전기별로 다른 client 인증서를 요구하지 않는다는 전제(공통 게이트웨이 인증서 1개) 하에,
 * {@link SSLSocketFactory} 를 단 한 번 생성해 모든 세션이 재사용한다.
 * </p>
 *
 * @author bckim
 */
@Configuration
public class MtlsClientConfig {

    @Bean
    public SSLSocketFactory ocppTargetSslSocketFactory(ProxyProperties props) throws Exception {
        ProxyProperties.Mtls mtls = props.getMtls();

        KeyStore keyStore = KeyStore.getInstance(mtls.getKeystoreType());
        try (InputStream is = new FileInputStream(mtls.getKeystorePath())) {
            keyStore.load(is, mtls.getKeystorePassword().toCharArray());
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        String keyPassword = mtls.getKeyPassword() != null ? mtls.getKeyPassword() : mtls.getKeystorePassword();
        kmf.init(keyStore, keyPassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance(mtls.getTruststoreType());
        try (InputStream is = new FileInputStream(mtls.getTruststorePath())) {
            trustStore.load(is, mtls.getTruststorePassword().toCharArray());
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        return sslContext.getSocketFactory();
    }
}
