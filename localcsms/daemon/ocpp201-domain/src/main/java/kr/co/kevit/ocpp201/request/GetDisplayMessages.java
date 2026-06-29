/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.MessagePriorityEnumType;
import kr.co.kevit.ocpp201.enumtype.MessageStateEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class GetDisplayMessages {
    
    /**
     * 
     */
    private List<Integer> id;
    
    /**
     * required
     */
    private Integer requestId;
    
    private MessagePriorityEnumType priority;
    
    private MessageStateEnumType state;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
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

}
