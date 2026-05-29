/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.smartcharging.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * DER 알람 (NotifyDERAlarmRequest 수신).
 *
 * TB : TB_DRCTL03
 *
 * @author bckim
 * @since 2026. 5. 26.
 */
public class DerAlarm implements Serializable {

    private static final long serialVersionUID = 5302026001801260003L;

    private Long seq;
    private String cpId;
    private String csId;
    private String controlType;
    private String gridEventFault;
    private String alarmEnded;     // "Y"/"N"
    private Date timestampDt;
    private String extraInfo;
    private Date receivedDate;

    public Long getSeq() { return seq; }
    public void setSeq(Long seq) { this.seq = seq; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getControlType() { return controlType; }
    public void setControlType(String controlType) { this.controlType = controlType; }

    public String getGridEventFault() { return gridEventFault; }
    public void setGridEventFault(String gridEventFault) { this.gridEventFault = gridEventFault; }

    public String getAlarmEnded() { return alarmEnded; }
    public void setAlarmEnded(String alarmEnded) { this.alarmEnded = alarmEnded; }

    public Date getTimestampDt() { return timestampDt; }
    public void setTimestampDt(Date timestampDt) { this.timestampDt = timestampDt; }

    public String getExtraInfo() { return extraInfo; }
    public void setExtraInfo(String extraInfo) { this.extraInfo = extraInfo; }

    public Date getReceivedDate() { return receivedDate; }
    public void setReceivedDate(Date receivedDate) { this.receivedDate = receivedDate; }
}
