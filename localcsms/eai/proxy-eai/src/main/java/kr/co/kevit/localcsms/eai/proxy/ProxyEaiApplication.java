package kr.co.kevit.localcsms.eai.proxy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Proxy EAI — OCPP1.6 메시지 원문(pass-through) 프록시.
 *
 * <p>
 * 기동 시 OCPP1.6 충전기 목록을 1회 조회하여, 충전기 수만큼 {@link kr.co.kevit.localcsms.eai.proxy.session.ChargerSession}
 * 스레드를 기동한다. 각 세션은:
 * </p>
 * <ul>
 * <li>RabbitMQ {@code req.<cpId>-<csId>} 큐를 구독 — 수신 메시지(OCPP1.6 원문)를 그대로
 * mTLS OCPP1.6 WebSocket 으로 대상 서버에 전송</li>
 * <li>대상 서버로부터 받은 원문 메시지를 그대로 {@code res.<cpId>-<csId>} 큐에 재발행</li>
 * </ul>
 * <p>
 * 메시지는 파싱/변환 없이 문자열 그대로 릴레이한다(순수 프록시).
 * 충전기 목록은 기동 시 1회만 조회하며, 운영 중 추가/삭제는 재기동으로 반영한다.
 * </p>
 *
 * @author bckim
 */
@SpringBootApplication(scanBasePackages = "kr.co.kevit.localcsms")
@MapperScan("kr.co.kevit.localcsms.**.dao")
@ConfigurationPropertiesScan("kr.co.kevit.localcsms.eai.proxy.config")
public class ProxyEaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyEaiApplication.class, args);
    }
}
