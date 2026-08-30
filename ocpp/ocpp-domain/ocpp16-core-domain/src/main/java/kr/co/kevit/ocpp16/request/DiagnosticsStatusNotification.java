/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.DiagnosticsStatusNotificationStatus;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 28.
 */
public class DiagnosticsStatusNotification {

    private DiagnosticsStatusNotificationStatus status;

    public DiagnosticsStatusNotificationStatus getStatus() {
        return status;
    }

    public void setStatus(DiagnosticsStatusNotificationStatus status) {
        this.status = status;
    }
    
}
