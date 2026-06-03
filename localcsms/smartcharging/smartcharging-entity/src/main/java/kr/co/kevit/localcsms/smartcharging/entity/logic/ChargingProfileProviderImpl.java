package kr.co.kevit.localcsms.smartcharging.entity.logic;

import kr.co.kevit.localcsms.smartcharging.entity.ChargingProfileProvider;
import kr.co.kevit.localcsms.smartcharging.entity.dao.ChargingProfileMapper;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingNeedsSnapshot;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedulePeriod;
import kr.co.kevit.localcsms.smartcharging.entity.domain.CsConfig;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ExternalChargingLimit;
import kr.co.kevit.localcsms.smartcharging.entity.domain.NegotiationState;
import kr.co.kevit.localcsms.smartcharging.entity.shared.ChargingProfileSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TB_CHPF001/002/003 (정규화) + TB_CHEN001 - 충전 프로파일 Provider 구현.
 */
@Service
public class ChargingProfileProviderImpl implements ChargingProfileProvider {

    private final ChargingProfileMapper mapper;

    public ChargingProfileProviderImpl(ChargingProfileMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void saveProfile(ChargingProfile profile) {
        mapper.insertProfile(profile);
        insertSchedulesAndPeriods(profile);
    }

    @Override
    public void updateProfile(ChargingProfile profile) {
        mapper.updateProfile(profile);
        // 기간 → 스케줄 순으로 삭제 후 재구성 (FK 안전)
        mapper.deletePeriodsByProfile(profile.getProfileId());
        mapper.deleteSchedulesByProfile(profile.getProfileId());
        insertSchedulesAndPeriods(profile);
    }

    /** 스케줄/기간 순번(1..N) 부여 후 정규화 저장 */
    private void insertSchedulesAndPeriods(ChargingProfile profile) {
        if (profile.getSchedules() == null) {
            return;
        }
        int scheduleSeq = 1;
        for (ChargingSchedule schedule : profile.getSchedules()) {
            schedule.setProfileId(profile.getProfileId());
            schedule.setScheduleSeq(scheduleSeq);
            mapper.insertSchedule(schedule);
            int periodSeq = 1;
            if (schedule.getPeriods() != null) {
                for (ChargingSchedulePeriod period : schedule.getPeriods()) {
                    period.setProfileId(profile.getProfileId());
                    period.setScheduleSeq(scheduleSeq);
                    period.setPeriodSeq(periodSeq);
                    mapper.insertPeriod(period);
                    periodSeq++;
                }
            }
            scheduleSeq++;
        }
    }

    @Override
    public ChargingProfile findOne(int profileId, String cpId, String csId) {
        return mapper.findOne(profileId, cpId, csId);
    }

    @Override
    public Page<ChargingProfile> findBySearchCond(ChargingProfileSearchCond cond) {
        int total = mapper.countBySearchCond(cond);
        cond.setTotalItemCount(total);
        List<ChargingProfile> list = total > 0 ? mapper.selectBySearchCond(cond) : new ArrayList<>();
        return new Page<>(cond, list);
    }

    @Override
    public List<ChargingProfile> findByTransactionId(String transactionId) {
        return mapper.selectByTransactionId(transactionId);
    }

    @Override
    public List<ChargingProfile> findActiveByEvse(String cpId, String csId, int evseId, String purpose) {
        return mapper.selectActiveByEvse(cpId, csId, evseId, purpose);
    }

    @Override
    public List<ChargingProfile> findDueDynamicProfiles() {
        return mapper.selectDueDynamicProfiles();
    }

    @Override
    public void touchDynUpdateTime(int profileId) {
        mapper.updateDynUpdateTime(profileId);
    }

    @Override
    public List<ChargingProfile> findAutoPushTargets() {
        return mapper.selectAutoPushTargets();
    }

    @Override
    public void deleteProfile(int profileId, String cpId, String csId) {
        mapper.deletePeriodsByProfile(profileId);
        mapper.deleteSchedulesByProfile(profileId);
        mapper.deleteProfile(profileId, cpId, csId);
    }

    @Override
    public void saveNeeds(ChargingNeedsSnapshot needs) {
        mapper.insertNeeds(needs);
    }

    @Override
    public ChargingNeedsSnapshot findLatestNeeds(String cpId, String csId, int evseId) {
        return mapper.selectLatestNeeds(cpId, csId, evseId);
    }

    @Override
    public void saveNegotiation(NegotiationState state) {
        mapper.upsertNegotiation(state);
    }

    @Override
    public NegotiationState findNegotiation(String cpId, String csId, int evseId) {
        return mapper.selectNegotiation(cpId, csId, evseId);
    }

    @Override
    public void saveExternalLimit(ExternalChargingLimit limit) {
        mapper.insertExternalLimit(limit);
    }

    @Override
    public void deactivateExternalLimit(String cpId, String csId, Integer evseId, String source) {
        mapper.deactivateExternalLimit(cpId, csId, evseId, source);
    }

    @Override
    public List<ExternalChargingLimit> findActiveExternalLimits(String cpId, String csId, int evseId) {
        return mapper.selectActiveExternalLimits(cpId, csId, evseId);
    }

    @Override
    public void saveCsConfig(CsConfig config) {
        mapper.upsertCsConfig(config);
    }

    @Override
    public CsConfig findCsConfig(String cpId, String csId) {
        return mapper.selectCsConfig(cpId, csId);
    }
}
