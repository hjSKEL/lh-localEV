/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import kr.co.kevit.ocpp16.enumtype.TriggerMessageStatusEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class ExtendedTriggerMessage {
    
    private TriggerMessageStatusEnum status;

    public TriggerMessageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TriggerMessageStatusEnum status) {
        this.status = status;
    }

}
