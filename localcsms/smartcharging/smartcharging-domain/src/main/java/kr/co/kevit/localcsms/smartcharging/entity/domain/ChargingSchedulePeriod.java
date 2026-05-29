package kr.co.kevit.localcsms.smartcharging.entity.domain;

import java.io.Serializable;

/**
 * TB_CHPF003 — 충전 스케줄 기간 (OCPP 2.1 ChargingSchedulePeriodType 정규화).
 *
 * <p>limit/setpoint/dischargeLimit 계열은 W 또는 A 단위(스케줄의 rateUnit). v2x 곡선은 JSON 보존.</p>
 */
public class ChargingSchedulePeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    private int profileId;
    private int scheduleSeq;
    private int periodSeq;

    private int startPeriod;
    private Double limit;
    private Double limitL2;
    private Double limitL3;
    private Integer numberPhases;
    private Integer phaseToUse;
    private Double dischargeLimit;
    private Double dischargeLimitL2;
    private Double dischargeLimitL3;
    private Double setpoint;
    private Double setpointL2;
    private Double setpointL3;
    private Double setpointReactive;
    private Double setpointReactiveL2;
    private Double setpointReactiveL3;
    private String operationMode;
    private Boolean evseSleep;
    private Integer v2xBaseline;
    private Boolean preconditioningRequest;
    private String v2xFreqWattJson;
    private String v2xSignalWattJson;

    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }

    public int getScheduleSeq() { return scheduleSeq; }
    public void setScheduleSeq(int scheduleSeq) { this.scheduleSeq = scheduleSeq; }

    public int getPeriodSeq() { return periodSeq; }
    public void setPeriodSeq(int periodSeq) { this.periodSeq = periodSeq; }

    public int getStartPeriod() { return startPeriod; }
    public void setStartPeriod(int startPeriod) { this.startPeriod = startPeriod; }

    public Double getLimit() { return limit; }
    public void setLimit(Double limit) { this.limit = limit; }

    public Double getLimitL2() { return limitL2; }
    public void setLimitL2(Double limitL2) { this.limitL2 = limitL2; }

    public Double getLimitL3() { return limitL3; }
    public void setLimitL3(Double limitL3) { this.limitL3 = limitL3; }

    public Integer getNumberPhases() { return numberPhases; }
    public void setNumberPhases(Integer numberPhases) { this.numberPhases = numberPhases; }

    public Integer getPhaseToUse() { return phaseToUse; }
    public void setPhaseToUse(Integer phaseToUse) { this.phaseToUse = phaseToUse; }

    public Double getDischargeLimit() { return dischargeLimit; }
    public void setDischargeLimit(Double dischargeLimit) { this.dischargeLimit = dischargeLimit; }

    public Double getDischargeLimitL2() { return dischargeLimitL2; }
    public void setDischargeLimitL2(Double dischargeLimitL2) { this.dischargeLimitL2 = dischargeLimitL2; }

    public Double getDischargeLimitL3() { return dischargeLimitL3; }
    public void setDischargeLimitL3(Double dischargeLimitL3) { this.dischargeLimitL3 = dischargeLimitL3; }

    public Double getSetpoint() { return setpoint; }
    public void setSetpoint(Double setpoint) { this.setpoint = setpoint; }

    public Double getSetpointL2() { return setpointL2; }
    public void setSetpointL2(Double setpointL2) { this.setpointL2 = setpointL2; }

    public Double getSetpointL3() { return setpointL3; }
    public void setSetpointL3(Double setpointL3) { this.setpointL3 = setpointL3; }

    public Double getSetpointReactive() { return setpointReactive; }
    public void setSetpointReactive(Double setpointReactive) { this.setpointReactive = setpointReactive; }

    public Double getSetpointReactiveL2() { return setpointReactiveL2; }
    public void setSetpointReactiveL2(Double setpointReactiveL2) { this.setpointReactiveL2 = setpointReactiveL2; }

    public Double getSetpointReactiveL3() { return setpointReactiveL3; }
    public void setSetpointReactiveL3(Double setpointReactiveL3) { this.setpointReactiveL3 = setpointReactiveL3; }

    public String getOperationMode() { return operationMode; }
    public void setOperationMode(String operationMode) { this.operationMode = operationMode; }

    public Boolean getEvseSleep() { return evseSleep; }
    public void setEvseSleep(Boolean evseSleep) { this.evseSleep = evseSleep; }

    public Integer getV2xBaseline() { return v2xBaseline; }
    public void setV2xBaseline(Integer v2xBaseline) { this.v2xBaseline = v2xBaseline; }

    public Boolean getPreconditioningRequest() { return preconditioningRequest; }
    public void setPreconditioningRequest(Boolean preconditioningRequest) { this.preconditioningRequest = preconditioningRequest; }

    public String getV2xFreqWattJson() { return v2xFreqWattJson; }
    public void setV2xFreqWattJson(String v2xFreqWattJson) { this.v2xFreqWattJson = v2xFreqWattJson; }

    public String getV2xSignalWattJson() { return v2xSignalWattJson; }
    public void setV2xSignalWattJson(String v2xSignalWattJson) { this.v2xSignalWattJson = v2xSignalWattJson; }
}
