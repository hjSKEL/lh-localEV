/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.shared;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingStationCsm;

import java.io.Serializable;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 10. 30.
 */
public class ChargingStationDto extends ChargingStationCsm implements Serializable{

    /**  */
    private static final long serialVersionUID = -2362308053594388872L;

    private String cpName;

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }
    
}
