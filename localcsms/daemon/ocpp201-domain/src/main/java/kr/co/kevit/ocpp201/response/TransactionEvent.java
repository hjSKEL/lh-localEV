/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.IdTokenInfoType;
import kr.co.kevit.ocpp201.domain.MessageContentType;
import kr.co.kevit.ocpp201.domain.TransactionLimitType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class TransactionEvent {
    
    /**
     * type:number
     * "description": "SHALL only be sent when charging has ended. Final total cost of this transaction, including taxes. In the currency configured with the Configuration Variable: &lt;&lt;configkey-currency,`Currency`&gt;&gt;. When omitted, the transaction was NOT free. To indicate a free transaction, the CSMS SHALL send 0.00.\r\n\r\n",
     */
    private Double totalCost;
    
    /**
     * "description": "Priority from a business point of view. Default priority is 0, The range is from -9 to 9. Higher values indicate a higher priority. The chargingPriority in &lt;&lt;transactioneventresponse,TransactionEventResponse&gt;&gt; is temporarily, so it may not be set in the &lt;&lt;cmn_idtokeninfotype,IdTokenInfoType&gt;&gt; afterwards. Also the chargingPriority in &lt;&lt;transactioneventresponse,TransactionEventResponse&gt;&gt; has a higher priority than the one in &lt;&lt;cmn_idtokeninfotype,IdTokenInfoType&gt;&gt;.  \r\n",
     */
    private Integer chargingPriority;
    
    private IdTokenInfoType idTokenInfo;
    
    private MessageContentType updatedPersonalMessage;

    /**
     * (2.1)
     */
    private TransactionLimitType transactionLimit;

    /**
     * (2.1)
     */
    private List<MessageContentType> updatedPersonalMessageExtra;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getChargingPriority() {
        return chargingPriority;
    }

    public void setChargingPriority(Integer chargingPriority) {
        this.chargingPriority = chargingPriority;
    }

    public IdTokenInfoType getIdTokenInfo() {
        return idTokenInfo;
    }

    public void setIdTokenInfo(IdTokenInfoType idTokenInfo) {
        this.idTokenInfo = idTokenInfo;
    }

    public MessageContentType getUpdatedPersonalMessage() {
        return updatedPersonalMessage;
    }

    public void setUpdatedPersonalMessage(MessageContentType updatedPersonalMessage) {
        this.updatedPersonalMessage = updatedPersonalMessage;
    }

    public TransactionLimitType getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(TransactionLimitType transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public List<MessageContentType> getUpdatedPersonalMessageExtra() {
        return updatedPersonalMessageExtra;
    }

    public void setUpdatedPersonalMessageExtra(List<MessageContentType> updatedPersonalMessageExtra) {
        this.updatedPersonalMessageExtra = updatedPersonalMessageExtra;
    }

}
