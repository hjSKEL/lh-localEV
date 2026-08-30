/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.store;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 27.
 */
public class ActionStore {
    
    private static ActionStore instance = new ActionStore();
    private Map<String,Action> actionMap = new HashMap<>();
    
    public static ActionStore getInstance() {
        return instance;
    }
    
    public void setAction(String csId, Action action) {
        actionMap.put(csId, action);
    }
    
    public Action getAction(String csId) {
        return actionMap.get(csId);
    }

}
