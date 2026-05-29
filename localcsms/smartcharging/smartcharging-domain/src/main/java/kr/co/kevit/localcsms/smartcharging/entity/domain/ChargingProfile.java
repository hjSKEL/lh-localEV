package kr.co.kevit.localcsms.smartcharging.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfileKind;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TB_CHPF001 / TB_CHPF002
 * OCPP 2.x SetChargingProfile - 충전 프로파일
 */
public class ChargingProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    /** PK: 프로파일 ID  PRFL_ID INT */
    private int profileId;

    /** 충전소 ID  CP_ID CHAR(6) */
    private String cpId;

    /** 충전기 ID  CS_ID CHAR(2) */
    private String csId;

    /** EVSE ID (0=충전기 전체)  EVSE_ID INT */
    private int evseId;

    /** 스택 레벨  STACK_LVL INT */
    private int stackLevel;

    /** 프로파일 목적 공통코드 CHPP00  PRPS CHAR(6) */
    private ChargingProfilePurpose purpose;

    /** 프로파일 종류 공통코드 CHKD00  KIND CHAR(6) */
    private ChargingProfileKind kind;

    /** 반복 종류 (D=Daily / W=Weekly)  RCRNC_KIND CHAR(1) */
    private String recurrencyKind;

    /** 유효 시작  VALID_FROM DATETIME */
    private Date validFrom;

    /** 유효 종료  VALID_TO DATETIME */
    private Date validTo;

    /** 연관 트랜잭션 ID (TxProfile용)  RC_ID VARCHAR(36) */
    private String rechargingId;

    /** 충전기 응답 상태  CS_STATUS CHAR(6) */
    private String csStatus;

    /** (2.1) 오프라인 시 프로파일 유효 지속(초)  MAX_OFFLINE_DURATION */
    private Integer maxOfflineDuration;

    /** (2.1) 오프라인 초과 후 무효화 여부  INVALID_AFTER_OFFLINE Y/N */
    private Boolean invalidAfterOfflineDuration;

    /** (2.1) Dynamic 프로파일 업데이트 주기(초)  DYN_UPDATE_INTERVAL */
    private Integer dynUpdateInterval;

    /** (2.1) 마지막 Dynamic 업데이트 시각  DYN_UPDATE_TIME */
    private Date dynUpdateTime;

    /** (2.1) priceSchedule 서명(Base64)  PRICE_SCHED_SIGNATURE */
    private String priceScheduleSignature;

    /** 등록/수정 정보 */
    private Writer writer;

    /** 충전 스케줄 목록 (TB_CHPF002 + TB_CHPF003 정규화) */
    private List<ChargingSchedule> schedules = new ArrayList<>();

    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public int getEvseId() { return evseId; }
    public void setEvseId(int evseId) { this.evseId = evseId; }

    public int getStackLevel() { return stackLevel; }
    public void setStackLevel(int stackLevel) { this.stackLevel = stackLevel; }

    public ChargingProfilePurpose getPurpose() { return purpose; }
    public void setPurpose(ChargingProfilePurpose purpose) { this.purpose = purpose; }

    public ChargingProfileKind getKind() { return kind; }
    public void setKind(ChargingProfileKind kind) { this.kind = kind; }

    public String getRecurrencyKind() { return recurrencyKind; }
    public void setRecurrencyKind(String recurrencyKind) { this.recurrencyKind = recurrencyKind; }

    public Date getValidFrom() { return validFrom; }
    public void setValidFrom(Date validFrom) { this.validFrom = validFrom; }

    public Date getValidTo() { return validTo; }
    public void setValidTo(Date validTo) { this.validTo = validTo; }

    public String getRechargingId() { return rechargingId; }
    public void setRechargingId(String rechargingId) { this.rechargingId = rechargingId; }

    public String getCsStatus() { return csStatus; }
    public void setCsStatus(String csStatus) { this.csStatus = csStatus; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }

    public Integer getMaxOfflineDuration() { return maxOfflineDuration; }
    public void setMaxOfflineDuration(Integer maxOfflineDuration) { this.maxOfflineDuration = maxOfflineDuration; }

    public Boolean getInvalidAfterOfflineDuration() { return invalidAfterOfflineDuration; }
    public void setInvalidAfterOfflineDuration(Boolean invalidAfterOfflineDuration) { this.invalidAfterOfflineDuration = invalidAfterOfflineDuration; }

    public Integer getDynUpdateInterval() { return dynUpdateInterval; }
    public void setDynUpdateInterval(Integer dynUpdateInterval) { this.dynUpdateInterval = dynUpdateInterval; }

    public Date getDynUpdateTime() { return dynUpdateTime; }
    public void setDynUpdateTime(Date dynUpdateTime) { this.dynUpdateTime = dynUpdateTime; }

    public String getPriceScheduleSignature() { return priceScheduleSignature; }
    public void setPriceScheduleSignature(String priceScheduleSignature) { this.priceScheduleSignature = priceScheduleSignature; }

    public List<ChargingSchedule> getSchedules() { return schedules; }
    public void setSchedules(List<ChargingSchedule> schedules) { this.schedules = schedules; }
}
