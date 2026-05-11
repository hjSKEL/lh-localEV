package kr.co.kevit.localcsms.eai.api.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * API EAI 공통 설정.
 *
 * Spring Boot 의 RestTemplateBuilder 를 사용해 RestTemplate 을 생성하면
 * auto-configured ObjectMapper (JavaTimeModule 포함) 가 자동으로 적용된다.
 */
@Configuration
public class ApiEaiConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(java.time.Duration.ofSeconds(5))
                .setReadTimeout(java.time.Duration.ofSeconds(10))
                .build();
    }
}
