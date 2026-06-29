/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.MessagePriorityEnumType;
import kr.co.kevit.ocpp201.enumtype.MessageStateEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class MessageInfoType {
    
    private ComponentType display;
    
    /**
     * required
     */
    private int id;
    
    /**
     * required
     */
    private MessagePriorityEnumType priority;
    
    private MessageStateEnumType state;
    
    /**
     * "type": "string","format": "date-time"
     */
    private String startDateTime;
    
    /**
     * "type": "string","format": "date-time"
     */
    private String endDateTime;
    
    /**
     * "type": "string","maxLength": 36
     */
    private String transactionId;
    
    /**
     * required
     */
    private MessageContentType message;

    /**
     * (2.1)
     */
    private List<MessageContentType> messageExtra;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public ComponentType getDisplay() {
        return display;
    }

    public void setDisplay(ComponentType display) {
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MessagePriorityEnumType getPriority() {
        return priority;
    }

    public void setPriority(MessagePriorityEnumType priority) {
        this.priority = priority;
    }

    public MessageStateEnumType getState() {
        return state;
    }

    public void setState(MessageStateEnumType state) {
        this.state = state;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public MessageContentType getMessage() {
        return message;
    }

    public void setMessage(MessageContentType message) {
        this.message = message;
    }

    public List<MessageContentType> getMessageExtra() {
        return messageExtra;
    }

    public void setMessageExtra(List<MessageContentType> messageExtra) {
        this.messageExtra = messageExtra;
    }
}