/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.domain;

import kr.co.kevit.ocpp16.enumtype.ChargingProfileKindEnum;
import kr.co.kevit.ocpp16.enumtype.ChargingProfilePurposeEnum;
import kr.co.kevit.ocpp16.enumtype.RecurrencyKindEnum;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 30.
 */
public class CsChargingProfiles {

    /**
     * required
     */
    private Integer chargingProfileId;
    /**
     * required
     */
    private Integer stackLevel;
    /**
     * required
     */
    private ChargingProfilePurposeEnum chargingProfilePurpose ;
    /**
     * required
     */
    private ChargingProfileKindEnum chargingProfileKind;
    /**
     * required
     */
    private ChargingSchedule chargingSchedule;
    
    private Integer transactionId;
    
    private RecurrencyKindEnum recurrencyKind;
    
    /**
     * "format": "date-time"
     */
    private String validFrom;
    /**
     * "format": "date-time"
     */
    private String validTo;
    public Integer getChargingProfileId() {
        return chargingProfileId;
    }
    public void setChargingProfileId(Integer chargingProfileId) {
        this.chargingProfileId = chargingProfileId;
    }
    public Integer getStackLevel() {
        return stackLevel;
    }
    public void setStackLevel(Integer stackLevel) {
        this.stackLevel = stackLevel;
    }
    public ChargingProfilePurposeEnum getChargingProfilePurpose() {
        return chargingProfilePurpose;
    }
    public void setChargingProfilePurpose(ChargingProfilePurposeEnum chargingProfilePurpose) {
        this.chargingProfilePurpose = chargingProfilePurpose;
    }
    public ChargingProfileKindEnum getChargingProfileKind() {
        return chargingProfileKind;
    }
    public void setChargingProfileKind(ChargingProfileKindEnum chargingProfileKind) {
        this.chargingProfileKind = chargingProfileKind;
    }
    public ChargingSchedule getChargingSchedule() {
        return chargingSchedule;
    }
    public void setChargingSchedule(ChargingSchedule chargingSchedule) {
        this.chargingSchedule = chargingSchedule;
    }
    public Integer getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }
    public RecurrencyKindEnum getRecurrencyKind() {
        return recurrencyKind;
    }
    public void setRecurrencyKind(RecurrencyKindEnum recurrencyKind) {
        this.recurrencyKind = recurrencyKind;
    }
    public String getValidFrom() {
        return validFrom;
    }
    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }
    public String getValidTo() {
        return validTo;
    }
    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }
}