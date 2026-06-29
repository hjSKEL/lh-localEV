/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public class EnterServiceGetType {
    //
    private Map<String, Object> customData;

    /**
     * required
     */
    private EnterServiceType enterService;

    /**
     * required
     * Id of setting
     * maxLength : 36
     */
    private String id;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public EnterServiceType getEnterService() {
        return enterService;
    }

    public void setEnterService(EnterServiceType enterService) {
        this.enterService = enterService;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
