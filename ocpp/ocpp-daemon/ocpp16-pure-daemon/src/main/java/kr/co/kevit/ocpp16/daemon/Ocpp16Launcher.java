/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon;

import org.apache.camel.CamelContext;
import org.apache.camel.main.Main;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import kr.co.kevit.ocpp16.daemon.valid.License;
import kr.co.kevit.ocpp16.license.Vendor;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 4. 20.
 */
public class Ocpp16Launcher {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Ocpp16Launcher.class);
    
    /**
     * 
     */
    public static void main(String... args) throws Exception {
        //
        Boolean isLicensed = License.getInstance().init();
        if(!isLicensed) {
            System.exit(1);
        }
        SecurityProfile.LEVEL = args[0];
        if(Vendor.getInstance().isChangeable()) {
            Vendor.getInstance().setPort(args[1], args[2]);            
            Vendor.getInstance().setIp(args[3]);
            if (SecurityProfile.LEVEL.equals("0") || SecurityProfile.LEVEL.equals("1")) {
                Vendor.getInstance().setTLS(false);
            } else {
                Vendor.getInstance().setTLS(true);
            }
        }
        LOGGER.info("LISTEN PORT : {}, REMOTE PORT : {}", Vendor.getInstance().ocppPort(), Vendor.getInstance().remotePort());
        LOGGER.info("IP : {}", Vendor.getInstance().getIp());
        
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        CamelContext  camelContext = SpringCamelContext.springCamelContext(applicationContext, false);
        BeanStore.getInstance().setApplicationContext(applicationContext);
        Main main = new Main();
        
        camelContext.start();
        main.getOrCreateCamelContext();
        main.run();     
    }

}
