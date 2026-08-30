/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.MessageTriggerEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class ExtendedTriggerMessage {
    
    private Integer connectorId;
    
    /**
     * required
     */
    private MessageTriggerEnum requestedMessage;

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    public MessageTriggerEnum getRequestedMessage() {
        return requestedMessage;
    }

    public void setRequestedMessage(MessageTriggerEnum requestedMessage) {
        this.requestedMessage = requestedMessage;
    }

}
