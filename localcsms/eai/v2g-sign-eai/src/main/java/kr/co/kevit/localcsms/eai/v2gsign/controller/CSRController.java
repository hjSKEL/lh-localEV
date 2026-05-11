package kr.co.kevit.localcsms.eai.v2gsign.controller;

import kr.co.kevit.localcsms.eai.v2gsign.dto.CSRRequest;
import kr.co.kevit.localcsms.eai.v2gsign.dto.CSRResponse;
import kr.co.kevit.localcsms.eai.v2gsign.service.CertSigningService;
import kr.co.kevit.localcsms.eai.v2gsign.service.CSRValidationService;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V2G CSR 수신 엔드포인트.
 *
 * POST /ca/csr
 * 요청: {"csId":"...", "csr":"-----BEGIN CERTIFICATE REQUEST-----\n...\n-----END CERTIFICATE REQUEST-----"}
 * 응답: {"result":"SUCCESS"} or {"result":"FAIL"}
 *
 * 검증 통과 시 즉시 SUCCESS 응답 후 비동기로 서명·전송.
 */
@RestController
@RequestMapping("/ca")
public class CSRController {

    private static final Logger log = LoggerFactory.getLogger(CSRController.class);

    private final CSRValidationService validationService;
    private final CertSigningService signingService;

    public CSRController(CSRValidationService validationService,
            CertSigningService signingService) {
        this.validationService = validationService;
        this.signingService = signingService;
    }

    @PostMapping("/csr")
    public ResponseEntity<CSRResponse> receiveCsr(@RequestBody CSRRequest request) {
        String csId = request.getCsId();
        String certificateType = request.getCertificateType();
        log.info("[V2G-SIGN] CSR 수신 csId={} certificateType={}", csId, certificateType);

        PKCS10CertificationRequest csr = validationService.validate(csId, request.getCsr());

        if (csr == null) {
            log.warn("[V2G-SIGN] csId={} 검증 실패 → FAIL", csId);
            return ResponseEntity.ok(new CSRResponse("FAIL"));
        }

        log.info("[V2G-SIGN] csId={} 검증 성공 → SUCCESS (비동기 서명 시작)", csId);
        signingService.signAndSend(csId, certificateType, csr);

        return ResponseEntity.ok(new CSRResponse("SUCCESS"));
    }
}
