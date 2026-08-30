/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import java.util.List;

import kr.co.kevit.ocpp16.domain.MeterValue;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 26.
 */
public class MeterValues {
    
    /**
     * required
     */
    private Integer connectorId;
    
    private Integer transactionId;
    
    /**
     * required
     */
    private List<MeterValue> meterValue;

    public Integer getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(Integer connectorId) {
        this.connectorId = connectorId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public List<MeterValue> getMeterValue() {
        return meterValue;
    }

    public void setMeterValue(List<MeterValue> meterValue) {
        this.meterValue = meterValue;
    }

}
