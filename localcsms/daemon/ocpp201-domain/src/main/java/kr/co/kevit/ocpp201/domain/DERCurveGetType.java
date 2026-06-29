/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.DERControlEnumType;

/**
 * 
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2026. 3. 18.
 */
public class DERCurveGetType {
    
    private DERCurveType curve;
    
    /**
     * "description": "Id of DER curve\r\n\r\n",
     * "maxLength": 36
     */
    private String id;
    
    private DERControlEnumType curveType;
    
    /**
     * "description": "True if this is a default curve\r\n\r\n",
     */
    private Boolean isDefault;
    
    /**
     * "description": "True if this setting is superseded by a higher priority setting (i.e. lower value of _priority_)\r\n\r\n",
     */
    private Boolean isSuperseded;
    
    private Map<String, Object> customData;

    /**
     * Get curve
     * @return curve
     */
    public DERCurveType getCurve() {
        return curve;
    }

    /**
     * Set curve
     * @param curve
     */
    public void setCurve(DERCurveType curve) {
        this.curve = curve;
    }

    /**
     * Get id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get curveType
     * @return curveType
     */
    public DERControlEnumType getCurveType() {
        return curveType;
    }

    /**
     * Set curveType
     * @param curveType
     */
    public void setCurveType(DERControlEnumType curveType) {
        this.curveType = curveType;
    }

    /**
     * Get isDefault
     * @return isDefault
     */
    public Boolean getIsDefault() {
        return isDefault;
    }

    /**
     * Set isDefault
     * @param isDefault
     */
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * Get isSuperseded
     * @return isSuperseded
     */
    public Boolean getIsSuperseded() {
        return isSuperseded;
    }

    /**
     * Set isSuperseded
     * @param isSuperseded
     */
    public void setIsSuperseded(Boolean isSuperseded) {
        this.isSuperseded = isSuperseded;
    }

    /**
     * Get customData
     * @return customData
     */
    public Map<String, Object> getCustomData() {
        return customData;
    }

    /**
     * Set customData
     * @param customData
     */
    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
    
}