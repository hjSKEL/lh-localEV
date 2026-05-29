package kr.co.kevit.localcsms.ocpp20;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "kr.co.kevit.localcsms")
@MapperScan("kr.co.kevit.localcsms.**.dao")
@EnableScheduling
public class Ocpp20DaemonApplication {

    private static final java.util.Set<String> VALID_SP = java.util.Set.of("0", "1", "2", "3");

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java -jar ocpp20-daemon.jar <sp:0|1|2|3> <ip> <port>");
            return;
        }
        if (!VALID_SP.contains(args[0])) {
            System.out.println("Invalid security profile: '" + args[0] + "'. Allowed: 0, 1, 2, 3");
            return;
        }
        System.out.println("sp=" + args[0] + " ip=" + args[1] + " port=" + args[2]);
        System.setProperty("server.port", args[2]);
        System.setProperty("daemon.ip",   args[1]);
        System.setProperty("daemon.port", args[2]);
        SpringApplication.run(Ocpp20DaemonApplication.class, args);
    }
}
