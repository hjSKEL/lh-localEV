package kr.co.kevit.localcsms.eai.ocsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OCSP EAI — 인증서 폐기 상태 응답 서버
 *
 * 실행:
 *   java -jar ocsp-eai.jar [--server.port=8080]
 *
 * RSA / ECDSA 인증서 발급 체인 모두 지원.
 * 요청 CertificateID 의 issuerKeyHash 로 RSA CA / ECDSA CA 를 자동 식별한다.
 */
@SpringBootApplication
public class OcspApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcspApplication.class, args);
    }
}
