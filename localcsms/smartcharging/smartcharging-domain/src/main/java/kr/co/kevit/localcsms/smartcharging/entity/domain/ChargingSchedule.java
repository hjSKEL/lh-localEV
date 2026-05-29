package kr.co.kevit.localcsms.smartcharging.entity.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TB_CHPF002 — 충전 스케줄 (OCPP 2.1 ChargingScheduleType 정규화). 프로파일 1:N.
 *
 * <p>limitAtSoc/salesTariff/priceSchedule 등 복합 객체는 JSON 컬럼으로 보존,
 * period 는 {@link ChargingSchedulePeriod} 로 정규화.</p>
 */
public class ChargingSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    private int profileId;
    private int scheduleSeq;

    private Integer scheduleId;
    private Date startSchedule;
    private Integer duration;
    private String rateUnit;
    private Double minChargingRate;
    private Double powerTolerance;
    private Boolean useLocalTime;
    private Integer randomizedDelay;
    private String limitAtSocJson;
    private String salesTariffJson;
    private String absolutePriceScheduleJson;
    private String priceLevelScheduleJson;

    private List<ChargingSchedulePeriod> periods = new ArrayList<>();

    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }

    public int getScheduleSeq() { return scheduleSeq; }
    public void setScheduleSeq(int scheduleSeq) { this.scheduleSeq = scheduleSeq; }

    public Integer getScheduleId() { return scheduleId; }
    public void setScheduleId(Integer scheduleId) { this.scheduleId = scheduleId; }

    public Date getStartSchedule() { return startSchedule; }
    public void setStartSchedule(Date startSchedule) { this.startSchedule = startSchedule; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public String getRateUnit() { return rateUnit; }
    public void setRateUnit(String rateUnit) { this.rateUnit = rateUnit; }

    public Double getMinChargingRate() { return minChargingRate; }
    public void setMinChargingRate(Double minChargingRate) { this.minChargingRate = minChargingRate; }

    public Double getPowerTolerance() { return powerTolerance; }
    public void setPowerTolerance(Double powerTolerance) { this.powerTolerance = powerTolerance; }

    public Boolean getUseLocalTime() { return useLocalTime; }
    public void setUseLocalTime(Boolean useLocalTime) { this.useLocalTime = useLocalTime; }

    public Integer getRandomizedDelay() { return randomizedDelay; }
    public void setRandomizedDelay(Integer randomizedDelay) { this.randomizedDelay = randomizedDelay; }

    public String getLimitAtSocJson() { return limitAtSocJson; }
    public void setLimitAtSocJson(String limitAtSocJson) { this.limitAtSocJson = limitAtSocJson; }

    public String getSalesTariffJson() { return salesTariffJson; }
    public void setSalesTariffJson(String salesTariffJson) { this.salesTariffJson = salesTariffJson; }

    public String getAbsolutePriceScheduleJson() { return absolutePriceScheduleJson; }
    public void setAbsolutePriceScheduleJson(String absolutePriceScheduleJson) { this.absolutePriceScheduleJson = absolutePriceScheduleJson; }

    public String getPriceLevelScheduleJson() { return priceLevelScheduleJson; }
    public void setPriceLevelScheduleJson(String priceLevelScheduleJson) { this.priceLevelScheduleJson = priceLevelScheduleJson; }

    public List<ChargingSchedulePeriod> getPeriods() { return periods; }
    public void setPeriods(List<ChargingSchedulePeriod> periods) { this.periods = periods; }
}
