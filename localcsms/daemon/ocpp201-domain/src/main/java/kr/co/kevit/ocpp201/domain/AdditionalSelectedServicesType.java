/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class AdditionalSelectedServicesType {
    
    /**
     * required
     */
    private RationalNumberType serviceFee;
    
    /**
     * required
     * "maxLength": 80
     */
    private String serviceName;
    

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public RationalNumberType getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(RationalNumberType serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}