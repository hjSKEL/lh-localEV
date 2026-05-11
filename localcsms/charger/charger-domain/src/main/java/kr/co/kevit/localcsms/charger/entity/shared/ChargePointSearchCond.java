/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.shared;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 10. 29.
 */
public class ChargePointSearchCond extends PageCriteria implements Serializable{

    /**  */
    private static final long serialVersionUID = -5554575751533268494L;

    private String cpName;

    private String cpId;
    
    private String csType;

    /**
     * Get cpName
     * @return cpName
     */
    public String getCpName() {
        return cpName;
    }

    /**
     * Set cpName
     * @param cpName
     */
    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    /**
     * Get cpId
     * @return cpId
     */
    public String getCpId() {
        return cpId;
    }

    /**
     * Set cpId
     * @param cpId
     */
    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

	public String getCsType() {
		return csType;
	}

	public void setCsType(String csType) {
		this.csType = csType;
	}

}
