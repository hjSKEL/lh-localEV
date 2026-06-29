/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class GetTransactionStatus {
    
    private Boolean ongoingIndicator;
    
    /**
     * required
     */
    private boolean messagesInQueue;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Boolean getOngoingIndicator() {
        return ongoingIndicator;
    }

    public void setOngoingIndicator(Boolean ongoingIndicator) {
        this.ongoingIndicator = ongoingIndicator;
    }

    public boolean isMessagesInQueue() {
        return messagesInQueue;
    }

    public void setMessagesInQueue(boolean messagesInQueue) {
        this.messagesInQueue = messagesInQueue;
    }

}
