/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.MessageInfoType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class NotifyDisplayMessages {
    
    /**
     * "minItems": 1
     */
    private List<MessageInfoType> messageInfo;
    
    /**
     * required
     * "type": "integer"
     */
    private int requestId;
    
    /**
     * "type": "boolean","default": false
     */
    private boolean tbc = false;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<MessageInfoType> getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(List<MessageInfoType> messageInfo) {
        this.messageInfo = messageInfo;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public boolean isTbc() {
        return tbc;
    }

    public void setTbc(boolean tbc) {
        this.tbc = tbc;
    }

}
