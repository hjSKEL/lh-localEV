package kr.co.kevit.localcsms.eai.proxy.config;

import javax.net.ssl.SSLSocketFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mTLS 클라이언트 인증서 설정 — 모든 {@code ChargerSession} 이 공유하는 단일 게이트웨이 인증서.
 *
 * <p>
 * 대상 서버가 충전기별로 다른 client 인증서를 요구하지 않는다는 전제(공통 게이트웨이 인증서 1개) 하에,
 * {@link SSLSocketFactory} 를 단 한 번 생성해 모든 세션이 재사용한다. 실제 빌드 로직은
 * {@link MtlsSocketFactoryBuilder}(Spring 무관 static 유틸)에 있다 — 연결 확인용 테스트
 * ({@code MtlsConnectionIT})가 Spring 컨텍스트 없이 동일 로직을 재사용하기 위함.
 * </p>
 *
 * @author bckim
 */
@Configuration
public class MtlsClientConfig {

    @Bean
    public SSLSocketFactory ocppTargetSslSocketFactory(ProxyProperties props) throws Exception {
        return MtlsSocketFactoryBuilder.build(props.getMtls());
    }
}
