/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class ChargingStationType {
    
    /**
     * "type": "string","maxLength": 25
     */
    private String serialNumber;
    
    /**
     * required
     * "type": "string","maxLength": 20
     */
    private String model;
    
    /**
     * 
     */
    private ModemType modem;
    
    /**
     * required
     * "type": "string","maxLength": 50
     */
    private String vendorName;
    
    /**
     * "type": "string","maxLength": 50
     */
    private String firmwareVersion;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ModemType getModem() {
        return modem;
    }

    public void setModem(ModemType modem) {
        this.modem = modem;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

}