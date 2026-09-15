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
            System.out.println("Usage: java -jar ocpp16-daemon.jar <sp level> <ip> <port> [LH|CPO]");
            return;
        }
        String sp = args[0];
        if(!"0".equals(sp) && !"1".equals(sp) && !"2".equals(sp) && !"3".equals(sp)) {
            System.out.println("Error: <sp level> must be 0, 1, 2, or 3. Got: " + sp);
            return;
        }
        // 4번째 인자(mode) 생략 시 기존 배포 스크립트 호환을 위해 LH 로 기본 동작
        String mode = args.length == 4 ? args[3] : "LH";
        if(!"LH".equalsIgnoreCase(mode) && !"CPO".equalsIgnoreCase(mode)) {
            System.out.println("Error: <mode> must be LH or CPO. Got: " + mode);
            return;
        }
        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);
        System.out.println("mode=" + mode);
        System.setProperty("server.port", args[2]);
        System.setProperty("daemon.ip",   args[1]);
        System.setProperty("daemon.port", args[2]);
        System.setProperty("daemon.mode", mode.toUpperCase());
        SpringApplication.run(Ocpp16DaemonApplication.class, args);
    }
}
