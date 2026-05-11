package kr.co.kevit.localcsms.eai.sign.service;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.asn1.misc.NetscapeCertType;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * X.509 인증서 서명 서비스.
 *
 * CSR 의 OID 로 RSA / ECDSA CA 체인을 선택하고,
 * BouncyCastle 로 X.509 v3 인증서를 생성한다.
 * 서명 완료 후 CertSenderService 로 전송을 위임한다.
 *
 * signAndSend() 는 @Async 로 호출되어 HTTP 응답과 독립적으로 실행된다.
 *
 * 생성 인증서 확장 (openssl.cnf usr_cert 기준):
 * basicConstraints = CA:FALSE (critical)
 * keyUsage = critical, nonRepudiation, digitalSignature, keyEncipherment
 * extendedKeyUsage = clientAuth, emailProtection
 * subjectKeyIdentifier = hash
 * authorityKeyIdentifier = keyid,issuer
 * authorityInfoAccess = OCSP;URI:{sign-eai.ocsp-url}
 */
@Service
public class CertSigningService {

    private static final Logger log = LoggerFactory.getLogger(CertSigningService.class);

    @Value("${sign-eai.csr-save-path:/root/ca/intermediate/csr/}")
    private String csrSavePath;

    @Value("${sign-eai.cert-save-path:/root/ca/intermediate/cert/}")
    private String certSavePath;

    @Value("${sign-eai.cert-validity-days:375}")
    private int validityDays;

    @Value("${sign-eai.ocsp-url:http://ocsp.kevit.co.kr:8080}")
    private String ocspUrl;

    // ── CA 체인 (SignEaiConfig 에서 주입) ────────────────────────────────────
    private X509Certificate rsaCaCert;
    private PrivateKey rsaCaKey;
    private X509Certificate ecCaCert;
    private PrivateKey ecCaKey;

    private final CertSenderService senderService;

    public CertSigningService(CertSenderService senderService) {
        this.senderService = senderService;
    }

    public void setRsaChain(X509Certificate caCert, PrivateKey caKey) {
        this.rsaCaCert = caCert;
        this.rsaCaKey = caKey;
    }

    public void setEcChain(X509Certificate caCert, PrivateKey caKey) {
        this.ecCaCert = caCert;
        this.ecCaKey = caKey;
    }

    // ── 비동기 서명·전송 ──────────────────────────────────────────────────────

    /**
     * CSR 검증 통과 직후 비동기로 호출된다.
     *
     * 1. CSR 파일 저장
     * 2. OID 로 CA 체인 선택 (RSA / ECDSA)
     * 3. X.509 v3 인증서 서명
     * 4. 인증서 PEM 파일 저장
     * 5. 충전기로 전송 위임
     */
    @Async
    public void signAndSend(String csId, String certificateType, PKCS10CertificationRequest csr) {
        try {
            // 1. CSR 파일 저장
            saveCsr(csId, csr);

            // 2. OID 로 CA 체인 선택
            String oid = csr.getSignatureAlgorithm().getAlgorithm().getId();
            boolean isRsa = CSRValidationService.OID_RSA.equals(oid);

            X509Certificate caCert = isRsa ? rsaCaCert : ecCaCert;
            PrivateKey caKey = isRsa ? rsaCaKey : ecCaKey;
            String sigAlg = isRsa ? "SHA256withRSA" : "SHA256withECDSA";

            if (caCert == null || caKey == null) {
                log.error("[SIGN] csId={} {} CA 체인 미설정 — 서명 불가",
                        csId, isRsa ? "RSA" : "ECDSA");
                return;
            }

            // 3. 인증서 서명
            log.info("[SIGN] csId={} {} 서명 시작", csId, sigAlg);
            String certPem = buildCertificate(csr, caCert, caKey, sigAlg);

            // 4. SUB CA 인증서를 체인에 추가 (충전기 인증서 + 중간 CA 인증서)
            String caCertPem = toPem(caCert);
            String chainPem = certPem + caCertPem;
            log.info("[SIGN] csId={} 체인 구성 완료 (충전기 인증서 + SUB CA)", csId);

            // 5. 체인 인증서 파일 저장
            String certFilePath = certSavePath + csId + ".pem";
            Files.createDirectories(Paths.get(certSavePath));
            Files.writeString(Paths.get(certFilePath), chainPem,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("[SIGN] csId={} 인증서 저장: {}", csId, certFilePath);

            // 6. 전송 위임 (체인 PEM)
            senderService.send(csId, certificateType, chainPem);

        } catch (Exception e) {
            log.error("[SIGN] csId={} 서명 처리 실패: {}", csId, e.getMessage(), e);
        }
    }

    // ── 인증서 생성 ───────────────────────────────────────────────────────────

    private String buildCertificate(PKCS10CertificationRequest csr,
            X509Certificate caCert,
            PrivateKey caKey,
            String sigAlg) throws Exception {

        JcaPKCS10CertificationRequest jcaCsr = new JcaPKCS10CertificationRequest(csr).setProvider("BC");

        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + (long) validityDays * 86_400_000L);
        BigInteger serial = new BigInteger(64, new SecureRandom());

        JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                caCert,
                serial,
                notBefore,
                notAfter,
                csr.getSubject(),
                jcaCsr.getPublicKey());

        // basicConstraints = CA:FALSE
        builder.addExtension(Extension.basicConstraints, false,
                new BasicConstraints(false));

        // nsCertType = client, email
        builder.addExtension(MiscObjectIdentifiers.netscapeCertType, false,
                new NetscapeCertType(NetscapeCertType.sslClient | NetscapeCertType.smime));

        // nsComment = "KEVIT CS Certificate"
        builder.addExtension(new ASN1ObjectIdentifier("2.16.840.1.113730.1.13"), false,
                new DERIA5String("KEVIT CS Certificate"));

        // keyUsage = critical, nonRepudiation, digitalSignature, keyEncipherment
        builder.addExtension(Extension.keyUsage, true,
                new KeyUsage(KeyUsage.nonRepudiation
                        | KeyUsage.digitalSignature
                        | KeyUsage.keyEncipherment));

        // extendedKeyUsage = clientAuth, emailProtection
        builder.addExtension(Extension.extendedKeyUsage, false,
                new ExtendedKeyUsage(new KeyPurposeId[] {
                        KeyPurposeId.id_kp_clientAuth,
                        KeyPurposeId.id_kp_emailProtection }));

        // subjectKeyIdentifier = hash
        builder.addExtension(Extension.subjectKeyIdentifier, false,
                extUtils.createSubjectKeyIdentifier(jcaCsr.getPublicKey()));

        // authorityKeyIdentifier = keyid,issuer
        builder.addExtension(Extension.authorityKeyIdentifier, false,
                extUtils.createAuthorityKeyIdentifier(caCert));

        // authorityInfoAccess = OCSP;URI:{ocspUrl}
        if (ocspUrl != null && !ocspUrl.isBlank()) {
            GeneralName ocspName = new GeneralName(GeneralName.uniformResourceIdentifier, ocspUrl);
            AuthorityInformationAccess aia = new AuthorityInformationAccess(
                    X509ObjectIdentifiers.id_ad_ocsp, ocspName);
            builder.addExtension(Extension.authorityInfoAccess, false, aia);
        }

        ContentSigner signer = new JcaContentSignerBuilder(sigAlg)
                .setProvider("BC")
                .build(caKey);

        X509Certificate cert = new JcaX509CertificateConverter()
                .setProvider("BC")
                .getCertificate(builder.build(signer));

        // PEM 인코딩
        StringWriter sw = new StringWriter();
        try (JcaPEMWriter pw = new JcaPEMWriter(sw)) {
            pw.writeObject(cert);
        }
        return sw.toString();
    }

    // ── CA 인증서 PEM 변환 ──────────────────────────────────────────────────────

    private String toPem(X509Certificate cert) throws Exception {
        StringWriter sw = new StringWriter();
        try (JcaPEMWriter pw = new JcaPEMWriter(sw)) {
            pw.writeObject(cert);
        }
        return sw.toString();
    }

    // ── CSR 파일 저장 ─────────────────────────────────────────────────────────

    private void saveCsr(String csId, PKCS10CertificationRequest csr) throws Exception {
        Files.createDirectories(Paths.get(csrSavePath));
        StringWriter sw = new StringWriter();
        try (JcaPEMWriter pw = new JcaPEMWriter(sw)) {
            pw.writeObject(csr);
        }
        String csrFilePath = csrSavePath + csId + ".csr";
        Files.writeString(Paths.get(csrFilePath), sw.toString(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        log.info("[SIGN] csId={} CSR 저장: {}", csId, csrFilePath);
    }
}
