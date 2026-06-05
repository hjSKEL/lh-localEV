/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.api.client.vo;

import java.io.Serializable;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2024. 3. 21.
 */
public class ContractCertRes implements Serializable{
    
    /**  */
    private static final long serialVersionUID = -7201299944943729273L;
    
    private SignContractCert CCPResponse;

    public SignContractCert getCCPResponse() {
        return CCPResponse;
    }

    public void setCCPResponse(SignContractCert cCPResponse) {
        CCPResponse = cCPResponse;
    }
}
