/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.bean.res;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.co.kevit.ocpp16.daemon.bean.ControlerBean;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class GetLogBean implements ControlerBean{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GetLogBean.class);
    
    @Override
    public Object control(String csId, List<Object> res) {
        //
        Object object = res.get(2);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetLogBean text : {}", text);
        }
        
        kr.co.kevit.ocpp16.response.GetLog response = gson.fromJson(text,kr.co.kevit.ocpp16.response.GetLog.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetLogBean.getStatus : {}", response.getStatus());
        }
        return null;
    }

}