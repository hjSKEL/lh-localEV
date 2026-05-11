package kr.co.kevit.localcsms.eai.v2gsign.service;

import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.StringReader;

/**
 * V2G CSR 정합성 검증 서비스.
 *
 * V2G 는 ECDSA 전용이므로 ecdsa-with-SHA256 OID 만 허용한다.
 */
@Service
public class CSRValidationService {

    private static final Logger log = LoggerFactory.getLogger(CSRValidationService.class);

    // ecdsa-with-SHA256
    public static final String OID_ECDSA = "1.2.840.10045.4.3.2";

    /**
     * CSR 검증.
     *
     * @return 검증 통과 시 파싱된 PKCS10CertificationRequest, 실패 시 null
     */
    public PKCS10CertificationRequest validate(String csId, String csrPem) {
        try {
            // 1. PEM 파싱
            PKCS10CertificationRequest csr;
            try (PEMParser parser = new PEMParser(new StringReader(csrPem))) {
                Object obj = parser.readObject();
                if (!(obj instanceof PKCS10CertificationRequest)) {
                    log.warn("[V2G-SIGN] csId={} PEM 파싱 실패: PKCS#10 형식 아님", csId);
                    return null;
                }
                csr = (PKCS10CertificationRequest) obj;
            }

            // 2. OID 확인 (ECDSA 전용)
            String oid = csr.getSignatureAlgorithm().getAlgorithm().getId();
            log.info("[V2G-SIGN] csId={} OID={} subject={}", csId, oid, csr.getSubject());

            if (!OID_ECDSA.equals(oid)) {
                log.warn("[V2G-SIGN] csId={} V2G 는 ECDSA 만 허용: OID={}", csId, oid);
                return null;
            }

            // 3. CSR 자체 서명 검증
            JcaPKCS10CertificationRequest jcaCsr =
                    new JcaPKCS10CertificationRequest(csr).setProvider("BC");

            boolean valid = csr.isSignatureValid(
                    new JcaContentVerifierProviderBuilder()
                            .setProvider("BC")
                            .build(jcaCsr.getPublicKey()));

            if (!valid) {
                log.warn("[V2G-SIGN] csId={} CSR 서명 검증 실패", csId);
                return null;
            }

            log.info("[V2G-SIGN] csId={} CSR 검증 완료", csId);
            return csr;

        } catch (Exception e) {
            log.error("[V2G-SIGN] csId={} CSR 검증 중 예외: {}", csId, e.getMessage(), e);
            return null;
        }
    }
}
