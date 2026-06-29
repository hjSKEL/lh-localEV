/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

/**
 * (2.1)
 */
public class BatteryDataType {

    /**
     * required
     */
    private int evseId;

    /**
     * required
     */
    private String serialNumber;

    /**
     * required
     */
    private double soC;

    /**
     * required
     */
    private double soH;

    private String productionDate;

    private String vendorInfo;

    private Map<String, Object> customData;

    public int getEvseId() {
        return evseId;
    }

    public void setEvseId(int evseId) {
        this.evseId = evseId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getSoC() {
        return soC;
    }

    public void setSoC(double soC) {
        this.soC = soC;
    }

    public double getSoH() {
        return soH;
    }

    public void setSoH(double soH) {
        this.soH = soH;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getVendorInfo() {
        return vendorInfo;
    }

    public void setVendorInfo(String vendorInfo) {
        this.vendorInfo = vendorInfo;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}
