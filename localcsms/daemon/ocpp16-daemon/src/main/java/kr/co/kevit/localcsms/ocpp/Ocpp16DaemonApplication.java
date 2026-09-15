package kr.co.kevit.localcsms.ocpp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "kr.co.kevit.localcsms")
@MapperScan("kr.co.kevit.localcsms.**.dao")
public class Ocpp16DaemonApplication {

    public static void main(String[] args) {
        //
        if(args.length != 3 && args.length != 4) {
            System.out.println("Usage: java -jar ocpp16-daemon.jar <security profile: 0|1|2|3> <ip> <port> [LH|CPO]");
            return;
        }
        String sp = args[0];
        if(!"0".equals(sp) && !"1".equals(sp) && !"2".equals(sp) && !"3".equals(sp)) {
            System.out.println("Error: <security profile> must be 0, 1, 2, or 3. Got: " + sp);
            return;
        }
        // 4번째 인자(mode) 생략 시 기존 배포 스크립트 호환을 위해 LH 로 기본 동작
        String mode = args.length == 4 ? args[3] : "LH";
        if(!"LH".equalsIgnoreCase(mode) && !"CPO".equalsIgnoreCase(mode)) {
            System.out.println("Error: <mode> must be LH or CPO. Got: " + mode);
            return;
        }
        System.out.println("securityProfile=" + sp);
        System.out.println(args[1]);
        System.out.println(args[2]);
        System.out.println("mode=" + mode);
        System.setProperty("server.port", args[2]);
        System.setProperty("daemon.ip",   args[1]);
        System.setProperty("daemon.port", args[2]);
        System.setProperty("daemon.mode", mode.toUpperCase());
        applySecurityProfile(sp);
        SpringApplication.run(Ocpp16DaemonApplication.class, args);
    }

    /**
     * OCPP1.6 Security Profile 에 따라 {@code server.ssl.*} 를 런타임에 결정한다.
     * (keystore/truststore 경로·비밀번호는 application.yml 에 고정 설정돼 있고, 여기서는
     * TLS 사용 여부와 클라이언트 인증서 요구 여부만 프로필별로 켠다.)
     *
     * <ul>
     * <li>SP0/SP1 — 평문(TLS 미사용). SP1(Basic Auth over HTTP)은 인증 로직이 별도로
     * 필요하지만 TLS 자체는 SP0 와 동일하게 끈다.</li>
     * <li>SP2 — TLS 사용, 서버 인증서만 제시(클라이언트 인증서 불필요).</li>
     * <li>SP3 — mTLS. 클라이언트(충전기) 인증서 없으면 handshake 자체를 거부.</li>
     * </ul>
     */
    private static void applySecurityProfile(String sp) {
        switch (sp) {
            case "2":
                System.setProperty("server.ssl.enabled", "true");
                System.setProperty("server.ssl.client-auth", "none");
                System.out.println("TLS(SP2) 적용 — 서버 인증서만 사용, 클라이언트 인증서 불필요");
                break;
            case "3":
                System.setProperty("server.ssl.enabled", "true");
                System.setProperty("server.ssl.client-auth", "need");
                System.out.println("mTLS(SP3) 적용 — 클라이언트(충전기) 인증서 필수");
                break;
            default: // "0", "1"
                System.setProperty("server.ssl.enabled", "false");
                System.out.println("TLS 미사용(SP" + sp + ") — 평문 ws://");
                break;
        }
    }
}
