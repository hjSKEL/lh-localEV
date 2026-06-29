/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp16.response;

import kr.co.kevit.ocpp16.enumtype.BootNotificationResponseStatusEnum;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 2. 20.
 */
public class BootNotification {

    private BootNotificationResponseStatusEnum status;
    
    private String currentTime;

    // Heatbeat Interval Seconds
    private Integer interval;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public BootNotificationResponseStatusEnum getStatus() {
        return status;
    }

    public void setStatus(BootNotificationResponseStatusEnum status) {
        this.status = status;
    }
}
