package kr.co.kevit.localcsms.ocpp20.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * OCPP 2.x daemon 공통 ObjectMapper.
 *
 * <p>OCPP 2.1 명세는 자주 새 옵션 필드가 추가되므로, 충전기→CSMS 인바운드 파싱이
 * 알려지지 않은 필드 한 개로 InternalError 를 내지 않도록 FAIL_ON_UNKNOWN_PROPERTIES 를
 * 끕니다. 또한 일부 충전기가 enum 을 대소문자 다르게 보내는 케이스가 있어
 * ACCEPT_CASE_INSENSITIVE_ENUMS 도 활성화합니다.</p>
 */
@Configuration
public class OcppObjectMapperConfig {

    @Bean
    @Primary
    public ObjectMapper ocppObjectMapper() {
        return JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
                .build();
    }
}
