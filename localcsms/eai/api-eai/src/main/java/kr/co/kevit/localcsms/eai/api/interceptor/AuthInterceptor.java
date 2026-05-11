package kr.co.kevit.localcsms.eai.api.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Authorization 헤더 검증 인터셉터.
 *
 * /ocpp16/** → Basic b2NwcDEuNjpvY2FrZXkxMjM0NTY3ODkw
 * /ocpp2x/** → Basic b2NwcDIuMC4xOm9jYWtleTEyMzQ1Njc4OTA=
 * 불일치 시 HTTP 401 반환.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Value("${api-eai.auth-token.ocpp16:Basic b2NwcDEuNjpvY2FrZXkxMjM0NTY3ODkw}")
    private String ocpp16Token;

    @Value("${api-eai.auth-token.ocpp2x:Basic b2NwcDIuMC4xOm9jYWtleTEyMzQ1Njc4OTA=}")
    private String ocpp2xToken;

    private final ObjectMapper objectMapper;

    public AuthInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Spring 에러 처리 엔드포인트는 인증 불필요
        if (uri.startsWith("/error")) {
            return true;
        }

        String authorization = request.getHeader("Authorization");

        String expected;
        if (uri.contains("/ocpp2x/")) {
            expected = ocpp2xToken;
        } else {
            expected = ocpp16Token;
        }

        if (expected.equals(authorization)) {
            return true;
        }

        log.warn("[AUTH] 인증 실패: uri={} Authorization={}", uri, authorization);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        Map.of("status", "rejected", "message", "Unauthorized")));
        return false;
    }
}
