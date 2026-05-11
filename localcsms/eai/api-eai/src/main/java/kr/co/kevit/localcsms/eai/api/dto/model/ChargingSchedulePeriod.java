package kr.co.kevit.localcsms.eai.api.dto.model;

/** OCPP 1.6 ChargingSchedulePeriod */
public class ChargingSchedulePeriod {

    /** 스케줄 시작 기준 오프셋(초) */
    private int startPeriod;
    /** 전력 제한값 (W or A) */
    private double limit;
    /** 적용 상수 (1~3, 기본 3) */
    private Integer numberPhases;

    public int getStartPeriod()        { return startPeriod; }
    public void setStartPeriod(int v)  { this.startPeriod = v; }

    public double getLimit()           { return limit; }
    public void setLimit(double v)     { this.limit = v; }

    public Integer getNumberPhases()        { return numberPhases; }
    public void setNumberPhases(Integer v)  { this.numberPhases = v; }
}
