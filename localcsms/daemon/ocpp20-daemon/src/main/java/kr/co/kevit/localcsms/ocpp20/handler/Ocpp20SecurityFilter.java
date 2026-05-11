package kr.co.kevit.localcsms.ocpp20.handler;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * OCPP 2.0.1 Security Profile — WebSocket 핸드셰이크 전 HTTP 단계에서 Basic Auth 검증.
 *
 * Security Profile 레벨 (기동 인자 args[0]):
 *   0, 3 — 충전소 존재 여부만 확인
 *   1, 2 — Basic Auth 비밀번호까지 검증
 *
 * 비밀번호 불일치 또는 미등록 충전소이면 HTTP 401 Unauthorized 를 반환하고 연결을 차단한다.
 */
@Component
@Order(1)
public class Ocpp20SecurityFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(Ocpp20SecurityFilter.class);

    @Autowired private ChargingStationService chargingStationService;
    @Autowired private ApplicationArguments   applicationArguments;

    private boolean authRequired      = false;  // sp=1,2: Basic Auth 비밀번호 검증
    private boolean existenceRequired = false;  // sp=0,3: 충전소 존재 여부 확인

    @PostConstruct
    public void init() {
        List<String> args = applicationArguments.getNonOptionArgs();
        String sp = args.isEmpty() ? "" : args.get(0);
        authRequired      = "1".equals(sp) || "2".equals(sp);
        existenceRequired = "0".equals(sp) || "3".equals(sp);
        if (authRequired) {
            log.info("[OCPP20] Security Profile {} 활성화 — 연결 전 비밀번호 인증 필요", sp);
        } else if (existenceRequired) {
            log.info("[OCPP20] Security Profile {} 활성화 — 충전소 존재 여부 확인", sp);
        }
    }

    /**
     * /ocpp20/{cpId} WebSocket 핸드셰이크 요청에만 필터 적용.
     * 정적 리소스(control20.html, js/, error/) 및 REST API(/ocpp20/command/**)는 제외.
     * Upgrade: websocket 헤더 유무로 판별.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (!uri.startsWith("/ocpp20/")) return true;
        String upgrade = request.getHeader("Upgrade");
        return upgrade == null || !"websocket".equalsIgnoreCase(upgrade);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!authRequired && !existenceRequired) {
            filterChain.doFilter(request, response);
            return;
        }

        String path   = request.getRequestURI();
        String cpCsId = path.substring(path.lastIndexOf('/') + 1);

        if (authRequired) {
            // sp=1,2: Basic Auth — 비밀번호까지 검증
            if (!authenticate(request, cpCsId)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("WWW-Authenticate", "Basic realm=\"OCPP\"");
                log.warn("[OCPP20] 401 Unauthorized — 연결 거부: cpCsId={}", cpCsId);
                return;
            }
        } else {
            // sp=0,3: 존재 여부만 확인
            if (!checkExists(cpCsId)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("WWW-Authenticate", "Basic realm=\"OCPP\"");
                log.warn("[OCPP20] 401 Unauthorized — 충전소 없음: cpCsId={}", cpCsId);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // -------------------------------------------------------------------------

    /** sp=0,3: cpId+csId가 DB에 존재하는지만 확인 */
    private boolean checkExists(String cpCsId) {
        String[] parts = cpCsId.split("-", 2);
        if (parts.length < 2) {
            log.warn("[OCPP20] EXISTS 실패 — cpId/csId 분리 불가: {}", cpCsId);
            return false;
        }
        ChargingStation cs = chargingStationService.retrieveChargingStationByCpIdNCsId(parts[0], parts[1]);
        if (cs == null) {
            log.warn("[OCPP20] EXISTS 실패 — 충전소 없음: cpId={} csId={}", parts[0], parts[1]);
            return false;
        }
        log.info("[OCPP20] EXISTS 확인 성공: cpCsId={}", cpCsId);
        return true;
    }

    /** sp=1,2: Basic Auth 비밀번호까지 검증 */
    private boolean authenticate(HttpServletRequest request, String cpCsId) {
        // cpCsId 예: "IA0002-03" → cpId="IA0002", csId="03"
        String[] parts = cpCsId.split("-", 2);
        if (parts.length < 2) {
            log.warn("[OCPP20] AUTH 실패 — cpId/csId 분리 불가: {}", cpCsId);
            return false;
        }
        String cpId = parts[0];
        String csId = parts[1];

        // Authorization: Basic <base64(id:password)>
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            log.warn("[OCPP20] AUTH 실패 — Authorization 헤더 없음: cpCsId={}", cpCsId);
            return false;
        }

        String decoded;
        try {
            decoded = new String(Base64.getDecoder().decode(authHeader.substring(6)));
        } catch (Exception e) {
            log.warn("[OCPP20] AUTH 실패 — Base64 디코딩 오류: cpCsId={}", cpCsId);
            return false;
        }

        String[] credentials = decoded.split(":", 2);
        if (credentials.length < 2) {
            log.warn("[OCPP20] AUTH 실패 — 자격증명 형식 오류: cpCsId={}", cpCsId);
            return false;
        }

        if (!cpCsId.equals(credentials[0])) {
            log.warn("[OCPP20] AUTH 실패 — cpCsId 불일치: cpCsId={} credentials1={}", cpCsId, credentials[0]);
            return false;
        }

        ChargingStation cs = chargingStationService.retrieveChargingStationByCpIdNCsId(cpId, csId);
        if (cs == null) {
            log.warn("[OCPP20] AUTH 실패 — 충전소 없음: cpId={} csId={}", cpId, csId);
            return false;
        }

        if (!cs.getCsPassword().equals(credentials[1])) {
            log.warn("[OCPP20] AUTH 실패 — 비밀번호 불일치: cpId={} csId={}", cpId, csId);
            return false;
        }else{
            log.info("[OCPP20] AUTH 성공: cpCsId={} password:{} credentials1={} credentials2:{} ", cpCsId,cs.getCsPassword(), credentials[0] ,credentials[1] );
        }
        return true;
    }
}
