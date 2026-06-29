/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

import java.util.List;

import kr.co.kevit.ocpp16.domain.TransactionData;
import kr.co.kevit.ocpp16.enumtype.StopTransactionReasonEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 26.
 */
public class StopTransaction {
    
    /**
     * "maxLength": 20
     */
    private String idTag;
    
    /**
     * required
     */
    private Integer meterStop;
    
    /**
     * required
     * "format": "date-time"
     */
    private String timestamp;
    
    /**
     * required
     */
    private Integer transactionId = 0;
    
    /**
     * 
     */
    private StopTransactionReasonEnum reason;
    
    /**
     * 
     */
    private List<TransactionData> transactionData;

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    public Integer getMeterStop() {
        return meterStop;
    }

    public void setMeterStop(Integer meterStop) {
        this.meterStop = meterStop;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public StopTransactionReasonEnum getReason() {
        return reason;
    }

    public void setReason(StopTransactionReasonEnum reason) {
        this.reason = reason;
    }

    public List<TransactionData> getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(List<TransactionData> transactionData) {
        this.transactionData = transactionData;
    }
}