package kr.co.kevit.localcsms.eai.v2gsign.config;

import kr.co.kevit.localcsms.eai.v2gsign.service.CertSigningService;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.jcajce.JcePKCSPBEInputDecryptorProviderBuilder;
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
 * V2G ECDSA CA 키·인증서 로드 및 CertSigningService 초기화.
 *
 * application.yml 의 v2g-sign-eai.ec.* 경로에서 PEM 파일을 읽는다.
 */
@Configuration
public class V2gSignEaiConfig {

    private static final Logger log = LoggerFactory.getLogger(V2gSignEaiConfig.class);

    // ── ECDSA V2G CA ──────────────────────────────────────────────────────────
    @Value("${v2g-sign-eai.ec.ca-cert:}")       private String ecCaCertPath;
    @Value("${v2g-sign-eai.ec.ca-key:}")        private String ecCaKeyPath;
    @Value("${v2g-sign-eai.ec.key-password:}")  private String ecKeyPassword;

    /**
     * BouncyCastle Provider 등록 및 CertSigningService 에 V2G ECDSA CA 체인 주입.
     * Spring Boot 기동 직후 실행된다.
     */
    @Bean
    public ApplicationRunner v2gSignEaiInitializer(CertSigningService signingService) {
        return args -> {
            Security.addProvider(new BouncyCastleProvider());

            // V2G ECDSA 체인 로드
            if (!ecCaCertPath.isEmpty()) {
                try {
                    X509Certificate caCert = loadCert(ecCaCertPath);
                    PrivateKey caKey = loadKey(ecCaKeyPath, ecKeyPassword.toCharArray());
                    signingService.setEcChain(caCert, caKey);
                    log.info("[V2G-SIGN] V2G ECDSA CA 체인 로드 완료: {}", ecCaCertPath);
                } catch (Exception e) {
                    log.error("[V2G-SIGN] V2G ECDSA CA 체인 로드 실패: {}", e.getMessage(), e);
                }
            } else {
                log.warn("[V2G-SIGN] V2G ECDSA CA 경로 미설정 — 서명 불가");
            }
        };
    }

    // ── PEM 유틸 ──────────────────────────────────────────────────────────────

    private X509Certificate loadCert(String path) throws Exception {
        try (PEMParser parser = new PEMParser(new FileReader(path))) {
            Object obj = parser.readObject();
            return new JcaX509CertificateConverter()
                    .setProvider("BC")
                    .getCertificate((org.bouncycastle.cert.X509CertificateHolder) obj);
        }
    }

    private PrivateKey loadKey(String path, char[] password) throws Exception {
        try (PEMParser parser = new PEMParser(new FileReader(path))) {
            Object obj = parser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (obj instanceof PKCS8EncryptedPrivateKeyInfo enc) {
                InputDecryptorProvider decProv = new JcePKCSPBEInputDecryptorProviderBuilder()
                        .setProvider("BC").build(password);
                PrivateKeyInfo pkInfo = enc.decryptPrivateKeyInfo(decProv);
                return converter.getPrivateKey(pkInfo);
            } else if (obj instanceof PEMEncryptedKeyPair enc) {
                PEMKeyPair kp = enc.decryptKeyPair(new JcePEMDecryptorProviderBuilder().build(password));
                return converter.getKeyPair(kp).getPrivate();
            } else if (obj instanceof PEMKeyPair kp) {
                return converter.getKeyPair(kp).getPrivate();
            } else if (obj instanceof PrivateKeyInfo pkInfo) {
                return converter.getPrivateKey(pkInfo);
            } else {
                throw new IllegalArgumentException("지원하지 않는 키 형식: " + obj.getClass().getName());
            }
        }
    }
}
