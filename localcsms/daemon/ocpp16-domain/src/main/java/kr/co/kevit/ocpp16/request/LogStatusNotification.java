/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import kr.co.kevit.ocpp16.enumtype.UploadLogStatusEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 8. 17.
 */
public class LogStatusNotification {
    
    private Integer requestId;
    
    /**
     * required
     */
    private UploadLogStatusEnum status;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public UploadLogStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UploadLogStatusEnum status) {
        this.status = status;
    }

}
