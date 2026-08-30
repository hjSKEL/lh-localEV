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
 * @since 2019. 6. 28.
 */
public class GetDiagnosticsBean implements ControlerBean{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GetDiagnosticsBean.class);
    
    @Override
    public Object control(String csId, List<Object> res) {
        //
        Object object = res.get(2);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetDiagnosticsBean text : {}", text);
        }
        kr.co.kevit.ocpp16.response.GetDiagnostics response = gson.fromJson(text,kr.co.kevit.ocpp16.response.GetDiagnostics.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetConfigurationBean.getFileName : {}", response.getFileName());
        }
        return null;
    }

}