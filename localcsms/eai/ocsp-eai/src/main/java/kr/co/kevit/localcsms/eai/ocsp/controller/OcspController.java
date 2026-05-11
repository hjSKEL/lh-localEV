package kr.co.kevit.localcsms.eai.ocsp.controller;

import kr.co.kevit.localcsms.eai.ocsp.service.OcspResponderService;
import kr.co.kevit.localcsms.eai.ocsp.store.CertificateStatusStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * OCSP HTTP 엔드포인트
 *
 * RFC 6960 §A.1 POST 방식: POST /ocsp (application/ocsp-request)
 * RFC 6960 §A.1 GET 방식: GET /ocsp/{base64}
 *
 * 관리 API:
 * POST /ocsp/admin/revoke/{serial}?reason=1 — 폐기 등록
 * POST /ocsp/admin/reinstate/{serial} — 폐기 취소
 */
@RestController
@RequestMapping("/")
public class OcspController {

    private static final Logger log = LoggerFactory.getLogger(OcspController.class);

    private static final String OCSP_REQUEST_TYPE = "application/ocsp-request";
    private static final String OCSP_RESPONSE_TYPE = "application/ocsp-response";

    private final OcspResponderService responderService;
    private final CertificateStatusStore statusStore;

    public OcspController(OcspResponderService responderService,
            CertificateStatusStore statusStore) {
        this.responderService = responderService;
        this.statusStore = statusStore;
    }

    // ── RFC 6960 POST ────────────────────────────────────────────────────────

    /**
     * POST /ocsp
     * Content-Type: application/ocsp-request
     *
     * RSA, ECDSA 모두 동일 엔드포인트로 수신.
     * OcspResponderService 가 issuerKeyHash 로 체인을 자동 식별한다.
     */
    @PostMapping(consumes = OCSP_REQUEST_TYPE, produces = OCSP_RESPONSE_TYPE)
    public ResponseEntity<byte[]> handlePost(@RequestBody byte[] requestBytes) {
        log.debug("[OCSP] POST 요청 수신 {} bytes", requestBytes.length);
        return process(requestBytes);
    }

    // ── RFC 6960 GET ─────────────────────────────────────────────────────────

    /**
     * GET /ocsp/{base64Request}
     * base64Request = URL-safe Base64(OCSPRequest bytes)
     *
     * 짧은 요청 (proxy/cache 친화적).
     */
    @GetMapping(value = "/{base64Request}", produces = OCSP_RESPONSE_TYPE)
    public ResponseEntity<byte[]> handleGet(
            @PathVariable String base64Request) {
        try {
            String decoded = URLDecoder.decode(base64Request, StandardCharsets.UTF_8);
            byte[] requestBytes = Base64.getDecoder().decode(decoded);
            log.debug("[OCSP] GET 요청 수신 {} bytes", requestBytes.length);
            return process(requestBytes);
        } catch (Exception e) {
            log.error("[OCSP] GET 요청 디코딩 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ── 관리 API ─────────────────────────────────────────────────────────────

    /**
     * POST /ocsp/admin/revoke/{serial}?reason=1
     *
     * @param serial 16진수 serial 번호 (예: 1A2B3C)
     * @param reason CRL Reason Code (기본 0=unspecified)
     */
    @PostMapping("/admin/revoke/{serial}")
    public ResponseEntity<String> revoke(
            @PathVariable String serial,
            @RequestParam(defaultValue = "0") int reason) {
        try {
            BigInteger serialNum = new BigInteger(serial, 16);
            statusStore.revoke(serialNum, reason);
            return ResponseEntity.ok(
                    "revoked: serial=" + serial + " reason=" + reason);
        } catch (Exception e) {
            log.error("[OCSP] 폐기 등록 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }

    /**
     * POST /ocsp/admin/reinstate/{serial}
     * 폐기 취소
     */
    @PostMapping("/admin/reinstate/{serial}")
    public ResponseEntity<String> reinstate(@PathVariable String serial) {
        try {
            BigInteger serialNum = new BigInteger(serial, 16);
            statusStore.reinstate(serialNum);
            return ResponseEntity.ok("reinstated: serial=" + serial);
        } catch (Exception e) {
            log.error("[OCSP] 폐기 취소 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body("error: " + e.getMessage());
        }
    }

    /**
     * GET /ocsp/admin/status
     * 현재 폐기 목록 크기 확인
     */
    @GetMapping("/admin/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok(
                "revokedCount=" + statusStore.revokedCount());
    }

    // ── 공통 처리 ────────────────────────────────────────────────────────────

    private ResponseEntity<byte[]> process(byte[] requestBytes) {
        try {
            byte[] responseBytes = responderService.respond(requestBytes);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(OCSP_RESPONSE_TYPE))
                    .body(responseBytes);
        } catch (Exception e) {
            log.error("[OCSP] 응답 생성 실패: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
