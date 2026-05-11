package kr.co.kevit.localcsms.eai.ocsp.service;

import kr.co.kevit.localcsms.eai.ocsp.store.CertificateStatusStore;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HexFormat;

/**
 * OCSP 응답 생성 서비스.
 *
 * RSA CA / ECDSA CA 두 체인을 모두 지원한다.
 * OCSPRequest 의 issuerKeyHash 를 비교해 어느 CA 체인인지 자동 판별하고
 * 해당 CA 의 서명키로 OCSPResponse 에 서명한다.
 *
 * issuerKeyHash 일치 순서: RSA CA → ECDSA CA → UNKNOWN
 */
@Service
public class OcspResponderService {

    private static final Logger log = LoggerFactory.getLogger(OcspResponderService.class);

    // ── RSA CA 체인 ──────────────────────────────────────────────────────────

    /** RSA Root CA 인증서 (중간 CA 인증서의 issuer) */
    private X509Certificate rsaRootCert;
    /** RSA 중간 CA 인증서 */
    private X509Certificate rsaCaCert;
    /** RSA OCSP 서명 인증서 (CA 와 동일하거나 위임 인증서) */
    private X509Certificate rsaResponderCert;
    /** RSA OCSP 서명 개인키 */
    private PrivateKey rsaResponderKey;
    /** 서명 알고리즘 */
    private static final String RSA_SIGN_ALG = "SHA256withRSA";

    // ── ECDSA CA 체인 ────────────────────────────────────────────────────────

    /** ECDSA Root CA 인증서 (중간 CA 인증서의 issuer) */
    private X509Certificate ecRootCert;
    /** ECDSA 중간 CA 인증서 */
    private X509Certificate ecCaCert;
    /** ECDSA OCSP 서명 인증서 */
    private X509Certificate ecResponderCert;
    /** ECDSA OCSP 서명 개인키 */
    private PrivateKey ecResponderKey;
    private static final String ECDSA_SIGN_ALG = "SHA256withECDSA";

    // ── 상태 저장소 ──────────────────────────────────────────────────────────

    private final CertificateStatusStore statusStore;

    public OcspResponderService(CertificateStatusStore statusStore) {
        this.statusStore = statusStore;
    }

    // ── 초기화 (OcspConfig 에서 호출) ────────────────────────────────────────

    public void setRsaChain(X509Certificate caCert, X509Certificate respCert, PrivateKey respKey) {
        this.rsaCaCert = caCert;
        this.rsaResponderCert = respCert;
        this.rsaResponderKey = respKey;
    }

    public void setEcChain(X509Certificate caCert, X509Certificate respCert, PrivateKey respKey) {
        this.ecCaCert = caCert;
        this.ecResponderCert = respCert;
        this.ecResponderKey = respKey;
    }

    public void setRsaRootCert(X509Certificate rootCert) {
        this.rsaRootCert = rootCert;
    }

    public void setEcRootCert(X509Certificate rootCert) {
        this.ecRootCert = rootCert;
    }

    // ── 핵심 응답 로직 ───────────────────────────────────────────────────────

    /**
     * OCSPRequest 바이트 → OCSPResponse 바이트
     *
     * @param requestBytes HTTP 요청 본문
     * @return HTTP 응답 본문 (application/ocsp-response)
     */
    public byte[] respond(byte[] requestBytes) throws Exception {

        // [1] 요청 파싱
        OCSPReq ocspReq = new OCSPReq(requestBytes);
        Req[] reqs = ocspReq.getRequestList();

        if (reqs == null || reqs.length == 0) {
            log.warn("[OCSP] 요청 목록 없음 → MALFORMED_REQUEST");
            return buildError(OCSPRespBuilder.MALFORMED_REQUEST);
        }

        // [2] DigestCalculatorProvider (SHA-1: OCSP CertificateID 표준)
        DigestCalculatorProvider digCalc = new JcaDigestCalculatorProviderBuilder().setProvider("BC").build();

        // [3] 첫 번째 요청의 issuerKeyHash 로 CA 체인 식별
        CertificateID firstId = reqs[0].getCertID();
        CaChain chain = resolveChain(firstId, digCalc);

        if (chain == null) {
            log.warn("[OCSP] 알 수 없는 issuer → UNAUTHORIZED");
            return buildError(OCSPRespBuilder.UNAUTHORIZED);
        }
        log.info("[OCSP] 체인 식별: {}", chain.algorithm);

        // [4] 응답 빌더
        BasicOCSPRespBuilder respBuilder = new org.bouncycastle.cert.ocsp.BasicOCSPRespBuilder(
                new org.bouncycastle.cert.ocsp.RespID(
                        new JcaX509CertificateHolder(chain.responderCert).getSubject()));

        // [4-1] 요청 nonce 가 있으면 응답에 echo (Apache OpenSSL 3.0 호환)
        Extension nonceExt = ocspReq.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
        if (nonceExt != null) {
            ExtensionsGenerator extGen = new ExtensionsGenerator();
            extGen.addExtension(nonceExt);
            respBuilder.setResponseExtensions(extGen.generate());
            log.debug("[OCSP] nonce echo 완료");
        }

        Date now = new Date();
        Date nextUpdate = new Date(now.getTime() + 60L * 60 * 1000); // 1시간 후

        // [5] 각 serial 상태 처리
        for (Req req : reqs) {
            BigInteger serial = req.getCertID().getSerialNumber();
            CertificateStatusStore.CertStatus status = statusStore.getStatus(serial);

            switch (status) {
                case GOOD -> {
                    respBuilder.addResponse(req.getCertID(), CertificateStatus.GOOD,
                            now, nextUpdate, null);
                    log.info("[OCSP] serial={} → GOOD ({})", serial.toString(16), chain.algorithm);
                }
                case REVOKED -> {
                    CertificateStatusStore.RevokedEntry entry = statusStore.getRevokedEntry(serial);
                    respBuilder.addResponse(req.getCertID(),
                            new RevokedStatus(entry.revokedAt(), entry.reason()),
                            now, nextUpdate, null);
                    log.info("[OCSP] serial={} → REVOKED reason={} ({})",
                            serial.toString(16), entry.reason(), chain.algorithm);
                }
                case UNKNOWN -> {
                    respBuilder.addResponse(req.getCertID(), new UnknownStatus(),
                            now, nextUpdate, null);
                    log.warn("[OCSP] serial={} → UNKNOWN ({})", serial.toString(16), chain.algorithm);
                }
            }
        }

        // [6] CA 체인에 맞는 서명 알고리즘으로 서명
        X509CertificateHolder[] certChain = {
                new JcaX509CertificateHolder(chain.responderCert),
                new JcaX509CertificateHolder(chain.caCert)
        };

        BasicOCSPResp basicResp = respBuilder.build(
                new JcaContentSignerBuilder(chain.sigAlgorithm)
                        .setProvider("BC")
                        .build(chain.responderKey),
                certChain,
                now);

        OCSPResp ocspResp = new OCSPRespBuilder()
                .build(OCSPRespBuilder.SUCCESSFUL, basicResp);

        return ocspResp.getEncoded();
    }

    // ── CA 체인 식별 ─────────────────────────────────────────────────────────

    /**
     * OCSPRequest 의 issuerKeyHash / issuerNameHash 를 RSA CA, ECDSA CA 와 비교해
     * 일치하는 CaChain 을 반환한다. 일치하지 않으면 null.
     */
    private CaChain resolveChain(CertificateID certId,
            DigestCalculatorProvider digCalc) throws Exception {

        HexFormat hex = HexFormat.of();
        String reqHashAlg = certId.getHashAlgOID().getId();
        String reqNameHash = hex.formatHex(certId.getIssuerNameHash());
        String reqKeyHash  = hex.formatHex(certId.getIssuerKeyHash());
        log.debug("[OCSP] 요청 CertificateID: hashAlg={} issuerNameHash={} issuerKeyHash={} serial={}",
                reqHashAlg, reqNameHash, reqKeyHash, certId.getSerialNumber().toString(16));

        // [1] 중간 CA 매칭 (end-entity 인증서 조회)
        if (rsaCaCert != null) {
            boolean matched = certId.matchesIssuer(new JcaX509CertificateHolder(rsaCaCert), digCalc);
            log.debug("[OCSP] RSA 중간CA 비교: subject={} matched={}", rsaCaCert.getSubjectX500Principal(), matched);
            if (matched) {
                return new CaChain(rsaCaCert, rsaResponderCert, rsaResponderKey,
                        RSA_SIGN_ALG, "RSA");
            }
        }
        if (ecCaCert != null) {
            boolean matched = certId.matchesIssuer(new JcaX509CertificateHolder(ecCaCert), digCalc);
            log.debug("[OCSP] ECDSA 중간CA 비교: subject={} matched={}", ecCaCert.getSubjectX500Principal(), matched);
            if (matched) {
                return new CaChain(ecCaCert, ecResponderCert, ecResponderKey,
                        ECDSA_SIGN_ALG, "ECDSA");
            }
        }

        // [2] Root CA 매칭 (중간 CA 인증서 자체의 상태 조회)
        if (rsaRootCert != null && rsaResponderCert != null) {
            boolean matched = certId.matchesIssuer(new JcaX509CertificateHolder(rsaRootCert), digCalc);
            log.debug("[OCSP] RSA RootCA 비교: subject={} matched={}", rsaRootCert.getSubjectX500Principal(), matched);
            if (matched) {
                return new CaChain(rsaRootCert, rsaResponderCert, rsaResponderKey,
                        RSA_SIGN_ALG, "RSA-Root");
            }
        }
        if (ecRootCert != null && ecResponderCert != null) {
            boolean matched = certId.matchesIssuer(new JcaX509CertificateHolder(ecRootCert), digCalc);
            log.debug("[OCSP] ECDSA RootCA 비교: subject={} matched={}", ecRootCert.getSubjectX500Principal(), matched);
            if (matched) {
                return new CaChain(ecRootCert, ecResponderCert, ecResponderKey,
                        ECDSA_SIGN_ALG, "ECDSA-Root");
            }
        }

        // 디버그: 매칭 실패 시 해시 비교 로그
        String algName = "1.3.14.3.2.26".equals(reqHashAlg) ? "SHA-1" : "SHA-256";
        if (rsaCaCert != null) logCaHashes("RSA 중간CA", rsaCaCert, algName, hex);
        if (ecCaCert != null) logCaHashes("ECDSA 중간CA", ecCaCert, algName, hex);
        if (rsaRootCert != null) logCaHashes("RSA RootCA", rsaRootCert, algName, hex);
        if (ecRootCert != null) logCaHashes("ECDSA RootCA", ecRootCert, algName, hex);

        return null;
    }

    private void logCaHashes(String label, X509Certificate caCert,
                             String algName, HexFormat hex) {
        try {
            MessageDigest md = MessageDigest.getInstance(algName);
            org.bouncycastle.asn1.x509.SubjectPublicKeyInfo spki =
                    org.bouncycastle.asn1.x509.SubjectPublicKeyInfo.getInstance(
                            caCert.getPublicKey().getEncoded());
            byte[] keyHash = md.digest(spki.getPublicKeyData().getBytes());
            md.reset();
            byte[] nameHash = md.digest(
                    new JcaX509CertificateHolder(caCert).getSubject().getEncoded());
            log.debug("[OCSP] {} CA nameHash({})={}", label, algName, hex.formatHex(nameHash));
            log.debug("[OCSP] {} CA keyHash({})={}", label, algName, hex.formatHex(keyHash));
        } catch (Exception e) {
            log.error("[OCSP] {} CA 해시 계산 실패: {}", label, e.getMessage());
        }
    }

    // ── 오류 응답 ────────────────────────────────────────────────────────────

    private byte[] buildError(int status) throws Exception {
        return new OCSPRespBuilder().build(status, null).getEncoded();
    }

    // ── 내부 레코드 ──────────────────────────────────────────────────────────

    /** 식별된 CA 체인 정보 (RSA or ECDSA) */
    private record CaChain(
            X509Certificate caCert,
            X509Certificate responderCert,
            PrivateKey responderKey,
            String sigAlgorithm,
            String algorithm) {
    }
}
