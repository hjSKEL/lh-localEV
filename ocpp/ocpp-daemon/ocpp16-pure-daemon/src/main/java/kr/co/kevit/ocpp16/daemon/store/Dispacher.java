/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.store;

import kr.co.kevit.ocpp16.daemon.util.DateUtils;
import org.eclipse.jetty.websocket.WebSocket.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 4. 15.
 */
public class Dispacher {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Dispacher.class);
    
    public static boolean dispatch(String csId, String text) {
        
        try {
            Connection connection = (Connection)SessionStore.getInstance().getSession(csId);
            connection.sendMessage(text);
            LOGGER.info("[TXT][{}] : TIME : {},MESSAGE : {}",csId, DateUtils.getCurrentDateAsString(DateUtils.DATE_TIME_FORMAT3), text);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
