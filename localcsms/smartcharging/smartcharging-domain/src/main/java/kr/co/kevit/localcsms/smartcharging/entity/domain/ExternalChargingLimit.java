package kr.co.kevit.localcsms.smartcharging.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;

import java.io.Serializable;

/**
 * TB_CHLM001 — 외부 충전 제약 (OCPP 2.1 NotifyChargingLimit, K15).
 *
 * <p>EMS/CSO/SO/grid 등 외부 소스가 통보한 충전 한계. 합성 스케줄 계산 시 최우선 상한으로 반영.</p>
 */
public class ExternalChargingLimit implements Serializable {

    private static final long serialVersionUID = 1L;

    private long seq;
    private String cpId;
    private String csId;
    private int evseId;
    private String limitSource;
    private Boolean gridCritical;
    private Boolean localGeneration;
    private String scheduleJson;
    private String activeYn;
    private Writer writer;

    public long getSeq() { return seq; }
    public void setSeq(long seq) { this.seq = seq; }

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public int getEvseId() { return evseId; }
    public void setEvseId(int evseId) { this.evseId = evseId; }

    public String getLimitSource() { return limitSource; }
    public void setLimitSource(String limitSource) { this.limitSource = limitSource; }

    public Boolean getGridCritical() { return gridCritical; }
    public void setGridCritical(Boolean gridCritical) { this.gridCritical = gridCritical; }

    public Boolean getLocalGeneration() { return localGeneration; }
    public void setLocalGeneration(Boolean localGeneration) { this.localGeneration = localGeneration; }

    public String getScheduleJson() { return scheduleJson; }
    public void setScheduleJson(String scheduleJson) { this.scheduleJson = scheduleJson; }

    public String getActiveYn() { return activeYn; }
    public void setActiveYn(String activeYn) { this.activeYn = activeYn; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }
}
