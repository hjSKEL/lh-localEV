package kr.co.kevit.localcsms.smartcharging.process.converter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedulePeriod;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfileKind;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;
import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.domain.ChargingScheduleType;
import kr.co.kevit.ocpp201.domain.ChargingSchedulePeriodType;
import kr.co.kevit.ocpp201.enumtype.ChargingProfileKindEnumType;
import kr.co.kevit.ocpp201.enumtype.ChargingProfilePurposeEnumType;
import kr.co.kevit.ocpp201.enumtype.ChargingRateUnitEnumType;
import kr.co.kevit.ocpp201.enumtype.OperationModeEnumType;
import kr.co.kevit.ocpp201.enumtype.RecurrencyKindEnumType;

/**
 * OCPP 2.1 {@link ChargingProfileType} ↔ localcsms 정규화 엔티티({@link ChargingProfile} 트리) 변환.
 *
 * <p>구조 필드(purpose/kind/schedule/period/2.1 boolean·numeric)는 정규 매핑,
 * 복합 객체(salesTariff/limitAtSoC/priceSchedule/v2x curve)는 JSON 컬럼으로 보존.</p>
 */
@Component
public class ChargingProfileConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargingProfileConverter.class);

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static final String ISO = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    // =====================================================================
    // OCPP → 엔티티
    // =====================================================================

    public ChargingProfile toEntity(ChargingProfileType cp, String cpId, String csId, int evseId) {
        ChargingProfile p = new ChargingProfile();
        p.setProfileId(cp.getId());
        p.setCpId(cpId);
        p.setCsId(csId);
        p.setEvseId(evseId);
        p.setStackLevel(cp.getStackLevel());
        p.setRechargingId(cp.getTransactionId());
        p.setValidFrom(parseDate(cp.getValidFrom()));
        p.setValidTo(parseDate(cp.getValidTo()));

        if (cp.getChargingProfilePurpose() != null) {
            try {
                p.setPurpose(ChargingProfilePurpose.valueOf(cp.getChargingProfilePurpose().name()));
            } catch (IllegalArgumentException e) {
                LOGGER.warn("unknown purpose: {}", cp.getChargingProfilePurpose());
            }
        }
        if (cp.getChargingProfileKind() != null) {
            try {
                p.setKind(ChargingProfileKind.valueOf(cp.getChargingProfileKind().name()));
            } catch (IllegalArgumentException e) {
                LOGGER.warn("unknown kind: {}", cp.getChargingProfileKind());
            }
        }
        if (cp.getRecurrencyKind() != null) {
            p.setRecurrencyKind(RecurrencyKindEnumType.Daily == cp.getRecurrencyKind() ? "D" : "W");
        }

        // 2.1 필드
        p.setMaxOfflineDuration(cp.getMaxOfflineDuration());
        p.setInvalidAfterOfflineDuration(cp.isInvalidAfterOfflineDuration());
        p.setDynUpdateInterval(cp.getDynUpdateInterval());
        p.setDynUpdateTime(parseDate(cp.getDynUpdateTime()));
        p.setPriceScheduleSignature(cp.getPriceScheduleSignature());

        // 스케줄/기간
        List<ChargingSchedule> schedules = new ArrayList<>();
        if (cp.getChargingSchedule() != null) {
            for (ChargingScheduleType s : cp.getChargingSchedule()) {
                schedules.add(toScheduleEntity(s));
            }
        }
        p.setSchedules(schedules);
        return p;
    }

    private ChargingSchedule toScheduleEntity(ChargingScheduleType s) {
        ChargingSchedule e = new ChargingSchedule();
        e.setScheduleId(s.getId());
        e.setStartSchedule(parseDate(s.getStartSchedule()));
        e.setDuration(s.getDuration());
        e.setRateUnit(s.getChargingRateUnit() == null ? null : s.getChargingRateUnit().name());
        e.setMinChargingRate(toDouble(s.getMinChargingRate()));
        e.setPowerTolerance(toDouble(s.getPowerTolerance()));
        e.setUseLocalTime(s.getUseLocalTime());
        e.setRandomizedDelay(s.getRandomizedDelay());
        e.setLimitAtSocJson(toJson(s.getLimitAtSoC()));
        e.setSalesTariffJson(toJson(s.getSalesTariff()));
        e.setAbsolutePriceScheduleJson(toJson(s.getAbsolutePriceSchedule()));
        e.setPriceLevelScheduleJson(toJson(s.getPriceLevelSchedule()));

        List<ChargingSchedulePeriod> periods = new ArrayList<>();
        if (s.getChargingSchedulePeriod() != null) {
            for (ChargingSchedulePeriodType pp : s.getChargingSchedulePeriod()) {
                periods.add(toPeriodEntity(pp));
            }
        }
        e.setPeriods(periods);
        return e;
    }

    private ChargingSchedulePeriod toPeriodEntity(ChargingSchedulePeriodType pp) {
        ChargingSchedulePeriod e = new ChargingSchedulePeriod();
        e.setStartPeriod(pp.getStartPeriod() == null ? 0 : pp.getStartPeriod());
        e.setLimit(toDouble(pp.getLimit()));
        e.setLimitL2(toDouble(pp.getLimit_L2()));
        e.setLimitL3(toDouble(pp.getLimit_L3()));
        e.setNumberPhases(pp.getNumberPhases());
        e.setPhaseToUse(pp.getPhaseToUse());
        e.setDischargeLimit(toDouble(pp.getDischargeLimit()));
        e.setDischargeLimitL2(toDouble(pp.getDischargeLimit_L2()));
        e.setDischargeLimitL3(toDouble(pp.getDischargeLimit_L3()));
        e.setSetpoint(toDouble(pp.getSetpoint()));
        e.setSetpointL2(toDouble(pp.getSetpoint_L2()));
        e.setSetpointL3(toDouble(pp.getSetpoint_L3()));
        e.setSetpointReactive(toDouble(pp.getSetpointReactive()));
        e.setSetpointReactiveL2(toDouble(pp.getSetpointReactive_L2()));
        e.setSetpointReactiveL3(toDouble(pp.getSetpointReactive_L3()));
        e.setOperationMode(pp.getOperationMode() == null ? null : pp.getOperationMode().name());
        e.setEvseSleep(pp.getEvseSleep());
        e.setV2xBaseline(pp.getV2xBaseline());
        e.setPreconditioningRequest(pp.getPreconditioningRequest());
        e.setV2xFreqWattJson(toJson(pp.getV2xFreqWattCurve()));
        e.setV2xSignalWattJson(toJson(pp.getV2xSignalWattCurve()));
        return e;
    }

    // =====================================================================
    // 엔티티 → OCPP
    // =====================================================================

    public ChargingProfileType toOcpp(ChargingProfile p) {
        ChargingProfileType cp = new ChargingProfileType();
        cp.setId(p.getProfileId());
        cp.setStackLevel(p.getStackLevel());
        cp.setTransactionId(p.getRechargingId());
        cp.setValidFrom(formatDate(p.getValidFrom()));
        cp.setValidTo(formatDate(p.getValidTo()));
        if (p.getPurpose() != null) {
            try {
                cp.setChargingProfilePurpose(ChargingProfilePurposeEnumType.valueOf(p.getPurpose().name()));
            } catch (IllegalArgumentException ignore) { /* 코드 불일치 시 생략 */ }
        }
        if (p.getKind() != null) {
            try {
                cp.setChargingProfileKind(ChargingProfileKindEnumType.valueOf(p.getKind().name()));
            } catch (IllegalArgumentException ignore) { /* 생략 */ }
        }
        if (p.getRecurrencyKind() != null) {
            cp.setRecurrencyKind("D".equals(p.getRecurrencyKind())
                    ? RecurrencyKindEnumType.Daily : RecurrencyKindEnumType.Weekly);
        }
        cp.setMaxOfflineDuration(p.getMaxOfflineDuration());
        if (p.getInvalidAfterOfflineDuration() != null) {
            cp.setInvalidAfterOfflineDuration(p.getInvalidAfterOfflineDuration());
        }
        cp.setDynUpdateInterval(p.getDynUpdateInterval());
        cp.setDynUpdateTime(formatDate(p.getDynUpdateTime()));
        cp.setPriceScheduleSignature(p.getPriceScheduleSignature());

        List<ChargingScheduleType> schedules = new ArrayList<>();
        if (p.getSchedules() != null) {
            for (ChargingSchedule s : p.getSchedules()) {
                schedules.add(toScheduleOcpp(s));
            }
        }
        cp.setChargingSchedule(schedules);
        return cp;
    }

    private ChargingScheduleType toScheduleOcpp(ChargingSchedule s) {
        ChargingScheduleType o = new ChargingScheduleType();
        o.setId(s.getScheduleId());
        o.setStartSchedule(formatDate(s.getStartSchedule()));
        o.setDuration(s.getDuration());
        if (s.getRateUnit() != null) {
            try { o.setChargingRateUnit(ChargingRateUnitEnumType.valueOf(s.getRateUnit())); }
            catch (IllegalArgumentException ignore) { /* 생략 */ }
        }
        o.setMinChargingRate(toInt(s.getMinChargingRate()));
        o.setPowerTolerance(toInt(s.getPowerTolerance()));
        o.setUseLocalTime(s.getUseLocalTime());
        o.setRandomizedDelay(s.getRandomizedDelay());
        o.setLimitAtSoC(fromJson(s.getLimitAtSocJson(),
                kr.co.kevit.ocpp201.domain.LimitAtSoCType.class));
        o.setSalesTariff(fromJson(s.getSalesTariffJson(),
                kr.co.kevit.ocpp201.domain.SalesTariffType.class));
        o.setAbsolutePriceSchedule(fromJson(s.getAbsolutePriceScheduleJson(),
                kr.co.kevit.ocpp201.domain.AbsolutePriceScheduleType.class));
        o.setPriceLevelSchedule(fromJson(s.getPriceLevelScheduleJson(),
                kr.co.kevit.ocpp201.domain.PriceLevelScheduleType.class));

        List<ChargingSchedulePeriodType> periods = new ArrayList<>();
        if (s.getPeriods() != null) {
            for (ChargingSchedulePeriod pp : s.getPeriods()) {
                periods.add(toPeriodOcpp(pp));
            }
        }
        o.setChargingSchedulePeriod(periods);
        return o;
    }

    private ChargingSchedulePeriodType toPeriodOcpp(ChargingSchedulePeriod e) {
        ChargingSchedulePeriodType o = new ChargingSchedulePeriodType();
        o.setStartPeriod(e.getStartPeriod());
        o.setLimit(toInt(e.getLimit()));
        o.setLimit_L2(toInt(e.getLimitL2()));
        o.setLimit_L3(toInt(e.getLimitL3()));
        o.setNumberPhases(e.getNumberPhases());
        o.setPhaseToUse(e.getPhaseToUse());
        o.setDischargeLimit(toInt(e.getDischargeLimit()));
        o.setDischargeLimit_L2(toInt(e.getDischargeLimitL2()));
        o.setDischargeLimit_L3(toInt(e.getDischargeLimitL3()));
        o.setSetpoint(toInt(e.getSetpoint()));
        o.setSetpoint_L2(toInt(e.getSetpointL2()));
        o.setSetpoint_L3(toInt(e.getSetpointL3()));
        o.setSetpointReactive(toInt(e.getSetpointReactive()));
        o.setSetpointReactive_L2(toInt(e.getSetpointReactiveL2()));
        o.setSetpointReactive_L3(toInt(e.getSetpointReactiveL3()));
        if (e.getOperationMode() != null) {
            try { o.setOperationMode(OperationModeEnumType.valueOf(e.getOperationMode())); }
            catch (IllegalArgumentException ignore) { /* 생략 */ }
        }
        o.setEvseSleep(e.getEvseSleep());
        o.setV2xBaseline(e.getV2xBaseline());
        o.setPreconditioningRequest(e.getPreconditioningRequest());
        return o;
    }

    // =====================================================================
    // schedules ↔ OCPP-format JSON (admin CRUD / 1.6 호환)
    // =====================================================================

    /** OCPP ChargingScheduleType[] JSON → 엔티티 스케줄 목록 */
    public List<ChargingSchedule> schedulesFromOcppJson(String json) {
        List<ChargingSchedule> result = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) {
            return result;
        }
        try {
            ChargingScheduleType[] arr = objectMapper.readValue(json, ChargingScheduleType[].class);
            for (ChargingScheduleType s : arr) {
                result.add(toScheduleEntity(s));
            }
        } catch (Exception e) {
            LOGGER.warn("schedulesFromOcppJson 실패: {}", e.getMessage());
        }
        return result;
    }

    /** 엔티티 스케줄 목록 → OCPP ChargingScheduleType[] JSON */
    public String schedulesToOcppJson(List<ChargingSchedule> schedules) {
        if (schedules == null || schedules.isEmpty()) {
            return null;
        }
        List<ChargingScheduleType> arr = new ArrayList<>();
        for (ChargingSchedule s : schedules) {
            arr.add(toScheduleOcpp(s));
        }
        try {
            return objectMapper.writeValueAsString(arr);
        } catch (Exception e) {
            LOGGER.warn("schedulesToOcppJson 실패: {}", e.getMessage());
            return null;
        }
    }

    // =====================================================================
    // helpers
    // =====================================================================

    private Double toDouble(Integer v) { return v == null ? null : v.doubleValue(); }

    private Integer toInt(Double v) { return v == null ? null : (int) Math.round(v); }

    private String toJson(Object o) {
        if (o == null) return null;
        try { return objectMapper.writeValueAsString(o); }
        catch (Exception e) { LOGGER.warn("toJson 실패: {}", e.getMessage()); return null; }
    }

    private <T> T fromJson(String json, Class<T> type) {
        if (json == null || json.trim().isEmpty()) return null;
        try { return objectMapper.readValue(json, type); }
        catch (Exception e) { LOGGER.warn("fromJson 실패({}): {}", type.getSimpleName(), e.getMessage()); return null; }
    }

    private Date parseDate(String iso) {
        if (iso == null || iso.isEmpty()) return null;
        try {
            return new SimpleDateFormat(ISO).parse(iso);
        } catch (Exception e) {
            try { return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(iso); }
            catch (Exception ex) { LOGGER.warn("날짜 파싱 실패: {}", iso); return null; }
        }
    }

    private String formatDate(Date d) {
        if (d == null) return null;
        SimpleDateFormat f = new SimpleDateFormat(ISO);
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        return f.format(d);
    }
}
