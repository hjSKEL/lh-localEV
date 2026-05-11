/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process.dto;

import java.math.BigDecimal;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 8.
 */
public class ChargeStatusDto {
    
    private String status;
    
    private long chargingTime;
    
    private BigDecimal chargeCopacity;
    
    private BigDecimal price;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(long chargingTime) {
        this.chargingTime = chargingTime;
    }

    public BigDecimal getChargeCopacity() {
        return chargeCopacity;
    }

    public void setChargeCopacity(BigDecimal chargeCopacity) {
        this.chargeCopacity = chargeCopacity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
