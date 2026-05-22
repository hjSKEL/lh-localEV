package kr.co.kevit.localcsms.eai.lh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * LH EAI — 외부 HTTP 호출을 받아 WSS(보안 WebSocket) 채널로 전달하는 연계 서버.
 *
 * 실행:
 *   java -jar lh-eai.jar --server.port=1207
 *
 * Endpoint:
 *   POST /api/v1/lh
 */
@SpringBootApplication
public class LhEaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LhEaiApplication.class, args);
    }
}
