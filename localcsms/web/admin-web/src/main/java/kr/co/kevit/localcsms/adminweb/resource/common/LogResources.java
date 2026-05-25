/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.PropertyKey;
import kr.co.kevit.localcsms.common.util.loader.PropertyLoader;

/**
 * 진단로그 / 보안로그 업로드 수신 엔드포인트.
 *
 * <p>OCPP 2.1 use case N01 (Retrieve Log Information) — CSMS 가 충전기에 보낸
 * {@code GetLogRequest.log.remoteLocation} 으로 충전기가 HTTP POST 로 진단로그를 업로드한다.</p>
 *
 * <p>세션 인증을 우회하고 *HTTP Basic Auth* 만 강제. userinfo 없는 요청은 401,
 * 인증 성공 시 파일 저장 후 200.</p>
 *
 * <p>관련 TC: TC_N_102 (HTTP), TC_N_103 (HTTPS).</p>
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
@RestController
@RequestMapping("ws/log")
public class LogResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogResources.class);

    private static final String BASIC_PREFIX = "Basic ";
    private static final String AUTH_REALM   = "csms-log";

    @Value("${csms.log.username:username}")
    private String basicAuthUsername;

    @Value("${csms.log.password:password}")
    private String basicAuthPassword;

    /** OCPP 진단로그 업로드. CS 가 본 URL 로 HTTP POST 로 로그 바이너리를 송신. */
    @PostMapping(value = "/upload/{requestId}")
    public ResponseEntity<String> uploadDiagnosticsLog(@PathVariable("requestId") String requestId,
                                                       HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!isAuthorized(authHeader)) {
            LOGGER.warn("[LOG-UPLOAD] 401 Unauthorized requestId={} remoteAddr={} authHeaderPresent={}",
                    requestId, request.getRemoteAddr(), authHeader != null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"" + AUTH_REALM + "\"")
                    .body("Unauthorized");
        }

        try {
            File saved = saveStreamToFile(requestId, request);
            LOGGER.info("[LOG-UPLOAD] 200 OK requestId={} remoteAddr={} bytes={} path={}",
                    requestId, request.getRemoteAddr(), saved.length(), saved.getAbsolutePath());
            return ResponseEntity.ok("OK");
        } catch (IOException e) {
            LOGGER.error("[LOG-UPLOAD] save failed requestId=" + requestId + ": " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Save failed: " + e.getMessage());
        }
    }

    /** Authorization 헤더 → Basic Auth 검증 */
    private boolean isAuthorized(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(BASIC_PREFIX)) {
            return false;
        }
        String encoded = authHeader.substring(BASIC_PREFIX.length()).trim();
        String decoded;
        try {
            decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            return false;
        }
        int colon = decoded.indexOf(':');
        if (colon < 0) {
            return false;
        }
        String user = decoded.substring(0, colon);
        String pass = decoded.substring(colon + 1);
        return basicAuthUsername.equals(user) && basicAuthPassword.equals(pass);
    }

    /** 업로드 본문을 진단로그 디렉토리에 파일로 저장 */
    private File saveStreamToFile(String requestId, HttpServletRequest request) throws IOException {
        String baseDir = PropertyLoader.getInstance().getProperty(PropertyKey.OcppFolderLocation);
        File logDir = new File(baseDir, "logs");
        if (!logDir.exists() && !logDir.mkdirs()) {
            throw new IOException("로그 디렉토리 생성 실패: " + logDir.getAbsolutePath());
        }
        String fileName = sanitize(requestId) + "_" + DateUtils.getCurrentDateAsString(DateUtils.YYYYMMDDHHMMSSSSS) + ".log";
        File outFile = new File(logDir, fileName);
        InputStream in = request.getInputStream();
        if (in == null) {
            throw new IOException("요청 본문 스트림이 없습니다.");
        }
        try (InputStream src = in;
             FileOutputStream out = new FileOutputStream(outFile)) {
            StreamUtils.copy(src, out);
        }
        return outFile;
    }

    private String sanitize(String s) {
        if (s == null) return "unknown";
        return s.replaceAll("[^A-Za-z0-9_.-]", "_");
    }
}
