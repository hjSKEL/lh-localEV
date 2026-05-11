/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * java -Duser.timezone=GMT+09:00 -Dfile.encoding=UTF-8 -Xms{MIN_MEMORY}m
 * -Xmx{MAX_MEMORY}m -jar evAdmin.jar {PORT}
 */
@SpringBootApplication(scanBasePackages = "kr.co.kevit.localcsms")
@MapperScan("kr.co.kevit.localcsms.**.dao")
public class AdminWebApplication {

    public static void main(String[] args) {
        if (args.length > 0) {
            System.setProperty("server.port", args[0]);
        }
        SpringApplication.run(AdminWebApplication.class, args);
    }
}
