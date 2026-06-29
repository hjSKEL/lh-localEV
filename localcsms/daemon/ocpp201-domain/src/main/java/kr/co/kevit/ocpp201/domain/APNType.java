/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.APNAuthenticationEnumType;

/**
 * APN\r\nurn:x-oca:ocpp:uid:2:233134\r\nCollection of configuration data needed to make a data-connection over a cellular network.\r\n\r\nNOTE: When asking a GSM modem to dial in, it is possible to specify which mobile operator should be used. This can be done with the mobile country code (MCC) in combination with a mobile network code (MNC). Example: If your preferred network is Vodafone Netherlands, the MCC=204 and the MNC=04 which means the key PreferredNetwork = 20404 Some modems allows to specify a preferred network, which means, if this network is not available, a different network is used. If you specify UseOnlyPreferredNetwork and this network is not available, the modem will not dial in.\r\n",
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class APNType {
    
    /**
     * required
     * "maxLength": 512
     * "description": "APN. APN. URI\r\nurn:x-oca:ocpp:uid:1:568814\r\nThe Access Point Name as an URL.\r\n",
     */
    private String apn;
    
    /**
     * "maxLength": 20
     * "description": "APN. APN. User_ Name\r\nurn:x-oca:ocpp:uid:1:568818\r\nAPN username.\r\n",
     */
    private String apnUserName;
    
    /**
     * "maxLength": 20
     * "description": "APN. APN. Password\r\nurn:x-oca:ocpp:uid:1:568819\r\nAPN Password.\r\n",
     */
    private String apnPassword;
    
    /**
     * "description": "APN. SIMPIN. PIN_ Code\r\nurn:x-oca:ocpp:uid:1:568821\r\nSIM card pin code.\r\n",
     */
    private Integer simPin;
    
    /**
     * "maxLength": 6
     * "description": "APN. Preferred_ Network. Mobile_ Network_ ID\r\nurn:x-oca:ocpp:uid:1:568822\r\nPreferred network, written as MCC and MNC concatenated. See note.\r\n",
     */
    private String preferredNetwork;
    
    /**
     * "description": "APN. Use_ Only_ Preferred_ Network. Indicator\r\nurn:x-oca:ocpp:uid:1:568824\r\nDefault: false. Use only the preferred Network, do\r\nnot dial in when not available. See Note.\r\n",
     */
    private boolean useOnlyPreferredNetwork = false;
    
    /**
     * required
     */
    private APNAuthenticationEnumType apnAuthentication;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getApn() {
        return apn;
    }

    public void setApn(String apn) {
        this.apn = apn;
    }

    public String getApnUserName() {
        return apnUserName;
    }

    public void setApnUserName(String apnUserName) {
        this.apnUserName = apnUserName;
    }

    public String getApnPassword() {
        return apnPassword;
    }

    public void setApnPassword(String apnPassword) {
        this.apnPassword = apnPassword;
    }

    public Integer getSimPin() {
        return simPin;
    }

    public void setSimPin(Integer simPin) {
        this.simPin = simPin;
    }

    public String getPreferredNetwork() {
        return preferredNetwork;
    }

    public void setPreferredNetwork(String preferredNetwork) {
        this.preferredNetwork = preferredNetwork;
    }

    public boolean isUseOnlyPreferredNetwork() {
        return useOnlyPreferredNetwork;
    }

    public void setUseOnlyPreferredNetwork(boolean useOnlyPreferredNetwork) {
        this.useOnlyPreferredNetwork = useOnlyPreferredNetwork;
    }

    public APNAuthenticationEnumType getApnAuthentication() {
        return apnAuthentication;
    }

    public void setApnAuthentication(APNAuthenticationEnumType apnAuthentication) {
        this.apnAuthentication = apnAuthentication;
    }
}