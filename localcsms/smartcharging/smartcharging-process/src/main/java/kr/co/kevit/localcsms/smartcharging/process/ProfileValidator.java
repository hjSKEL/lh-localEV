package kr.co.kevit.localcsms.smartcharging.process;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedulePeriod;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;

/**
 * OCPP 2.1 ChargingProfile 구조/목적별 검증 (K01).
 *
 * <p>SetChargingProfile 수락 전 CSMS 측 사전 검증. 실패 시 {@link SmartChargingException}.</p>
 */
@Component
public class ProfileValidator {

    public void validate(ChargingProfile p) {
        if (p == null) {
            throw new SmartChargingException("chargingProfile is null");
        }
        if (p.getPurpose() == null) {
            throw new SmartChargingException("chargingProfilePurpose 누락");
        }
        if (p.getStackLevel() < 0) {
            throw new SmartChargingException("stackLevel 은 0 이상이어야 함");
        }
        if (p.getSchedules() == null || p.getSchedules().isEmpty()) {
            throw new SmartChargingException("chargingSchedule 1개 이상 필요");
        }

        for (ChargingSchedule s : p.getSchedules()) {
            validateSchedule(s);
        }

        validatePurposeRules(p);
    }

    private void validateSchedule(ChargingSchedule s) {
        if (!StringUtils.hasText(s.getRateUnit())) {
            throw new SmartChargingException("chargingRateUnit 누락");
        }
        if (s.getPeriods() == null || s.getPeriods().isEmpty()) {
            throw new SmartChargingException("chargingSchedulePeriod 1개 이상 필요");
        }
        // 첫 기간은 startPeriod=0 (OCPP 규칙)
        ChargingSchedulePeriod first = s.getPeriods().get(0);
        if (first.getStartPeriod() != 0) {
            throw new SmartChargingException("첫 chargingSchedulePeriod.startPeriod 는 0 이어야 함");
        }
        // startPeriod 단조 증가
        int prev = -1;
        for (ChargingSchedulePeriod period : s.getPeriods()) {
            if (period.getStartPeriod() <= prev) {
                throw new SmartChargingException("chargingSchedulePeriod.startPeriod 는 오름차순이어야 함");
            }
            prev = period.getStartPeriod();
        }
    }

    private void validatePurposeRules(ChargingProfile p) {
        ChargingProfilePurpose purpose = p.getPurpose();
        switch (purpose) {
            case TxProfile:
                if (!StringUtils.hasText(p.getRechargingId())) {
                    throw new SmartChargingException("TxProfile 은 transactionId 가 필요함");
                }
                break;
            case ChargingStationMaxProfile:
                // 충전소(전체 EVSE) 단위 — evseId=0 이어야 함
                if (p.getEvseId() != 0) {
                    throw new SmartChargingException("ChargingStationMaxProfile 은 evseId=0(충전소 전체) 이어야 함");
                }
                break;
            case ChargingStationExternalConstraints:
                // EMS 등 외부 제약 — 충전소 단위
                if (p.getEvseId() != 0) {
                    throw new SmartChargingException("ChargingStationExternalConstraints 는 evseId=0 이어야 함");
                }
                break;
            default:
                // TxDefaultProfile / PriorityCharging / LocalGeneration — 추가 제약 없음
                break;
        }
    }
}
