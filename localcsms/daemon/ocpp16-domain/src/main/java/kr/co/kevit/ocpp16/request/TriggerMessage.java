/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.RequestedMessageEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class TriggerMessage {
    /**
     * required
     */
    private RequestedMessageEnum requestedMessage;

    private Integer connectorId;

    public RequestedMessageEnum getRequestedMessage() {
        return requestedMessage;
    }

    public void setRequestedMessage(RequestedMessageEnum requestedMessage) {
        this.requestedMessage = requestedMessage;
    }

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }
    
}
