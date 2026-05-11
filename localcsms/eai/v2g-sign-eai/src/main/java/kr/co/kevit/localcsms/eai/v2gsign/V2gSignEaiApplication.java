package kr.co.kevit.localcsms.eai.v2gsign;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * V2G Sign EAI — V2GCertificate CSR 수신·검증·서명·전송 서버
 *
 * V2G PKI (ISO 15118) ECDSA 전용.
 *
 * 실행:
 *   java -jar v2g-sign-eai.jar --server.port=1206
 */
@SpringBootApplication(scanBasePackages = "kr.co.kevit.localcsms")
@MapperScan("kr.co.kevit.localcsms.**.dao")
@EnableAsync
public class V2gSignEaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(V2gSignEaiApplication.class, args);
    }
}
