package kr.co.kevit.localcsms.eai.api.interceptor;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * OCPP 버전 검증 인터셉터.
 *
 * - /ocpp16/** : ocpp1.6 만 허용
 * - /ocpp2x/** : ocpp2.0.1, ocpp2.1 허용
 *
 * cpCsId 포맷: "111111-01" → cpId="111111", csId="01" (마지막 '-' 기준 분리)
 *
 * 충전기 식별자 추출 우선순위:
 *   1. 요청 파라미터 "chargingStationIdentity"
 *   2. URI 경로 마지막 세그먼트 (bypass/{cpCsId} 경우)
 *      단, /sessions 엔드포인트는 식별자 없으므로 스킵
 */
@Component
public class OcppVersionInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(OcppVersionInterceptor.class);

    private static final Set<String> OCPP16_ALLOWED  = Set.of(StringConstants.OCPP16);
    private static final Set<String> OCPP2X_ALLOWED  = Set.of(StringConstants.OCPP201, StringConstants.OCPP21);

    private final ChargingStationService chargingStationService;

    public OcppVersionInterceptor(ChargingStationService chargingStationService) {
        this.chargingStationService = chargingStationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();

        Set<String> allowedVersions;
        if (uri.startsWith("/ocpp16/")) {
            allowedVersions = OCPP16_ALLOWED;
        } else if (uri.startsWith("/ocpp2x/")) {
            allowedVersions = OCPP2X_ALLOWED;
        } else {
            return true;
        }

        String cpCsId = resolveCpCsId(request, uri);
        if (cpCsId == null) {
            // /sessions 등 식별자 없는 경로는 그대로 통과
            return true;
        }

        String[] parts = splitCpCsId(cpCsId);
        if (parts == null) {
            log.warn("[OcppVersion] cpCsId 파싱 실패: {}", cpCsId);
            response.sendError(HttpStatus.BAD_REQUEST.value(),
                    "cpCsId 형식이 올바르지 않습니다. 예: 111111-01");
            return false;
        }

        String cpId = parts[0];
        String csId = parts[1];

        ChargingStation cs = chargingStationService.retrieveChargingStationByCpIdNCsId(cpId, csId);
        if (cs == null) {
            log.warn("[OcppVersion] 충전기 없음: cpId={} csId={}", cpId, csId);
            response.sendError(HttpStatus.NOT_FOUND.value(),
                    "충전기를 찾을 수 없습니다: " + cpCsId);
            return false;
        }

        String ocppVersion = cs.getOcppVersion();
        if (!allowedVersions.contains(ocppVersion)) {
            log.warn("[OcppVersion] 버전 불일치: cpCsId={} ocppVersion={} allowed={}",
                    cpCsId, ocppVersion, allowedVersions);
            response.sendError(HttpStatus.BAD_REQUEST.value(),
                    "지원하지 않는 OCPP 버전입니다. 충전기 버전: " + ocppVersion);
            return false;
        }

        log.debug("[OcppVersion] OK cpCsId={} ocppVersion={}", cpCsId, ocppVersion);
        return true;
    }

    /**
     * 요청에서 충전기 식별자(cpCsId)를 추출한다.
     * <p>
     * 1순위: 쿼리 파라미터 "chargingStationIdentity"
     * 2순위: URI 마지막 세그먼트 (bypass/{cpCsId} 패턴)
     *        단 세그먼트가 "sessions" 이면 null 반환
     */
    private String resolveCpCsId(HttpServletRequest request, String uri) {
        String identity = request.getParameter("chargingStationIdentity");
        if (identity != null && !identity.isBlank()) {
            return identity;
        }

        // URI 마지막 세그먼트 추출
        String[] segments = uri.split("/");
        String last = segments[segments.length - 1];
        if ("sessions".equals(last)) {
            return null;
        }
        // bypass/{cpCsId} 패턴인지 확인 (하이픈 포함 여부)
        if (last.contains("-")) {
            return last;
        }
        return null;
    }

    /**
     * "cpId-csId" 형식을 파싱한다. 마지막 '-' 기준으로 분리.
     * 예: "111111-01" → ["111111", "01"]
     */
    private String[] splitCpCsId(String cpCsId) {
        int idx = cpCsId.lastIndexOf('-');
        if (idx <= 0 || idx == cpCsId.length() - 1) {
            return null;
        }
        return new String[]{ cpCsId.substring(0, idx), cpCsId.substring(idx + 1) };
    }
}
