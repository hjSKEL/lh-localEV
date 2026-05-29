package kr.co.kevit.localcsms.smartcharging.process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedulePeriod;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfileKind;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;
import kr.co.kevit.ocpp201.domain.ChargingSchedulePeriodType;
import kr.co.kevit.ocpp201.domain.CompositeScheduleType;
import kr.co.kevit.ocpp201.enumtype.ChargingRateUnitEnumType;

/**
 * 복수 ChargingProfile → 합성 스케줄 계산 (OCPP 2.1 K08 / GetCompositeSchedule).
 *
 * <p>우선순위(상한 min): ChargingStationExternalConstraints &gt; ChargingStationMaxProfile &gt;
 * TxProfile(우선) / TxDefaultProfile. purpose 내 stackLevel 최댓값 1개 선택. 각 시점 합성 limit =
 * 적용 레이어 limit 의 최솟값(가장 제한적). limit_L2/L3 도 동일 산출.</p>
 *
 * <p>고도화: Recurring(Daily/Weekly) 전개, 요청과 다른 rateUnit(W↔A) 의 근사 환산(공칭 전압·상수 기반).</p>
 * <p>제한: setpoint/dischargeLimit/V2X 곡선 합성은 미지원(상한 limit 중심).</p>
 */
@Component
public class CompositeScheduleCalculator {

    private static final int DAILY_SEC = 86400;
    private static final int WEEKLY_SEC = 604800;

    /** W↔A 근사 환산용 공칭 상전압(V). 운영 환경에 맞게 설정. */
    @Value("${smartcharging.nominalVoltage:230.0}")
    private double nominalVoltage;

    /** numberPhases 미지정 시 기본 상 수 */
    @Value("${smartcharging.defaultPhases:3}")
    private int defaultPhases;

    public CompositeScheduleType calculate(List<ChargingProfile> profiles, int evseId,
                                           int durationSec, String rateUnit, Date scheduleStart) {
        Date start = scheduleStart != null ? scheduleStart : new Date();

        List<ChargingProfile> applicable = new ArrayList<>();
        for (ChargingProfile p : profiles) {
            if (p.getEvseId() != 0 && p.getEvseId() != evseId) continue;
            if (!isValidAt(p, start)) continue;
            if (primarySchedule(p) != null) applicable.add(p);
        }

        List<ChargingProfile> layers = selectLimitLayers(applicable);

        TreeSet<Integer> boundaries = new TreeSet<>();
        boundaries.add(0);
        for (ChargingProfile layer : layers) {
            ChargingSchedule s = primarySchedule(layer);
            int span = effectiveSpan(layer, s, durationSec);
            for (ChargingSchedulePeriod period : s.getPeriods()) {
                addRecurringBoundaries(boundaries, layer, period.getStartPeriod(), span, durationSec);
            }
        }

        List<ChargingSchedulePeriodType> outPeriods = new ArrayList<>();
        Double prevLimit = null, prevL2 = null, prevL3 = null;
        for (Integer t : boundaries) {
            Double limit = compositeAt(layers, rateUnit, t, ChargingSchedulePeriod::getLimit);
            if (limit == null) continue;
            Double l2 = compositeAt(layers, rateUnit, t, ChargingSchedulePeriod::getLimitL2);
            Double l3 = compositeAt(layers, rateUnit, t, ChargingSchedulePeriod::getLimitL3);
            if (!eq(limit, prevLimit) || !eq(l2, prevL2) || !eq(l3, prevL3)) {
                ChargingSchedulePeriodType pt = new ChargingSchedulePeriodType();
                pt.setStartPeriod(t);
                pt.setLimit((int) Math.round(limit));
                if (l2 != null) pt.setLimit_L2((int) Math.round(l2));
                if (l3 != null) pt.setLimit_L3((int) Math.round(l3));
                outPeriods.add(pt);
                prevLimit = limit; prevL2 = l2; prevL3 = l3;
            }
        }

        CompositeScheduleType result = new CompositeScheduleType();
        result.setEvseId(evseId);
        result.setDuration(durationSec);
        result.setChargingRateUnit(toRateUnit(rateUnit));
        result.setChargingSchedulePeriod(outPeriods);
        return result;
    }

    private List<ChargingProfile> selectLimitLayers(List<ChargingProfile> applicable) {
        ChargingProfile ext = highestStack(applicable, ChargingProfilePurpose.ChargingStationExternalConstraints);
        ChargingProfile max = highestStack(applicable, ChargingProfilePurpose.ChargingStationMaxProfile);
        ChargingProfile tx = highestStack(applicable, ChargingProfilePurpose.TxProfile);
        ChargingProfile txDef = highestStack(applicable, ChargingProfilePurpose.TxDefaultProfile);

        List<ChargingProfile> layers = new ArrayList<>();
        if (ext != null) layers.add(ext);
        if (max != null) layers.add(max);
        if (tx != null) layers.add(tx);
        else if (txDef != null) layers.add(txDef);
        return layers;
    }

    private ChargingProfile highestStack(List<ChargingProfile> list, ChargingProfilePurpose purpose) {
        return list.stream()
                .filter(p -> purpose == p.getPurpose())
                .max(Comparator.comparingInt(ChargingProfile::getStackLevel))
                .orElse(null);
    }

    /** 각 레이어의 시점 t 선택필드 값(요청 단위 환산) 중 최솟값. 정의된 레이어 없으면 null */
    private Double compositeAt(List<ChargingProfile> layers, String rateUnit, int t,
                               Function<ChargingSchedulePeriod, Double> selector) {
        Double min = null;
        for (ChargingProfile layer : layers) {
            Double v = valueAt(layer, rateUnit, t, selector);
            if (v == null) continue;
            if (min == null || v < min) min = v;
        }
        return min;
    }

    /** 첫 스케줄에서 t 를 포함하는 period 의 선택필드 값(요청 단위 환산). Recurring 전개 적용. */
    private Double valueAt(ChargingProfile p, String rateUnit, int t,
                           Function<ChargingSchedulePeriod, Double> selector) {
        ChargingSchedule s = primarySchedule(p);
        if (s == null) return null;
        int effectiveT = mapRecurring(p, s, t);
        if (effectiveT < 0) return null;

        ChargingSchedulePeriod hit = null;
        for (ChargingSchedulePeriod period : s.getPeriods()) {
            if (period.getStartPeriod() <= effectiveT) hit = period;
            else break;
        }
        if (hit == null) return null;
        Double raw = selector.apply(hit);
        if (raw == null) return null;
        return convert(raw, s.getRateUnit(), rateUnit, hit.getNumberPhases());
    }

    /** Recurring 이면 t 를 반복 구간 내 오프셋으로 매핑, 비반복이면 duration 초과 시 -1 */
    private int mapRecurring(ChargingProfile p, ChargingSchedule s, int t) {
        if (ChargingProfileKind.Recurring == p.getKind()) {
            int len = "W".equalsIgnoreCase(p.getRecurrencyKind()) ? WEEKLY_SEC : DAILY_SEC;
            return t % len;
        }
        if (s.getDuration() != null && t >= s.getDuration()) return -1;
        return t;
    }

    /** Recurring 전개를 반영해 경계 후보를 [0,duration) 범위로 추가 */
    private void addRecurringBoundaries(TreeSet<Integer> boundaries, ChargingProfile p,
                                        int startPeriod, int span, int durationSec) {
        if (ChargingProfileKind.Recurring == p.getKind()) {
            int len = "W".equalsIgnoreCase(p.getRecurrencyKind()) ? WEEKLY_SEC : DAILY_SEC;
            for (int base = 0; base < durationSec; base += len) {
                int b = base + startPeriod;
                if (b < durationSec) boundaries.add(b);
            }
        } else if (startPeriod < span && startPeriod < durationSec) {
            boundaries.add(startPeriod);
        }
    }

    private int effectiveSpan(ChargingProfile p, ChargingSchedule s, int durationSec) {
        if (ChargingProfileKind.Recurring == p.getKind()) return durationSec;
        return s.getDuration() != null ? Math.min(s.getDuration(), durationSec) : durationSec;
    }

    /** W↔A 근사 환산. 같은 단위면 그대로. */
    private Double convert(double value, String fromUnit, String toUnit, Integer numberPhases) {
        if (fromUnit == null || toUnit == null || fromUnit.equalsIgnoreCase(toUnit)) {
            return value;
        }
        int phases = numberPhases != null ? numberPhases : defaultPhases;
        double factor = nominalVoltage * phases;
        if (factor <= 0) return value;
        if ("W".equalsIgnoreCase(fromUnit) && "A".equalsIgnoreCase(toUnit)) {
            return value / factor;              // W → A
        }
        if ("A".equalsIgnoreCase(fromUnit) && "W".equalsIgnoreCase(toUnit)) {
            return value * factor;              // A → W
        }
        return value;
    }

    private ChargingSchedule primarySchedule(ChargingProfile p) {
        if (p.getSchedules() == null) return null;
        for (ChargingSchedule s : p.getSchedules()) {
            if (s.getPeriods() != null && !s.getPeriods().isEmpty()) return s;
        }
        return null;
    }

    private boolean isValidAt(ChargingProfile p, Date at) {
        if (p.getValidFrom() != null && at.before(p.getValidFrom())) return false;
        if (p.getValidTo() != null && at.after(p.getValidTo())) return false;
        return true;
    }

    private boolean eq(Double a, Double b) {
        if (a == null) return b == null;
        return b != null && a.doubleValue() == b.doubleValue();
    }

    private ChargingRateUnitEnumType toRateUnit(String unit) {
        if (unit == null) return null;
        try { return ChargingRateUnitEnumType.valueOf(unit); }
        catch (IllegalArgumentException e) { return null; }
    }
}
