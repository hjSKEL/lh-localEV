/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.DataEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class VariableCharacteristicsType {
    
    /**
     * "maxLength": 16
     */
    private String unit;
    
    /**
     * required
     */
    private DataEnumType dataType;
    
    /**
     *"type": "number"
     */
    private int minLimit;
    
    /**
     * "type": "number"
     */
    private int maxLimit;
    
    /**
     * "type": "string","maxLength": 1000
     */
    private String valuesList;
    
    /**
     * required
     */
    private boolean supportsMonitoring;

    /**
     * (2.1)
     */
    private Integer maxElements;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public DataEnumType getDataType() {
        return dataType;
    }

    public void setDataType(DataEnumType dataType) {
        this.dataType = dataType;
    }

    public int getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(int minLimit) {
        this.minLimit = minLimit;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getValuesList() {
        return valuesList;
    }

    public void setValuesList(String valuesList) {
        this.valuesList = valuesList;
    }

    public boolean isSupportsMonitoring() {
        return supportsMonitoring;
    }

    public void setSupportsMonitoring(boolean supportsMonitoring) {
        this.supportsMonitoring = supportsMonitoring;
    }

    public Integer getMaxElements() {
        return maxElements;
    }

    public void setMaxElements(Integer maxElements) {
        this.maxElements = maxElements;
    }

}
