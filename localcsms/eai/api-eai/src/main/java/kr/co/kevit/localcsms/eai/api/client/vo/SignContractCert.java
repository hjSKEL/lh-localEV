/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.api.client.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2024. 3. 20.
 */
public class SignContractCert implements Serializable{
    
    /**  */
    private static final long serialVersionUID = 5360005605589472662L;
    
    private List<ContractCert> emaidContent;

    public List<ContractCert> getEmaidContent() {
        return emaidContent;
    }

    public void setEmaidContent(List<ContractCert> emaidContent) {
        this.emaidContent = emaidContent;
    }
}
