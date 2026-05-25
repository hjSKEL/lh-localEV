/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * DER Start/Stop 이벤트 (NotifyDERStartStopRequest 수신).
 *
 * TB : TB_DRCTL04
 *
 * @author bckim
 * @since 2026. 5. 26.
 */
public class DerStartStop implements Serializable {

    private static final long serialVersionUID = 5302026001801260004L;

    private Long seq;
    private String cpId;
    private String csId;
    private String controlId;
    private String startedYn;
    private Date timestampDt;
    private Date receivedDate;

    public Long getSeq() { return seq; }
    public void setSeq(Long seq) { this.seq = seq; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getControlId() { return controlId; }
    public void setControlId(String controlId) { this.controlId = controlId; }

    public String getStartedYn() { return startedYn; }
    public void setStartedYn(String startedYn) { this.startedYn = startedYn; }

    public Date getTimestampDt() { return timestampDt; }
    public void setTimestampDt(Date timestampDt) { this.timestampDt = timestampDt; }

    public Date getReceivedDate() { return receivedDate; }
    public void setReceivedDate(Date receivedDate) { this.receivedDate = receivedDate; }
}
