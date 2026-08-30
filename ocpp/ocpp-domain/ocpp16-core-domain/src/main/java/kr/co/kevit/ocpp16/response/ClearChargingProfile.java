/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import kr.co.kevit.ocpp16.enumtype.ClearChargingProfileStatusEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class ClearChargingProfile {
    
    private ClearChargingProfileStatusEnum status;

    public ClearChargingProfileStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ClearChargingProfileStatusEnum status) {
        this.status = status;
    }

}
