/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import kr.co.kevit.ocpp16.enumtype.ChangeAvailabilityStatus;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class ChangeAvailability {
    
    /**
     * required
     */
    private ChangeAvailabilityStatus status;

    public ChangeAvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(ChangeAvailabilityStatus status) {
        this.status = status;
    }

}
