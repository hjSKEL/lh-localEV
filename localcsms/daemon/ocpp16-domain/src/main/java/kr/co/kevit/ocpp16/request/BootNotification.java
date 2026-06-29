/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 2. 20.
 */
public class BootNotification {

    /**
     * required
     * "maxLength": 20
     */
    private String chargePointVendor;
    
    /**
     * required
     * "maxLength": 20
     */
    private String chargePointModel;
    
    /**
     * "maxLength": 25
     */
    private String chargePointSerialNumber;
    
    /**
     * "maxLength": 25
     */
    private String chargeBoxSerialNumber;
    
    /**
     * "maxLength": 50
     */
    private String firmwareVersion;
    
    /**
     * "maxLength": 20
     */
    private String iccid;
    
    /**
     * "maxLength": 20
     */
    private String imsi;
    
    /**
     * for KEVIT
     * "maxLength": 20
     */
    private String imei;

    /**
     * for KEVIT
     * "maxLength": 20
     */
    private String phoneNumber;
    
    /**
     * "maxLength": 25
     */
    private String meterType;
    
    /**
     * "maxLength": 25
     */
    private String meterSerialNumber;

    public String getChargePointVendor() {
        return chargePointVendor;
    }

    public void setChargePointVendor(String chargePointVendor) {
        this.chargePointVendor = chargePointVendor;
    }

    public String getChargePointModel() {
        return chargePointModel;
    }

    public void setChargePointModel(String chargePointModel) {
        this.chargePointModel = chargePointModel;
    }

    public String getChargePointSerialNumber() {
        return chargePointSerialNumber;
    }

    public void setChargePointSerialNumber(String chargePointSerialNumber) {
        this.chargePointSerialNumber = chargePointSerialNumber;
    }

    public String getChargeBoxSerialNumber() {
        return chargeBoxSerialNumber;
    }

    public void setChargeBoxSerialNumber(String chargeBoxSerialNumber) {
        this.chargeBoxSerialNumber = chargeBoxSerialNumber;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getMeterSerialNumber() {
        return meterSerialNumber;
    }

    public void setMeterSerialNumber(String meterSerialNumber) {
        this.meterSerialNumber = meterSerialNumber;
    }

    /**
     * Get imsi
     * @return imsi
     */
    public String getImsi() {
        return imsi;
    }

    /**
     * Set imsi
     * @param imsi
     */
    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

}