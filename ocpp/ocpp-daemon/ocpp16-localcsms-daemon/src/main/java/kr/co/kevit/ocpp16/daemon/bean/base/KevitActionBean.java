/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.base;

import com.google.gson.Gson;
import kr.co.kevit.ocpp16.daemon.store.Action;
import kr.co.kevit.ocpp16.daemon.store.ActionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;


/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 7. 27.
 */
public class KevitActionBean implements ActionBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(KevitActionBean.class);
    
    public Object control(String csId, String messageTypeId, List<Object> res) {
        //
        Object object = res.get(2);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug(text);
        }
        
        Action action = ActionStore.getInstance().getAction(csId);
        if(action == null) {
            return null;
        }
        String actionName = null;
        if(res.get(1).toString().equals(action.getId())) {
            actionName = action.getName();
        }

        return actionName;
    }

}