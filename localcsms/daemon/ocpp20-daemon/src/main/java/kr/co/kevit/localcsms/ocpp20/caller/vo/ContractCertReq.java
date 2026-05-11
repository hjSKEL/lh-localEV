/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.caller.vo;

import java.io.Serializable;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2024. 3. 20.
 */
public class ContractCertReq implements Serializable{
    
    /**  */
    private static final long serialVersionUID = 1058906645691267576L;

    private String certificateInstallationReq;
    
    private String xsdMsgDefNamespace;

    public String getCertificateInstallationReq() {
        return certificateInstallationReq;
    }

    public void setCertificateInstallationReq(String certificateInstallationReq) {
        this.certificateInstallationReq = certificateInstallationReq;
    }

    public String getXsdMsgDefNamespace() {
        return xsdMsgDefNamespace;
    }

    public void setXsdMsgDefNamespace(String xsdMsgDefNamespace) {
        this.xsdMsgDefNamespace = xsdMsgDefNamespace;
    }

}
