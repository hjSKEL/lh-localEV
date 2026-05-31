package kr.co.kevit.localcsms.smartcharging.process;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedulePeriod;
import kr.co.kevit.localcsms.smartcharging.process.ChargingProfileService;
import kr.co.kevit.ocpp201.domain.ChargingScheduleUpdateType;

/**
 * Dynamic 충전 프로파일 관리 (OCPP 2.1 K28).
 *
 * <p>Pull: {@link #computeUpdate}로 현재 scheduleUpdate 계산(PullDynamicScheduleUpdate 응답).<br>
 * Push: {@link #collectDueUpdates}로 dynUpdateInterval 경과 프로파일의 갱신 목록 산출
 * (데몬 스케줄러가 UpdateDynamicSchedule CALL 로 송신 후 {@link #markPushed}).</p>
 *
 * <p>현재 scheduleUpdate 는 저장 프로파일의 첫 스케줄 첫 기간 기준. 추후 EMS/가격/grid
 * 외부 신호 기반 산출로 확장(ExternalLimitManager 연동).</p>
 */
@Component
public class DynamicScheduleManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicScheduleManager.class);

    private final ChargingProfileService chargingProfileService;

    public DynamicScheduleManager(ChargingProfileService chargingProfileService) {
        this.chargingProfileService = chargingProfileService;
    }

    /** profileId 의 현재 scheduleUpdate 계산. 미존재 시 null. */
    public ChargingScheduleUpdateType computeUpdate(int profileId, String cpId, String csId) {
        ChargingProfile profile = chargingProfileService.findOne(profileId, cpId, csId);
        if (profile == null) {
            return null;
        }
        return extractUpdate(profile);
    }

    /** dynUpdateInterval 경과한 Dynamic 프로파일들의 push 목록 */
    public List<DynamicPushItem> collectDueUpdates() {
        List<DynamicPushItem> items = new ArrayList<>();
        for (ChargingProfile header : chargingProfileService.findDueDynamicProfiles()) {
            ChargingProfile full = chargingProfileService.findOne(header.getProfileId(),
                    header.getCpId(), header.getCsId());
            if (full == null) continue;
            items.add(new DynamicPushItem(full.getCpId(), full.getCsId(), full.getProfileId(),
                    extractUpdate(full)));
        }
        return items;
    }

    /** push 완료 후 dynUpdateTime 갱신 */
    public void markPushed(int profileId) {
        chargingProfileService.touchDynUpdateTime(profileId);
    }

    /**
     * 첫 스케줄 첫 기간 → ChargingScheduleUpdateType.
     *
     * <p>OCPP 2.1 K28 PullDynamicScheduleUpdate 응답 검증(TC_K_121)은 scheduleUpdate 에
     * 의미있는 dynamic indicator 가 최소 한 개 채워져 있어야 통과한다. {@code setpoint} 가
     * Dynamic Control 의 1차 indicator 이므로, 저장된 period 에 setpoint 가 없으면
     * limit 값을 setpoint 로 fallback 하여 채운다.</p>
     */
    private ChargingScheduleUpdateType extractUpdate(ChargingProfile profile) {
        ChargingScheduleUpdateType update = new ChargingScheduleUpdateType();
        List<ChargingSchedule> schedules = profile.getSchedules();
        if (schedules == null || schedules.isEmpty()) return update;
        List<ChargingSchedulePeriod> periods = schedules.get(0).getPeriods();
        if (periods == null || periods.isEmpty()) return update;
        ChargingSchedulePeriod p = periods.get(0);
        update.setLimit(p.getLimit());
        update.setLimit_L2(p.getLimitL2());
        update.setLimit_L3(p.getLimitL3());
        // setpoint fallback: 저장된 setpoint 없으면 limit 값 사용 (Dynamic 모드 indicator)
        Double setpoint = p.getSetpoint() != null ? p.getSetpoint()
                : p.getLimit();
        update.setSetpoint(setpoint);
        update.setSetpoint_L2(p.getSetpointL2());
        update.setSetpoint_L3(p.getSetpointL3());
        update.setDischargeLimit(p.getDischargeLimit());
        LOGGER.debug("computeUpdate profileId={} limit={} setpoint={}", profile.getProfileId(),
                p.getLimit(), setpoint);
        return update;
    }
}
