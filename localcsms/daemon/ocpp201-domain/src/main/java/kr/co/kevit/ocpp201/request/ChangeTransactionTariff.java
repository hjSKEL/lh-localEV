/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.TariffType;

/**
 * (2.1)
 */
public class ChangeTransactionTariff {

    /**
     * required
     */
    private TariffType tariff;

    /**
     * required
     */
    private String transactionId;

    private Map<String, Object> customData;

    public TariffType getTariff() {
        return tariff;
    }

    public void setTariff(TariffType tariff) {
        this.tariff = tariff;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
