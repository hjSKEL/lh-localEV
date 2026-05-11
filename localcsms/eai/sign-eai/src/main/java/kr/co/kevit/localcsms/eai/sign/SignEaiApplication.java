package kr.co.kevit.localcsms.eai.sign;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Sign EAI — CSR 수신·검증·서명·전송 서버
 *
 * 실행:
 *   java -jar sign-eai.jar --server.port=1205
 *
 * RSA / ECDSA 모두 지원.
 * CSR 의 서명 알고리즘 OID 로 체인을 자동 식별한다.
 */
@SpringBootApplication(scanBasePackages = "kr.co.kevit.localcsms")
@MapperScan("kr.co.kevit.localcsms.**.dao")
@EnableAsync
public class SignEaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignEaiApplication.class, args);
    }
}
