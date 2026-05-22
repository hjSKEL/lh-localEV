package kr.co.kevit.localcsms.eai.zeroenergy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Zero Energy EAI — 충전 누적량 조회 외부 연계 서버
 *
 * 실행:
 *   java -jar zeroenergy-eai.jar --server.port=1206
 *
 * Endpoint:
 *   POST /api/v1/zeroenergy
 */
@SpringBootApplication
public class ZeroEnergyEaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroEnergyEaiApplication.class, args);
    }
}
