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
import kr.co.kevit.ocpp16.domain.ConfigurationKey;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class GetConfigurationBean implements ControlerBean{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GetConfigurationBean.class);
    
    @Override
    public Object control(String csId, List<Object> res) {
        //
        Object object = res.get(2);
        Gson gson = new Gson();
        String text = gson.toJson(object);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetConfigurationBean text : {}", text);
        }
        kr.co.kevit.ocpp16.response.GetConfiguration response = gson.fromJson(text,kr.co.kevit.ocpp16.response.GetConfiguration.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetConfigurationBean.getStatus : {}", response.getUnknownKey());
            if(response.getUnknownKey() != null) {
                for(String value : response.getUnknownKey()) {
                    LOGGER.debug("GetConfigurationBean.getUnknownKey.value : {}", value);
                }
            }
            if(response.getConfigurationKey() != null) {
                for(ConfigurationKey keyValue : response.getConfigurationKey()) {
                    LOGGER.debug("GetConfigurationBean.getConfigurationKey.getKey : {}", keyValue.getKey());
                    LOGGER.debug("GetConfigurationBean.getConfigurationKey.getValue : {}", keyValue.getValue());
                    LOGGER.debug("GetConfigurationBean.getConfigurationKey.isReadonly : {}", keyValue.isReadonly());
                }
            }
        }
        return null;
    }

}