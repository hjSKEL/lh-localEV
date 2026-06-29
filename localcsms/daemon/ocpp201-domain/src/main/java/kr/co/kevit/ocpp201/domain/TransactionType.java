/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ChargingStateEnumType;
import kr.co.kevit.ocpp201.enumtype.OperationModeEnumType;
import kr.co.kevit.ocpp201.enumtype.ReasonEnumType;

/**
 * "description": "Transaction\r\nurn:x-oca:ocpp:uid:2:233318\r\n",
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class TransactionType {
    
    /**
     * required
     * "description": "This contains the Id of the transaction.\r\n",
     * "maxLength": 36
     */
    private String transactionId;
    
    private ChargingStateEnumType chargingState;
    
    /**
     * "description": "Transaction. Time_ Spent_ Charging. Elapsed_ Time\r\nurn:x-oca:ocpp:uid:1:569415\r\nContains the total time that energy flowed from EVSE to EV during the transaction (in seconds). Note that timeSpentCharging is smaller or equal to the duration of the transaction.\r\n",
     */
    private Integer timeSpentCharging;
    
    private ReasonEnumType stoppedReason;
    
    /**
     * "description": "The ID given to remote start request (&lt;&lt;requeststarttransactionrequest, RequestStartTransactionRequest&gt;&gt;. This enables to CSMS to match the started transaction to the given start request.\r\n",
     */
    private Integer remoteStartId;

    /**
     * (2.1)
     */
    private OperationModeEnumType operationMode;

    /**
     * (2.1)
     */
    private String tariffId;

    /**
     * (2.1)
     */
    private TransactionLimitType transactionLimit;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public ChargingStateEnumType getChargingState() {
        return chargingState;
    }

    public void setChargingState(ChargingStateEnumType chargingState) {
        this.chargingState = chargingState;
    }

    public Integer getTimeSpentCharging() {
        return timeSpentCharging;
    }

    public void setTimeSpentCharging(Integer timeSpentCharging) {
        this.timeSpentCharging = timeSpentCharging;
    }

    public ReasonEnumType getStoppedReason() {
        return stoppedReason;
    }

    public void setStoppedReason(ReasonEnumType stoppedReason) {
        this.stoppedReason = stoppedReason;
    }

    public Integer getRemoteStartId() {
        return remoteStartId;
    }

    public void setRemoteStartId(Integer remoteStartId) {
        this.remoteStartId = remoteStartId;
    }

    public OperationModeEnumType getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(OperationModeEnumType operationMode) {
        this.operationMode = operationMode;
    }

    public String getTariffId() {
        return tariffId;
    }

    public void setTariffId(String tariffId) {
        this.tariffId = tariffId;
    }

    public TransactionLimitType getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(TransactionLimitType transactionLimit) {
        this.transactionLimit = transactionLimit;
    }
}
