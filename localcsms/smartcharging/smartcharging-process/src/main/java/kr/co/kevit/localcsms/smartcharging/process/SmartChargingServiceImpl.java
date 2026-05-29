package kr.co.kevit.localcsms.smartcharging.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.CsConfig;
import kr.co.kevit.localcsms.smartcharging.process.ChargingProfileService;
import kr.co.kevit.localcsms.smartcharging.process.converter.ChargingProfileConverter;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;
import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.domain.CompositeScheduleType;

/**
 * {@link SmartChargingService} 구현 — ProfileValidator·CompositeScheduleCalculator·
 * ChargingProfileService(영속) 오케스트레이션.
 */
@Service
public class SmartChargingServiceImpl implements SmartChargingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartChargingServiceImpl.class);

    private final ChargingProfileService chargingProfileService;
    private final ProfileValidator profileValidator;
    private final CompositeScheduleCalculator compositeCalculator;
    private final ExternalLimitManager externalLimitManager;
    private final ChargingProfileConverter converter;

    public SmartChargingServiceImpl(ChargingProfileService chargingProfileService,
                                    ProfileValidator profileValidator,
                                    CompositeScheduleCalculator compositeCalculator,
                                    ExternalLimitManager externalLimitManager,
                                    ChargingProfileConverter converter) {
        this.chargingProfileService = chargingProfileService;
        this.profileValidator = profileValidator;
        this.compositeCalculator = compositeCalculator;
        this.externalLimitManager = externalLimitManager;
        this.converter = converter;
    }

    @Override
    public void registerProfile(ChargingProfile profile, boolean isUpdate) {
        profileValidator.validate(profile);
        // K01.FR.80 — ChargingStationExternalConstraints 프로파일의 id 는 CS 에 설정된
        // MaxExternalConstraintsId 이하여야 함.
        if (ChargingProfilePurpose.ChargingStationExternalConstraints == profile.getPurpose()) {
            CsConfig cfg = chargingProfileService.findCsConfig(profile.getCpId(), profile.getCsId());
            Integer maxId = cfg != null ? cfg.getMaxExtConstraintsId() : null;
            if (maxId != null && profile.getProfileId() > maxId) {
                throw new SmartChargingException(
                        "ExternalConstraints profileId=" + profile.getProfileId()
                                + " > MaxExternalConstraintsId=" + maxId);
            }
        }
        if (isUpdate) {
            chargingProfileService.updateProfile(profile);
        } else {
            chargingProfileService.saveProfile(profile);
        }
    }

    @Override
    public int clearTxProfiles(String transactionId) {
        if (transactionId == null || transactionId.isEmpty()) {
            return 0;
        }
        List<ChargingProfile> profiles = chargingProfileService.findByTransactionId(transactionId);
        int cleared = 0;
        for (ChargingProfile p : profiles) {
            if (ChargingProfilePurpose.TxProfile == p.getPurpose()) {
                chargingProfileService.deleteProfile(p.getProfileId(), p.getCpId(), p.getCsId());
                cleared++;
            }
        }
        LOGGER.info("clearTxProfiles transactionId={} cleared={}", transactionId, cleared);
        return cleared;
    }

    @Override
    public List<ChargingProfile> resolveActiveProfiles(String cpId, String csId, int evseId) {
        List<ChargingProfile> result = new ArrayList<>();
        // 외부 제약(EMS/grid) — 최우선 상한
        result.addAll(externalLimitManager.activeLimitsAsProfiles(cpId, csId, evseId));
        // 충전소 단위 상한
        addHydrated(result, cpId, csId, 0, ChargingProfilePurpose.ChargingStationExternalConstraints);
        addHydrated(result, cpId, csId, 0, ChargingProfilePurpose.ChargingStationMaxProfile);
        // 해당 EVSE 의 Tx
        addHydrated(result, cpId, csId, evseId, ChargingProfilePurpose.TxProfile);
        addHydrated(result, cpId, csId, evseId, ChargingProfilePurpose.TxDefaultProfile);
        // 충전소 기본 TxDefault (evse 0)
        addHydrated(result, cpId, csId, 0, ChargingProfilePurpose.TxDefaultProfile);
        return result;
    }

    @Override
    public CompositeScheduleType calculateComposite(String cpId, String csId, int evseId,
                                                    int durationSec, String rateUnit) {
        List<ChargingProfile> profiles = resolveActiveProfiles(cpId, csId, evseId);
        return compositeCalculator.calculate(profiles, evseId, durationSec, rateUnit, new Date());
    }

    @Override
    public int syncReportedProfiles(String cpId, String csId, int evseId, List<ChargingProfileType> profiles) {
        if (profiles == null || profiles.isEmpty()) {
            return 0;
        }
        int synced = 0;
        for (ChargingProfileType cp : profiles) {
            try {
                ChargingProfile entity = converter.toEntity(cp, cpId, csId, evseId);
                entity.setWriter(new Writer("SYSTEM"));
                boolean exists = chargingProfileService.findOne(cp.getId(), cpId, csId) != null;
                if (exists) {
                    chargingProfileService.updateProfile(entity);
                } else {
                    chargingProfileService.saveProfile(entity);
                }
                synced++;
            } catch (Exception e) {
                LOGGER.warn("ReportChargingProfiles 동기화 실패 cpId={} csId={} profileId={}: {}",
                        cpId, csId, cp.getId(), e.getMessage());
            }
        }
        LOGGER.info("ReportChargingProfiles 동기화 cpId={} csId={} evseId={} synced={}", cpId, csId, evseId, synced);
        return synced;
    }

    /** 헤더 조회 → findOne 으로 스케줄/기간 hydrate 후 result 에 추가 */
    private void addHydrated(List<ChargingProfile> result, String cpId, String csId, int evseId,
                             ChargingProfilePurpose purpose) {
        List<ChargingProfile> headers =
                chargingProfileService.findActiveByEvse(cpId, csId, evseId, purpose.getCode());
        for (ChargingProfile h : headers) {
            ChargingProfile full = chargingProfileService.findOne(h.getProfileId(), cpId, csId);
            if (full != null) {
                result.add(full);
            }
        }
    }
}
