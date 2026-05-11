package kr.co.kevit.localcsms.ocpp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "kr.co.kevit.localcsms")
@MapperScan("kr.co.kevit.localcsms.**.dao")
public class Ocpp16DaemonApplication {

    public static void main(String[] args) {
        //
        if(args.length != 3) {
            System.out.println("Usage: java -jar ocpp16-daemon.jar <sp level> <ip> <port>");
            return;
        }
        String sp = args[0];
        if(!"0".equals(sp) && !"1".equals(sp) && !"2".equals(sp) && !"3".equals(sp)) {
            System.out.println("Error: <sp level> must be 0, 1, 2, or 3. Got: " + sp);
            return;
        }
        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);
        System.setProperty("server.port", args[2]);
        System.setProperty("daemon.ip",   args[1]);
        System.setProperty("daemon.port", args[2]);
        SpringApplication.run(Ocpp16DaemonApplication.class, args);
    }
}
