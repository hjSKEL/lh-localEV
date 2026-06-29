/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 2. 21.
 */
public class Heartbeat {

    private Integer RSRP;
    private Integer RSRQ;
    private Integer RSSI;
    /**
     * 1,2
     * 1,2,4
     * 1
     * null or blank
     */
    private String powerModuleError;

    public Integer getRSRP() {
        return RSRP;
    }

    public void setRSRP(Integer RSRP) {
        this.RSRP = RSRP;
    }

    public Integer getRSRQ() {
        return RSRQ;
    }

    public void setRSRQ(Integer RSRQ) {
        this.RSRQ = RSRQ;
    }

    public Integer getRSSI() {
        return RSSI;
    }

    public void setRSSI(Integer RSSI) {
        this.RSSI = RSSI;
    }

    public String getPowerModuleError() {
        return powerModuleError;
    }

    public void setPowerModuleError(String powerModuleError) {
        this.powerModuleError = powerModuleError;
    }
}
