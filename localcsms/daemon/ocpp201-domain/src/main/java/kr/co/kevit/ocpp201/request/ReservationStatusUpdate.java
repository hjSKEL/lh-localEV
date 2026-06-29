/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ReservationUpdateStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class ReservationStatusUpdate {
    
    /**
     * required
     */
    private int reservationId;
    
    /**
     * required
     */
    private ReservationUpdateStatusEnumType reservationUpdateStatus;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public ReservationUpdateStatusEnumType getReservationUpdateStatus() {
        return reservationUpdateStatus;
    }

    public void setReservationUpdateStatus(ReservationUpdateStatusEnumType reservationUpdateStatus) {
        this.reservationUpdateStatus = reservationUpdateStatus;
    }

}