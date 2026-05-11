/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.shared;

import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 16.
 */
public class ChargerStatusInfoDto extends ChargerStatusInfo{
    
    /**  */
    private static final long serialVersionUID = -2898382509078354766L;

    private String cpName;

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

}
