package kr.co.kevit.localcsms.smartcharging.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;

import java.io.Serializable;
import java.util.Date;

/**
 * TB_CHEN001 — EV ChargingNeeds 스냅샷 (OCPP 2.1 NotifyEVChargingNeeds 수신분).
 *
 * <p>협상/스케줄 계산 입력. 전체 ChargingNeedsType 은 {@code needsJson} 으로 보존,
 * 질의용 헤더(controlMode/requestedEnergyTransfer/departureTime)는 정규 컬럼.</p>
 */
public class ChargingNeedsSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    private long seq;
    private String cpId;
    private String csId;
    private int evseId;
    private String rechargingId;
    private String requestedEnergyTransfer;
    private String controlMode;
    private Date departureTime;
    private Integer maxScheduleTuples;
    private String needsJson;
    private Date receivedAt;
    private Writer writer;

    public long getSeq() { return seq; }
    public void setSeq(long seq) { this.seq = seq; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public int getEvseId() { return evseId; }
    public void setEvseId(int evseId) { this.evseId = evseId; }

    public String getRechargingId() { return rechargingId; }
    public void setRechargingId(String rechargingId) { this.rechargingId = rechargingId; }

    public String getRequestedEnergyTransfer() { return requestedEnergyTransfer; }
    public void setRequestedEnergyTransfer(String requestedEnergyTransfer) { this.requestedEnergyTransfer = requestedEnergyTransfer; }

    public String getControlMode() { return controlMode; }
    public void setControlMode(String controlMode) { this.controlMode = controlMode; }

    public Date getDepartureTime() { return departureTime; }
    public void setDepartureTime(Date departureTime) { this.departureTime = departureTime; }

    public Integer getMaxScheduleTuples() { return maxScheduleTuples; }
    public void setMaxScheduleTuples(Integer maxScheduleTuples) { this.maxScheduleTuples = maxScheduleTuples; }

    public String getNeedsJson() { return needsJson; }
    public void setNeedsJson(String needsJson) { this.needsJson = needsJson; }

    public Date getReceivedAt() { return receivedAt; }
    public void setReceivedAt(Date receivedAt) { this.receivedAt = receivedAt; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }
}
