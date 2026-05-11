package kr.co.kevit.localcsms.eai.api.dto.model;

import kr.co.kevit.localcsms.eai.api.dto.type.ChargingProfileKindType;
import kr.co.kevit.localcsms.eai.api.dto.type.ChargingProfilePurposeType;
import kr.co.kevit.localcsms.eai.api.dto.type.RecurrencyKindType;

import java.time.OffsetDateTime;

/** OCPP 1.6 ChargingProfile */
public class ChargingProfile {

    private int chargingProfileId;
    /** 부모 프로파일 ID (옵션) */
    private Integer transactionId;
    private int stackLevel;
    private ChargingProfilePurposeType chargingProfilePurpose;
    private ChargingProfileKindType chargingProfileKind;
    /** Recurring 시에만 필요 */
    private RecurrencyKindType recurrencyKind;
    private OffsetDateTime validFrom;
    private OffsetDateTime validTo;
    private ChargingSchedule chargingSchedule;

    public int getChargingProfileId()                              { return chargingProfileId; }
    public void setChargingProfileId(int v)                        { this.chargingProfileId = v; }

    public Integer getTransactionId()                              { return transactionId; }
    public void setTransactionId(Integer v)                        { this.transactionId = v; }

    public int getStackLevel()                                     { return stackLevel; }
    public void setStackLevel(int v)                               { this.stackLevel = v; }

    public ChargingProfilePurposeType getChargingProfilePurpose()  { return chargingProfilePurpose; }
    public void setChargingProfilePurpose(ChargingProfilePurposeType v) { this.chargingProfilePurpose = v; }

    public ChargingProfileKindType getChargingProfileKind()        { return chargingProfileKind; }
    public void setChargingProfileKind(ChargingProfileKindType v)  { this.chargingProfileKind = v; }

    public RecurrencyKindType getRecurrencyKind()                  { return recurrencyKind; }
    public void setRecurrencyKind(RecurrencyKindType v)            { this.recurrencyKind = v; }

    public OffsetDateTime getValidFrom()                           { return validFrom; }
    public void setValidFrom(OffsetDateTime v)                     { this.validFrom = v; }

    public OffsetDateTime getValidTo()                             { return validTo; }
    public void setValidTo(OffsetDateTime v)                       { this.validTo = v; }

    public ChargingSchedule getChargingSchedule()                  { return chargingSchedule; }
    public void setChargingSchedule(ChargingSchedule v)            { this.chargingSchedule = v; }
}
