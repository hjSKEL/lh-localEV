/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.FirmwareStatusEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class SignedFirmwareStatusNotification {
    
    private Integer requestId;
    
    /**
     * required
     */
    private FirmwareStatusEnum status;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public FirmwareStatusEnum getStatus() {
        return status;
    }

    public void setStatus(FirmwareStatusEnum status) {
        this.status = status;
    }
    
}
