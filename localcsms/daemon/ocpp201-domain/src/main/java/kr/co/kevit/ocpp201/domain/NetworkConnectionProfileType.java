/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.OCPPInterfaceEnumType;
import kr.co.kevit.ocpp201.enumtype.OCPPTransportEnumType;
import kr.co.kevit.ocpp201.enumtype.OCPPVersionEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class NetworkConnectionProfileType {
    
    private APNType apn;
    
    /**
     * required
     */
    private OCPPVersionEnumType ocppVersion;
    
    /**
     * required
     */
    private OCPPTransportEnumType ocppTransport;
    
    /**
     * required
     * "maxLength": 512
     * "description": "Communication_ Function. OCPP_ Central_ System_ URL. URI\r\nurn:x-oca:ocpp:uid:1:569357\r\nURL of the CSMS(s) that this Charging Station  communicates with.\r\n",
     */
    private String ocppCsmsUrl;
    
    /**
     * required
     * "description": "Duration in seconds before a message send by the Charging Station via this network connection times-out.\r\nThe best setting depends on the underlying network and response times of the CSMS.\r\nIf you are looking for a some guideline: use 30 seconds as a starting point.\r\n",
     */
    private int messageTimeout;
    
    /**
     * required
     */
    private int securityProfile;
    
    /**
     * required
     */
    private OCPPInterfaceEnumType ocppInterface;
    
    private VPNType vpn;

    /**
     * (2.1)
     */
    private String identity;

    /**
     * (2.1)
     */
    private String basicAuthPassword;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public APNType getApn() {
        return apn;
    }

    public void setApn(APNType apn) {
        this.apn = apn;
    }

    public OCPPVersionEnumType getOcppVersion() {
        return ocppVersion;
    }

    public void setOcppVersion(OCPPVersionEnumType ocppVersion) {
        this.ocppVersion = ocppVersion;
    }

    public OCPPTransportEnumType getOcppTransport() {
        return ocppTransport;
    }

    public void setOcppTransport(OCPPTransportEnumType ocppTransport) {
        this.ocppTransport = ocppTransport;
    }

    public String getOcppCsmsUrl() {
        return ocppCsmsUrl;
    }

    public void setOcppCsmsUrl(String ocppCsmsUrl) {
        this.ocppCsmsUrl = ocppCsmsUrl;
    }

    public int getMessageTimeout() {
        return messageTimeout;
    }

    public void setMessageTimeout(int messageTimeout) {
        this.messageTimeout = messageTimeout;
    }

    public OCPPInterfaceEnumType getOcppInterface() {
        return ocppInterface;
    }

    public void setOcppInterface(OCPPInterfaceEnumType ocppInterface) {
        this.ocppInterface = ocppInterface;
    }

    public VPNType getVpn() {
        return vpn;
    }

    public void setVpn(VPNType vpn) {
        this.vpn = vpn;
    }

    /**
     * Get securityProfile
     * @return securityProfile
     */
    public int getSecurityProfile() {
        return securityProfile;
    }

    /**
     * Set securityProfile
     * @param securityProfile
     */
    public void setSecurityProfile(int securityProfile) {
        this.securityProfile = securityProfile;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getBasicAuthPassword() {
        return basicAuthPassword;
    }

    public void setBasicAuthPassword(String basicAuthPassword) {
        this.basicAuthPassword = basicAuthPassword;
    }

}