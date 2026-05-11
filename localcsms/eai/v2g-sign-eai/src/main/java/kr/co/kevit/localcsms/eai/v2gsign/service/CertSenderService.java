package kr.co.kevit.localcsms.eai.v2gsign.service;

import kr.co.kevit.localcsms.system.entity.domain.DaemonAccess;
import kr.co.kevit.localcsms.system.process.DaemonAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 서명된 V2G 인증서를 ocpp20-daemon 으로 전송하는 서비스.
 *
 * ocpp20-daemon POST /command/{cpCsId} 포맷:
 * {
 *   "action": "CertificateSigned",
 *   "payload": {
 *     "certificateType": "V2GCertificate",
 *     "certificateChain": "<PEM>"
 *   }
 * }
 *
 * ip/port 는 TB_SYDA001(DaemonAccess) 에서 csId 로 조회한다.
 */
@Service
public class CertSenderService {

    private static final Logger log = LoggerFactory.getLogger(CertSenderService.class);

    @Autowired
    private DaemonAccessService daemonAccessService;

    public void send(String csId, String certificateType, String certPem) {
        DaemonAccess da = daemonAccessService.retrieveDaemonAccessByCpCsId(csId);
        if (da == null) {
            log.error("[V2G-SIGN] DaemonAccess 없음 — 전송 불가 csId={}", csId);
            return;
        }

        String url = "http://" + da.getIp() + ":" + da.getPort() + "/command/" + csId;
        String body = buildBody(certificateType, certPem);

        try {
            log.info("[V2G-SIGN] CertificateSigned 전송 전 5초 대기 csId={}", csId);
            Thread.sleep(5_000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        log.info("[V2G-SIGN] CertificateSigned 전송 시작 csId={} url={}", csId, url);

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            if (code == 200) {
                log.info("[V2G-SIGN] CertificateSigned 전송 완료 csId={} HTTP={}", csId, code);
            } else {
                log.warn("[V2G-SIGN] CertificateSigned 전송 비정상 응답 csId={} HTTP={}", csId, code);
            }

        } catch (Exception e) {
            log.error("[V2G-SIGN] CertificateSigned 전송 실패 csId={}: {}", csId, e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private String buildBody(String certificateType, String certPem) {
        String escapedPem = certPem
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r\n", "\\n")
                .replace("\n", "\\n");
        return "{\"action\":\"CertificateSigned\","
                + "\"payload\":{"
                + "\"certificateChain\":\"" + escapedPem + "\""
                + "}}";
    }
}
