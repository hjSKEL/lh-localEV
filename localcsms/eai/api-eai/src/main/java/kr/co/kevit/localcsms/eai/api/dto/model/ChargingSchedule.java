package kr.co.kevit.localcsms.eai.api.dto.model;

import kr.co.kevit.localcsms.eai.api.dto.type.ChargingRateUnitType;

import java.time.OffsetDateTime;
import java.util.List;

/** OCPP 1.6 ChargingSchedule */
public class ChargingSchedule {

    /** 스케줄 지속 시간(초, 옵션) */
    private Integer duration;
    /** 스케줄 시작 시각 (옵션) */
    private OffsetDateTime startSchedule;
    /** 전력 단위 */
    private ChargingRateUnitType chargingRateUnit;
    /** 기간별 전력 제한 목록 */
    private List<ChargingSchedulePeriod> chargingSchedulePeriod;
    /** 최소 충전 속도 (옵션) */
    private Double minChargingRate;

    public Integer getDuration()                                        { return duration; }
    public void setDuration(Integer v)                                  { this.duration = v; }

    public OffsetDateTime getStartSchedule()                            { return startSchedule; }
    public void setStartSchedule(OffsetDateTime v)                      { this.startSchedule = v; }

    public ChargingRateUnitType getChargingRateUnit()                   { return chargingRateUnit; }
    public void setChargingRateUnit(ChargingRateUnitType v)             { this.chargingRateUnit = v; }

    public List<ChargingSchedulePeriod> getChargingSchedulePeriod()     { return chargingSchedulePeriod; }
    public void setChargingSchedulePeriod(List<ChargingSchedulePeriod> v) { this.chargingSchedulePeriod = v; }

    public Double getMinChargingRate()                                  { return minChargingRate; }
    public void setMinChargingRate(Double v)                            { this.minChargingRate = v; }
}
