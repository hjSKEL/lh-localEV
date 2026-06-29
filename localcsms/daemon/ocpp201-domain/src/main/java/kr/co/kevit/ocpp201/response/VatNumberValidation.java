/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import kr.co.kevit.ocpp201.domain.*;
import kr.co.kevit.ocpp201.enumtype.GenericStatusEnumType;

import java.util.Map;

/**
 * (2.1)
 */
public class VatNumberValidation {

    /**
     * required
     */
    private String vatNumber;

    /**
     * required
     */
    private GenericStatusEnumType status;

    private AddressType company;

    private StatusInfoType statusInfo;

    private Integer evseId;

    private Map<String, Object> customData;

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public GenericStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(GenericStatusEnumType status) {
        this.status = status;
    }

    public AddressType getCompany() {
        return company;
    }

    public void setCompany(AddressType company) {
        this.company = company;
    }

    public StatusInfoType getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfoType statusInfo) {
        this.statusInfo = statusInfo;
    }

    public Integer getEvseId() {
        return evseId;
    }

    public void setEvseId(Integer evseId) {
        this.evseId = evseId;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
