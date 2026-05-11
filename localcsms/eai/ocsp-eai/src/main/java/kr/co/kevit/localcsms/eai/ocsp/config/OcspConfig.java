package kr.co.kevit.localcsms.eai.ocsp.config;

import kr.co.kevit.localcsms.eai.ocsp.service.OcspResponderService;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;

/**
 * RSA CA / ECDSA CA 키·인증서 로드 및 OcspResponderService 초기화.
 *
 * application.yml 의 ocsp.rsa.* / ocsp.ec.* 경로에서 PEM 파일을 읽는다.
 * 경로가 비어 있으면 해당 CA 체인은 비활성화된다.
 */
@Configuration
public class OcspConfig {

    private static final Logger log = LoggerFactory.getLogger(OcspConfig.class);

    // ── RSA ─────────────────────────────────────────────────────────────────
    @Value("${ocsp.rsa.root-cert:}")     private String rsaRootCertPath;
    @Value("${ocsp.rsa.ca-cert:}")       private String rsaCaCertPath;
    @Value("${ocsp.rsa.responder-cert:}") private String rsaResponderCertPath;
    @Value("${ocsp.rsa.responder-key:}")  private String rsaResponderKeyPath;
    @Value("${ocsp.rsa.key-password:}")   private String rsaKeyPassword;

    // ── ECDSA ────────────────────────────────────────────────────────────────
    @Value("${ocsp.ec.root-cert:}")      private String ecRootCertPath;
    @Value("${ocsp.ec.ca-cert:}")        private String ecCaCertPath;
    @Value("${ocsp.ec.responder-cert:}")  private String ecResponderCertPath;
    @Value("${ocsp.ec.responder-key:}")   private String ecResponderKeyPath;
    @Value("${ocsp.ec.key-password:}")    private String ecKeyPassword;

    /**
     * BouncyCastle Provider 등록 및 OcspResponderService 에 CA 체인 주입.
     * Spring Boot 기동 직후 실행된다.
     */
    @Bean
    public ApplicationRunner ocspInitializer(OcspResponderService responderService) {
        return args -> {
            Security.addProvider(new BouncyCastleProvider());

            // RSA 체인 로드
            if (!rsaCaCertPath.isEmpty()) {
                try {
                    X509Certificate caCert   = loadCert(rsaCaCertPath);
                    X509Certificate respCert = loadCert(rsaResponderCertPath);
                    PrivateKey      respKey  = loadKey(rsaResponderKeyPath,
                                                       rsaKeyPassword.toCharArray());
                    responderService.setRsaChain(caCert, respCert, respKey);
                    log.info("[OCSP] RSA 체인 로드 완료: ca={}", rsaCaCertPath);
                } catch (Exception e) {
                    log.error("[OCSP] RSA 체인 로드 실패: {}", e.getMessage(), e);
                }
            } else {
                log.warn("[OCSP] RSA CA 경로 미설정 — RSA 체인 비활성화");
            }

            // RSA Root CA 로드
            if (!rsaRootCertPath.isEmpty()) {
                try {
                    X509Certificate rootCert = loadCert(rsaRootCertPath);
                    responderService.setRsaRootCert(rootCert);
                    log.info("[OCSP] RSA Root CA 로드 완료: root={}", rsaRootCertPath);
                } catch (Exception e) {
                    log.error("[OCSP] RSA Root CA 로드 실패: {}", e.getMessage(), e);
                }
            }

            // ECDSA 체인 로드
            if (!ecCaCertPath.isEmpty()) {
                try {
                    X509Certificate caCert   = loadCert(ecCaCertPath);
                    X509Certificate respCert = loadCert(ecResponderCertPath);
                    PrivateKey      respKey  = loadKey(ecResponderKeyPath,
                                                       ecKeyPassword.toCharArray());
                    responderService.setEcChain(caCert, respCert, respKey);
                    log.info("[OCSP] ECDSA 체인 로드 완료: ca={}", ecCaCertPath);
                } catch (Exception e) {
                    log.error("[OCSP] ECDSA 체인 로드 실패: {}", e.getMessage(), e);
                }
            } else {
                log.warn("[OCSP] ECDSA CA 경로 미설정 — ECDSA 체인 비활성화");
            }

            // ECDSA Root CA 로드
            if (!ecRootCertPath.isEmpty()) {
                try {
                    X509Certificate rootCert = loadCert(ecRootCertPath);
                    responderService.setEcRootCert(rootCert);
                    log.info("[OCSP] ECDSA Root CA 로드 완료: root={}", ecRootCertPath);
                } catch (Exception e) {
                    log.error("[OCSP] ECDSA Root CA 로드 실패: {}", e.getMessage(), e);
                }
            }
        };
    }

    // ── PEM 유틸 ─────────────────────────────────────────────────────────────

    private X509Certificate loadCert(String path) throws Exception {
        try (PEMParser parser = new PEMParser(new FileReader(path))) {
            Object obj = parser.readObject();
            return new JcaX509CertificateConverter()
                    .setProvider("BC")
                    .getCertificate(
                            (org.bouncycastle.cert.X509CertificateHolder) obj);
        }
    }

    private PrivateKey loadKey(String path, char[] password) throws Exception {
        try (PEMParser parser = new PEMParser(new FileReader(path))) {
            Object obj = parser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (obj instanceof PEMEncryptedKeyPair enc) {
                // 암호화된 키
                obj = enc.decryptKeyPair(
                        new JcePEMDecryptorProviderBuilder().build(password));
            }
            return converter.getKeyPair((PEMKeyPair) obj).getPrivate();
        }
    }
}
