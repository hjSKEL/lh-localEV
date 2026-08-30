/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import java.util.List;

import kr.co.kevit.ocpp16.domain.ConfigurationKey;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class GetConfiguration {
    
    /**
     * 
     */
    private List<ConfigurationKey> configurationKey;
    
    /**
     * "type": "string","maxLength": 50
     */
    private List<String> unknownKey;

    public List<ConfigurationKey> getConfigurationKey() {
        return configurationKey;
    }

    public void setConfigurationKey(List<ConfigurationKey> configurationKey) {
        this.configurationKey = configurationKey;
    }

    public List<String> getUnknownKey() {
        return unknownKey;
    }

    public void setUnknownKey(List<String> unknownKey) {
        this.unknownKey = unknownKey;
    }

}