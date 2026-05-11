package kr.co.kevit.localcsms.eai.sign.config;

import kr.co.kevit.localcsms.eai.sign.service.CertSigningService;
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
 * RSA CA / ECDSA CA 키·인증서 로드 및 CertSigningService 초기화.
 *
 * application.yml 의 sign-eai.rsa.* / sign-eai.ec.* 경로에서 PEM 파일을 읽는다.
 * 경로가 비어 있으면 해당 CA 체인은 비활성화된다.
 */
@Configuration
public class SignEaiConfig {

    private static final Logger log = LoggerFactory.getLogger(SignEaiConfig.class);

    // ── RSA ──────────────────────────────────────────────────────────────────
    @Value("${sign-eai.rsa.ca-cert:}")      private String rsaCaCertPath;
    @Value("${sign-eai.rsa.ca-key:}")       private String rsaCaKeyPath;
    @Value("${sign-eai.rsa.key-password:}") private String rsaKeyPassword;

    // ── ECDSA ─────────────────────────────────────────────────────────────────
    @Value("${sign-eai.ec.ca-cert:}")       private String ecCaCertPath;
    @Value("${sign-eai.ec.ca-key:}")        private String ecCaKeyPath;
    @Value("${sign-eai.ec.key-password:}")  private String ecKeyPassword;

    /**
     * BouncyCastle Provider 등록 및 CertSigningService 에 CA 체인 주입.
     * Spring Boot 기동 직후 실행된다.
     */
    @Bean
    public ApplicationRunner signEaiInitializer(CertSigningService signingService) {
        return args -> {
            Security.addProvider(new BouncyCastleProvider());

            // RSA 체인 로드
            if (!rsaCaCertPath.isEmpty()) {
                try {
                    X509Certificate caCert = loadCert(rsaCaCertPath);
                    PrivateKey caKey = loadKey(rsaCaKeyPath, rsaKeyPassword.toCharArray());
                    signingService.setRsaChain(caCert, caKey);
                    log.info("[SIGN] RSA CA 체인 로드 완료: {}", rsaCaCertPath);
                } catch (Exception e) {
                    log.error("[SIGN] RSA CA 체인 로드 실패: {}", e.getMessage(), e);
                }
            } else {
                log.warn("[SIGN] RSA CA 경로 미설정 — RSA 비활성화");
            }

            // ECDSA 체인 로드
            if (!ecCaCertPath.isEmpty()) {
                try {
                    X509Certificate caCert = loadCert(ecCaCertPath);
                    PrivateKey caKey = loadKey(ecCaKeyPath, ecKeyPassword.toCharArray());
                    signingService.setEcChain(caCert, caKey);
                    log.info("[SIGN] ECDSA CA 체인 로드 완료: {}", ecCaCertPath);
                } catch (Exception e) {
                    log.error("[SIGN] ECDSA CA 체인 로드 실패: {}", e.getMessage(), e);
                }
            } else {
                log.warn("[SIGN] ECDSA CA 경로 미설정 — ECDSA 비활성화");
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
                // PKCS#8 암호화 키 (openssl genpkey / openssl pkcs8 형식)
                InputDecryptorProvider decProv = new JcePKCSPBEInputDecryptorProviderBuilder()
                        .setProvider("BC").build(password);
                PrivateKeyInfo pkInfo = enc.decryptPrivateKeyInfo(decProv);
                return converter.getPrivateKey(pkInfo);
            } else if (obj instanceof PEMEncryptedKeyPair enc) {
                // 전통 OpenSSL 암호화 키 (DEK-Info 헤더)
                PEMKeyPair kp = enc.decryptKeyPair(new JcePEMDecryptorProviderBuilder().build(password));
                return converter.getKeyPair(kp).getPrivate();
            } else if (obj instanceof PEMKeyPair kp) {
                // 비암호화 키
                return converter.getKeyPair(kp).getPrivate();
            } else if (obj instanceof PrivateKeyInfo pkInfo) {
                // 비암호화 PKCS#8 키
                return converter.getPrivateKey(pkInfo);
            } else {
                throw new IllegalArgumentException("지원하지 않는 키 형식: " + obj.getClass().getName());
            }
        }
    }
}
