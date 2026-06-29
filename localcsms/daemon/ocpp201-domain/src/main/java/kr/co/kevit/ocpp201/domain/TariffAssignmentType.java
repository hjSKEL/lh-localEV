/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.TariffKindEnumType;

/**
 * (2.1)
 */
public class TariffAssignmentType {

    /**
     * required
     */
    private String tariffId;

    /**
     * required
     */
    private TariffKindEnumType tariffKind;

    private String validFrom;

    private List<Integer> evseIds;

    private List<String> idTokens;

    private Map<String, Object> customData;

    public String getTariffId() {
        return tariffId;
    }

    public void setTariffId(String tariffId) {
        this.tariffId = tariffId;
    }

    public TariffKindEnumType getTariffKind() {
        return tariffKind;
    }

    public void setTariffKind(TariffKindEnumType tariffKind) {
        this.tariffKind = tariffKind;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public List<Integer> getEvseIds() {
        return evseIds;
    }

    public void setEvseIds(List<Integer> evseIds) {
        this.evseIds = evseIds;
    }

    public List<String> getIdTokens() {
        return idTokens;
    }

    public void setIdTokens(List<String> idTokens) {
        this.idTokens = idTokens;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

}
