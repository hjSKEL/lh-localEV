/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import kr.co.kevit.localcsms.eai.adr.task.DERControlRunner;
import kr.co.kevit.localcsms.eai.adr.task.DynamicScheduleRunner;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 4. 20.
 */
public class ADRLauncher {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ADRLauncher.class);
    
    /**
     * 
     */
    public static void main(String... args) throws Exception {
        //
        LOGGER.info("ADRLauncher START ");
        new DynamicScheduleRunner().start();
        new DERControlRunner().start();
        
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        CamelContext camelContext = SpringCamelContext.springCamelContext(applicationContext, false);
        camelContext.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { camelContext.stop(); } catch (Exception e) { e.printStackTrace(); }
        }));
        Thread.currentThread().join();     
    }

}
