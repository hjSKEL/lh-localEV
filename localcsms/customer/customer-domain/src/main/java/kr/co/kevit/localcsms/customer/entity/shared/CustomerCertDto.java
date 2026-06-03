/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.entity.shared;

import kr.co.kevit.localcsms.customer.entity.domain.CustomerCert;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 3. 18.
 */
public class CustomerCertDto extends CustomerCert{
    
    /**  */
    private static final long serialVersionUID = -4708373886897196588L;
    
    /**
     * 고객명
     * CUT_NM      VARCHAR2(20 BYTE)               NOT NULL,
     */
    private String custName;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

}
