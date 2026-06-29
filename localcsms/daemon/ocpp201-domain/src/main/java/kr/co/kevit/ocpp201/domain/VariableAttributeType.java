/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.AttributeEnumType;
import kr.co.kevit.ocpp201.enumtype.MutabilityEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class VariableAttributeType {
    
    private AttributeEnumType type;
    
    /**
     * "type": "string","maxLength": 2500
     */
    private String value;
    
    private MutabilityEnumType mutability;
    
    private boolean persistent = false;
    
    private boolean constant = false;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public AttributeEnumType getType() {
        return type;
    }

    public void setType(AttributeEnumType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MutabilityEnumType getMutability() {
        return mutability;
    }

    public void setMutability(MutabilityEnumType mutability) {
        this.mutability = mutability;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

}