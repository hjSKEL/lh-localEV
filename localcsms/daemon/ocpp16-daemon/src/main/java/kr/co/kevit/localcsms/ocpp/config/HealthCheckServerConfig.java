package kr.co.kevit.localcsms.ocpp.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 헬스체크 전용 평문 HTTP 커넥터를 추가한다.
 *
 * <p>
 * 메인 서버 포트({@code server.port}, 실행 인자로 지정)는 Security Profile(SP0~SP3)에 따라
 * TLS/mTLS 가 걸릴 수 있어, 외부에서 인증서 없이 단순 헬스체크를 못 할 수 있다. 그래서
 * {@code /checkHealth} 는 메인 포트 설정과 무관하게 항상 평문 커넥터(기본 8090)로 응답 가능하도록
 * Tomcat 에 추가 커넥터를 등록한다. 같은 Spring MVC 디스패처를 공유하므로 다른 REST 엔드포인트도
 * 기술적으로 이 포트에서 응답 가능하지만(기존 {@code /command} 등과 동일한 방식), 용도는
 * 헬스체크로 한정해서 사용한다.
 * </p>
 *
 * @author bckim
 */
@Configuration
public class HealthCheckServerConfig {

    @Value("${daemon.health-check-port:8090}")
    private int healthCheckPort;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> healthCheckConnectorCustomizer() {
        return factory -> {
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setPort(healthCheckPort);
            connector.setScheme("http");
            connector.setSecure(false);
            factory.addAdditionalTomcatConnectors(connector);
        };
    }
}
