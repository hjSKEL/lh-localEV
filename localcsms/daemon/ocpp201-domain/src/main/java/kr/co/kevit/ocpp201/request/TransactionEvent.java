/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.CostDetailsType;
import kr.co.kevit.ocpp201.domain.EVSEType;
import kr.co.kevit.ocpp201.domain.IdTokenType;
import kr.co.kevit.ocpp201.domain.MeterValueType;
import kr.co.kevit.ocpp201.domain.TransactionType;
import kr.co.kevit.ocpp201.enumtype.PreconditioningStatusEnumType;
import kr.co.kevit.ocpp201.enumtype.TransactionEventEnumType;
import kr.co.kevit.ocpp201.enumtype.TriggerReasonEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class TransactionEvent {
    
    /**
     * required
     */
    private TransactionEventEnumType eventType;
    
    /**
     * "minItems": 1
     */
    private List<MeterValueType> meterValue;
    
    /**
     * required
     * "description": "The date and time at which this transaction event occurred.\r\n",
     * "type": "string","format": "date-time"
     */
    private String timestamp;
    
    /**
     * required
     */
    private TriggerReasonEnumType triggerReason;
    
    /**
     * required
     * "description": "Incremental sequence number, helps with determining if all messages of a transaction have been received.\r\n",
     */
    private int seqNo;
    
    /**
     * "description": "Indication that this transaction event happened when the Charging Station was offline. Default = false, meaning: the event occurred when the Charging Station was online.\r\n",
     */
    private boolean offline = false;
    
    /**
     * "description": "If the Charging Station is able to report the number of phases used, then it SHALL provide it. When omitted the CSMS may be able to determine the number of phases used via device management.\r\n",
     */
    private Integer numberOfPhasesUsed;
    
    /**
     * "description": "The maximum current of the connected cable in Ampere (A).\r\n",
     *"type": "number"
     */
    private Integer cableMaxCurrent;
    
    /**
     * "description": "This contains the Id of the reservation that terminates as a result of this transaction.\r\n",
     */
    private Integer reservationId;
    
    /**
     * required
     */
    private TransactionType transactionInfo;
    
    private EVSEType evse;
    
    private IdTokenType idToken;

    /**
     * (2.1)
     */
    private CostDetailsType costDetails;

    /**
     * (2.1)
     */
    private PreconditioningStatusEnumType preconditioningStatus;

    /**
     * (2.1)
     */
    private Boolean evseSleep;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public TransactionEventEnumType getEventType() {
        return eventType;
    }

    public void setEventType(TransactionEventEnumType eventType) {
        this.eventType = eventType;
    }

    public List<MeterValueType> getMeterValue() {
        return meterValue;
    }

    public void setMeterValue(List<MeterValueType> meterValue) {
        this.meterValue = meterValue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public TriggerReasonEnumType getTriggerReason() {
        return triggerReason;
    }

    public void setTriggerReason(TriggerReasonEnumType triggerReason) {
        this.triggerReason = triggerReason;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public Integer getNumberOfPhasesUsed() {
        return numberOfPhasesUsed;
    }

    public void setNumberOfPhasesUsed(Integer numberOfPhasesUsed) {
        this.numberOfPhasesUsed = numberOfPhasesUsed;
    }

    public Integer getCableMaxCurrent() {
        return cableMaxCurrent;
    }

    public void setCableMaxCurrent(Integer cableMaxCurrent) {
        this.cableMaxCurrent = cableMaxCurrent;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public TransactionType getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(TransactionType transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public EVSEType getEvse() {
        return evse;
    }

    public void setEvse(EVSEType evse) {
        this.evse = evse;
    }

    public IdTokenType getIdToken() {
        return idToken;
    }

    public void setIdToken(IdTokenType idToken) {
        this.idToken = idToken;
    }

    public CostDetailsType getCostDetails() {
        return costDetails;
    }

    public void setCostDetails(CostDetailsType costDetails) {
        this.costDetails = costDetails;
    }

    public PreconditioningStatusEnumType getPreconditioningStatus() {
        return preconditioningStatus;
    }

    public void setPreconditioningStatus(PreconditioningStatusEnumType preconditioningStatus) {
        this.preconditioningStatus = preconditioningStatus;
    }

    public Boolean getEvseSleep() {
        return evseSleep;
    }

    public void setEvseSleep(Boolean evseSleep) {
        this.evseSleep = evseSleep;
    }

}
