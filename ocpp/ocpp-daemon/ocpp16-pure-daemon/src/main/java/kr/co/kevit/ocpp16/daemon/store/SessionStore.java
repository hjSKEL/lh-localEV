/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.store;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 4. 15.
 */
public class SessionStore {
    
    private static SessionStore instance = new SessionStore();
    
    private Map<String, Object> sessionMap = new HashMap<>();
    
    public static SessionStore getInstance() {
        return instance;
    }
    
    public void setSession(String key, Object value) {
        sessionMap.put(key, value);
    }
    
    public Object getSession(String key) {
        return sessionMap.get(key);
    }

}
