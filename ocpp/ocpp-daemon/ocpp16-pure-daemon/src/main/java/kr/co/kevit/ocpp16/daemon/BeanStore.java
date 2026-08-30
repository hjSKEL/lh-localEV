/*******************************************************************************
 * Copyright(c) 2019 Charge All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon;

import org.springframework.context.ApplicationContext;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr</a> 
 * @since 2019. 5. 1.
 */
public class BeanStore {
    
    private ApplicationContext applicationContext;
    
    private static BeanStore store = new BeanStore();
    
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    public static BeanStore getInstance() {
        return store;
    }
    
    public Object getBean(String name) {
        return (Object)applicationContext.getBean(name);
    }

}
