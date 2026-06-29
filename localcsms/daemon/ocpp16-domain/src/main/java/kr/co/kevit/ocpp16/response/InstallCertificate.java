/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import kr.co.kevit.ocpp16.enumtype.InstallCertificateStatusEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class InstallCertificate {
    
    private InstallCertificateStatusEnum status;

    public InstallCertificateStatusEnum getStatus() {
        return status;
    }

    public void setStatus(InstallCertificateStatusEnum status) {
        this.status = status;
    }

}
