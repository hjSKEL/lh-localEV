/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.caller.vo;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2024. 3. 20.
 */
public class ContractCert {

    private String metaData;
    private String certificateInstallationRes;
    private String emaid;
    
    public String getMetaData() {
        return metaData;
    }
    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }
    public String getCertificateInstallationRes() {
        return certificateInstallationRes;
    }
    public void setCertificateInstallationRes(String certificateInstallationRes) {
        this.certificateInstallationRes = certificateInstallationRes;
    }
    public String getEmaid() {
        return emaid;
    }
    public void setEmaid(String emaid) {
        this.emaid = emaid;
    }
    
}
