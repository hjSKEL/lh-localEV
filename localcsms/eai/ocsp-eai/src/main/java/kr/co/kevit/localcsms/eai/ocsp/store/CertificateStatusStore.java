package kr.co.kevit.localcsms.eai.ocsp.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 인증서 폐기 상태 저장소.
 *
 * 운영 환경에서는 DB 또는 CRL 파일로 교체한다.
 * serial → RevokedEntry 매핑. 없으면 GOOD.
 */
@Component
public class CertificateStatusStore {

    private static final Logger log = LoggerFactory.getLogger(CertificateStatusStore.class);

    public enum CertStatus { GOOD, REVOKED, UNKNOWN }

    /**
     * 폐기 정보
     *
     * @param revokedAt 폐기 시각
     * @param reason    CRL Reason Code
     *                  0=unspecified  1=keyCompromise  3=affiliationChanged
     *                  4=superseded   5=cessationOfOperation
     */
    public record RevokedEntry(Date revokedAt, int reason) {}

    private static final int MAX_REVOKED_ENTRIES = 100_000;

    // serial(BigInteger) → 폐기 정보
    private final ConcurrentHashMap<BigInteger, RevokedEntry> revokedMap = new ConcurrentHashMap<>();

    // ── 상태 조회 ────────────────────────────────────────────────────────────

    public CertStatus getStatus(BigInteger serial) {
        if (revokedMap.containsKey(serial)) {
            return CertStatus.REVOKED;
        }
        return CertStatus.GOOD;
    }

    public RevokedEntry getRevokedEntry(BigInteger serial) {
        return revokedMap.get(serial);
    }

    // ── 폐기 등록 / 복구 ─────────────────────────────────────────────────────

    /**
     * 인증서 폐기 등록
     * @param serial serial 번호
     * @param reason CRL Reason Code (0~8)
     */
    public void revoke(BigInteger serial, int reason) {
        if (revokedMap.size() >= MAX_REVOKED_ENTRIES) {
            log.warn("[OCSP] 폐기 목록 최대 크기({}) 도달 — 등록 거부 serial={}", MAX_REVOKED_ENTRIES, serial.toString(16));
            return;
        }
        revokedMap.put(serial, new RevokedEntry(new Date(), reason));
        log.info("[OCSP] 인증서 폐기 등록: serial={} reason={} (현재 {}건)", serial.toString(16), reason, revokedMap.size());
    }

    /**
     * 폐기 취소 (운용상 필요 시)
     */
    public void reinstate(BigInteger serial) {
        revokedMap.remove(serial);
        log.info("[OCSP] 인증서 폐기 취소: serial={}", serial.toString(16));
    }

    /** 현재 폐기 목록 크기 */
    public int revokedCount() {
        return revokedMap.size();
    }
}
