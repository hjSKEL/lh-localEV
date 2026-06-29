/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.EnergyTransferModeEnumType;

/**
 * (2.1)
 */
public class NotifyAllowedEnergyTransfer {

    /**
     * required
     */
    private String transactionId;

    /**
     * required
     */
    private List<EnergyTransferModeEnumType> allowedEnergyTransfer;

    private Map<String, Object> customData;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<EnergyTransferModeEnumType> getAllowedEnergyTransfer() {
        return allowedEnergyTransfer;
    }

    public void setAllowedEnergyTransfer(List<EnergyTransferModeEnumType> allowedEnergyTransfer) {
        this.allowedEnergyTransfer = allowedEnergyTransfer;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
