package kr.co.kevit.localcsms.smartcharging.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;

import java.io.Serializable;

/**
 * TB_CHNG001 — ISO 15118-20 협상 상태 (EVSE 1:1).
 *
 * <p>STATE 전이: NEEDS_RECEIVED → PROFILE_SENT → SCHEDULE_CONFIRMED.</p>
 */
public class NegotiationState implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String NEEDS_RECEIVED     = "NEEDS_RECEIVED";
    public static final String PROFILE_SENT       = "PROFILE_SENT";
    public static final String SCHEDULE_CONFIRMED = "SCHEDULE_CONFIRMED";

    private String cpId;
    private String csId;
    private int evseId;
    private String rechargingId;
    private String state;
    private String controlMode;
    private Integer lastProfileId;
    private Writer writer;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public int getEvseId() { return evseId; }
    public void setEvseId(int evseId) { this.evseId = evseId; }

    public String getRechargingId() { return rechargingId; }
    public void setRechargingId(String rechargingId) { this.rechargingId = rechargingId; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getControlMode() { return controlMode; }
    public void setControlMode(String controlMode) { this.controlMode = controlMode; }

    public Integer getLastProfileId() { return lastProfileId; }
    public void setLastProfileId(Integer lastProfileId) { this.lastProfileId = lastProfileId; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }
}
