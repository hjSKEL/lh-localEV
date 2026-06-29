/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ChargingProfileKindEnumType;
import kr.co.kevit.ocpp201.enumtype.ChargingProfilePurposeEnumType;
import kr.co.kevit.ocpp201.enumtype.RecurrencyKindEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2020. 1. 15.
 */
public class ChargingProfileType {

    /**
     * required
     */
    private int id;

    /**
     * required
     */
    private int stackLevel;

    /**
     * required
     */
    private ChargingProfilePurposeEnumType chargingProfilePurpose;

    /**
     * required
     */
    private ChargingProfileKindEnumType chargingProfileKind;

    private RecurrencyKindEnumType recurrencyKind;

    /**
     * "type": "string","format": "date-time"
     */
    private String validFrom;

    /**
     * "type": "string","format": "date-time"
     */
    private String validTo;

    /**
     * required
     * "minItems": 1,"maxItems": 3
     */
    private List<ChargingScheduleType> chargingSchedule;

    /**
     * "type": "string","maxLength": 36
     */
    private String transactionId;

    /**
     * *(2.1)* Period in seconds that this charging profile remains valid after the
     * Charging Station has gone offline.
     * After this period the charging profile permanently becomes invalid and
     * Charging Station reverts back to a valid profile with a lower stack level.
     * value of 0 or no value means that no timeout applies and the charging profile
     * is valid when offline.
     */
    private Integer maxOfflineDuration;

    /**
     * *(2.1)* When set to true this charging profile will not be valid anymore
     * after being offline for more than _maxOfflineDuration_.
     * When absent defaults to false.
     */
    private Boolean invalidAfterOfflineDuration;

    /**
     * *(2.1)* Interval in seconds after receipt of last update, when to request a
     * profile update by sending a PullDynamicScheduleUpdateRequest message.
     * A value of 0 or no value means that no update interval applies.
     * Only relevant in a dynamic charging profile.
     */
    private Integer dynUpdateInterval;

    /**
     * *(2.1)* Time at which limits or setpoints in this charging profile were last
     * updated by a PullDynamicScheduleUpdateRequest or UpdateDynamicScheduleRequest
     * or by an external actor.
     * Only relevant in a dynamic charging profile.
     */
    private String dynUpdateTime;

    /**
     * *(2.1)* ISO 15118-20 signature for all price schedules in
     * _chargingSchedules_.
     * Note: for 256-bit elliptic curves (like secp256k1) the ECDSA signature is 512
     * bits (64 bytes) and for 521-bit curves (like secp521r1) the signature is 1042
     * bits.
     * This equals 131 bytes, which can be encoded as base64 in 176 bytes.
     */
    private String priceScheduleSignature;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStackLevel() {
        return stackLevel;
    }

    public void setStackLevel(int stackLevel) {
        this.stackLevel = stackLevel;
    }

    public ChargingProfilePurposeEnumType getChargingProfilePurpose() {
        return chargingProfilePurpose;
    }

    public void setChargingProfilePurpose(ChargingProfilePurposeEnumType chargingProfilePurpose) {
        this.chargingProfilePurpose = chargingProfilePurpose;
    }

    public ChargingProfileKindEnumType getChargingProfileKind() {
        return chargingProfileKind;
    }

    public void setChargingProfileKind(ChargingProfileKindEnumType chargingProfileKind) {
        this.chargingProfileKind = chargingProfileKind;
    }

    public RecurrencyKindEnumType getRecurrencyKind() {
        return recurrencyKind;
    }

    public void setRecurrencyKind(RecurrencyKindEnumType recurrencyKind) {
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

    public List<ChargingScheduleType> getChargingSchedule() {
        return chargingSchedule;
    }

    public void setChargingSchedule(List<ChargingScheduleType> chargingSchedule) {
        this.chargingSchedule = chargingSchedule;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getMaxOfflineDuration() {
        return maxOfflineDuration;
    }

    public void setMaxOfflineDuration(Integer maxOfflineDuration) {
        this.maxOfflineDuration = maxOfflineDuration;
    }

    public Boolean getInvalidAfterOfflineDuration() {
        return invalidAfterOfflineDuration;
    }

    public void setInvalidAfterOfflineDuration(Boolean invalidAfterOfflineDuration) {
        this.invalidAfterOfflineDuration = invalidAfterOfflineDuration;
    }

    public Integer getDynUpdateInterval() {
        return dynUpdateInterval;
    }

    public void setDynUpdateInterval(Integer dynUpdateInterval) {
        this.dynUpdateInterval = dynUpdateInterval;
    }

    public String getDynUpdateTime() {
        return dynUpdateTime;
    }

    public void setDynUpdateTime(String dynUpdateTime) {
        this.dynUpdateTime = dynUpdateTime;
    }

    public String getPriceScheduleSignature() {
        return priceScheduleSignature;
    }

    public void setPriceScheduleSignature(String priceScheduleSignature) {
        this.priceScheduleSignature = priceScheduleSignature;
    }
}