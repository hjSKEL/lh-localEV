/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity.domain;

import java.io.Serializable;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * DER Control 마스터 (OCPP 2.1 use case R04).
 *
 * <p>출처 구분:<br>
 * - {@link #ORIGIN_CSMS} : 운영자가 어드민에 등록 + SetDERControlRequest 송신용<br>
 * - {@link #ORIGIN_CS_REPORT} : CS 가 ReportDERControlRequest 로 보고한 현재 상태</p>
 *
 * <p>같은 controlId 라도 ORIGIN 이 다르면 별 행으로 저장됨 (TB_DRCTL01 PK = CONTROL_ID + ORIGIN_CD).</p>
 *
 * TB : TB_DRCTL01
 *
 * @author bckim
 * @since 2026. 5. 26.
 */
public class DerControl implements Serializable {

    private static final long serialVersionUID = 5302026001801260001L;

    public static final String ORIGIN_CSMS      = "CSMS";
    public static final String ORIGIN_CS_REPORT = "CS_REPORT";

    public static final String STATUS_PENDING  = "PENDING";
    public static final String STATUS_ACTIVE   = "ACTIVE";
    public static final String STATUS_REPLACED = "REPLACED";
    public static final String STATUS_CLEARED  = "CLEARED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_EXPIRED  = "EXPIRED";

    public static final String YES = "Y";
    public static final String NO  = "N";

    public static final String CTRL_ENTER_SERVICE             = "EnterService";
    public static final String CTRL_FREQ_DROOP                = "FreqDroop";
    public static final String CTRL_FREQ_WATT                 = "FreqWatt";
    public static final String CTRL_FIXED_PF_ABSORB           = "FixedPFAbsorb";
    public static final String CTRL_FIXED_PF_INJECT           = "FixedPFInject";
    public static final String CTRL_FIXED_VAR                 = "FixedVar";
    public static final String CTRL_GRADIENTS                 = "Gradients";
    public static final String CTRL_LIMIT_MAX_DISCHARGE       = "LimitMaxDischarge";
    public static final String CTRL_VOLT_WATT                 = "VoltWatt";
    public static final String CTRL_VOLT_VAR                  = "VoltVar";
    public static final String CTRL_WATT_PF                   = "WattPF";
    public static final String CTRL_WATT_VAR                  = "WattVar";
    public static final String CTRL_HF_MUST_TRIP              = "HFMustTrip";
    public static final String CTRL_HF_MAY_TRIP               = "HFMayTrip";
    public static final String CTRL_HV_MUST_TRIP              = "HVMustTrip";
    public static final String CTRL_HV_MOM_CESS               = "HVMomCess";
    public static final String CTRL_HV_MAY_TRIP               = "HVMayTrip";
    public static final String CTRL_LF_MUST_TRIP              = "LFMustTrip";
    public static final String CTRL_LV_MUST_TRIP              = "LVMustTrip";
    public static final String CTRL_LV_MOM_CESS               = "LVMomCess";
    public static final String CTRL_LV_MAY_TRIP               = "LVMayTrip";
    public static final String CTRL_POWER_MONITORING_MUST_TRIP = "PowerMonitoringMustTrip";

    private String controlId;
    private String originCd;
    private String cpId;
    private String csId;
    private String isDefault;
    private String controlType;
    private String subId;
    private Integer priority;
    private Date startTime;
    private Integer durationSec;
    private String isSuperseded;
    private String paramJson;
    private String statusCd = STATUS_PENDING;
    private Date csAckDt;
    private String reasonCd;
    private String description;
    private Writer writer;

    public String getControlId() { return controlId; }
    public void setControlId(String controlId) { this.controlId = controlId; }

    public String getOriginCd() { return originCd; }
    public void setOriginCd(String originCd) { this.originCd = originCd; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getIsDefault() { return isDefault; }
    public void setIsDefault(String isDefault) { this.isDefault = isDefault; }

    public String getControlType() { return controlType; }
    public void setControlType(String controlType) { this.controlType = controlType; }

    public String getSubId() { return subId; }
    public void setSubId(String subId) { this.subId = subId; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Integer getDurationSec() { return durationSec; }
    public void setDurationSec(Integer durationSec) { this.durationSec = durationSec; }

    public String getIsSuperseded() { return isSuperseded; }
    public void setIsSuperseded(String isSuperseded) { this.isSuperseded = isSuperseded; }

    public String getParamJson() { return paramJson; }
    public void setParamJson(String paramJson) { this.paramJson = paramJson; }

    public String getStatusCd() { return statusCd; }
    public void setStatusCd(String statusCd) { this.statusCd = statusCd; }

    public Date getCsAckDt() { return csAckDt; }
    public void setCsAckDt(Date csAckDt) { this.csAckDt = csAckDt; }

    public String getReasonCd() { return reasonCd; }
    public void setReasonCd(String reasonCd) { this.reasonCd = reasonCd; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }
}
