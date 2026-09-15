package kr.co.kevit.localcsms.eai.api.config;

import kr.co.kevit.localcsms.eai.api.interceptor.AuthInterceptor;
import kr.co.kevit.localcsms.eai.api.interceptor.OcppVersionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * API EAI Web MVC 설정.
 *
 * AuthInterceptor 적용:
 *   - 대상: 모든 경로 ("/**")
 *   - 제외: 정적 리소스 (*.html, /js/**, /css/**)
 *           OCPP 1.6 제어 화면 API (/ocpp16/bypass/**)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final OcppVersionInterceptor ocppVersionInterceptor;

    public WebConfig(AuthInterceptor authInterceptor,
                     OcppVersionInterceptor ocppVersionInterceptor) {
        this.authInterceptor = authInterceptor;
        this.ocppVersionInterceptor = ocppVersionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/*.html",
                        "/js/**",
                        "/css/**",
                        "/ocpp16/bypass/**",
                        "/csOcpp/**"
                );

        // OCPP 버전 검증 — ocpp16/**, ocpp2x/** 대상
        registry.addInterceptor(ocppVersionInterceptor)
                .addPathPatterns("/ocpp16/**");
    }
}
