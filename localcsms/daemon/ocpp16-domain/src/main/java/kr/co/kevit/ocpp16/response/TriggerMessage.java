/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import kr.co.kevit.ocpp16.enumtype.TriggerMessageStatus;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 6. 28.
 */
public class TriggerMessage {

    private TriggerMessageStatus status;

    public TriggerMessageStatus getStatus() {
        return status;
    }

    public void setStatus(TriggerMessageStatus status) {
        this.status = status;
    }

}
