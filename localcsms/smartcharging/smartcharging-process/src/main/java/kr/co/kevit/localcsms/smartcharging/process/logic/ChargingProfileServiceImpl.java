package kr.co.kevit.localcsms.smartcharging.process.logic;

import kr.co.kevit.localcsms.smartcharging.entity.ChargingProfileProvider;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingNeedsSnapshot;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.CsConfig;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ExternalChargingLimit;
import kr.co.kevit.localcsms.smartcharging.entity.domain.NegotiationState;
import kr.co.kevit.localcsms.smartcharging.entity.shared.ChargingProfileSearchCond;
import kr.co.kevit.localcsms.smartcharging.process.ChargingProfileService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TB_CHPF001/002/003 (정규화) + TB_CHEN001 - 충전 프로파일 Service 구현.
 */
@Service
public class ChargingProfileServiceImpl implements ChargingProfileService {

    private final ChargingProfileProvider chargingProfileProvider;

    public ChargingProfileServiceImpl(ChargingProfileProvider chargingProfileProvider) {
        this.chargingProfileProvider = chargingProfileProvider;
    }

    @Transactional
    @Override
    public void saveProfile(ChargingProfile profile) {
        chargingProfileProvider.saveProfile(profile);
    }

    @Transactional
    @Override
    public void updateProfile(ChargingProfile profile) {
        chargingProfileProvider.updateProfile(profile);
    }

    @Transactional(readOnly = true)
    @Override
    public ChargingProfile findOne(int profileId, String cpId, String csId) {
        return chargingProfileProvider.findOne(profileId, cpId, csId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ChargingProfile> retrieveBySearchCond(ChargingProfileSearchCond cond) {
        return chargingProfileProvider.findBySearchCond(cond);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChargingProfile> findByTransactionId(String transactionId) {
        return chargingProfileProvider.findByTransactionId(transactionId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChargingProfile> findActiveByEvse(String cpId, String csId, int evseId, String purpose) {
        return chargingProfileProvider.findActiveByEvse(cpId, csId, evseId, purpose);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChargingProfile> findDueDynamicProfiles() {
        return chargingProfileProvider.findDueDynamicProfiles();
    }

    @Transactional
    @Override
    public void touchDynUpdateTime(int profileId) {
        chargingProfileProvider.touchDynUpdateTime(profileId);
    }

    @Transactional
    @Override
    public void deleteProfile(int profileId, String cpId, String csId) {
        chargingProfileProvider.deleteProfile(profileId, cpId, csId);
    }

    @Transactional
    @Override
    public void saveNeeds(ChargingNeedsSnapshot needs) {
        chargingProfileProvider.saveNeeds(needs);
    }

    @Transactional(readOnly = true)
    @Override
    public ChargingNeedsSnapshot findLatestNeeds(String cpId, String csId, int evseId) {
        return chargingProfileProvider.findLatestNeeds(cpId, csId, evseId);
    }

    @Transactional
    @Override
    public void saveNegotiation(NegotiationState state) {
        chargingProfileProvider.saveNegotiation(state);
    }

    @Transactional(readOnly = true)
    @Override
    public NegotiationState findNegotiation(String cpId, String csId, int evseId) {
        return chargingProfileProvider.findNegotiation(cpId, csId, evseId);
    }

    @Transactional
    @Override
    public void saveExternalLimit(ExternalChargingLimit limit) {
        chargingProfileProvider.saveExternalLimit(limit);
    }

    @Transactional
    @Override
    public void deactivateExternalLimit(String cpId, String csId, Integer evseId, String source) {
        chargingProfileProvider.deactivateExternalLimit(cpId, csId, evseId, source);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ExternalChargingLimit> findActiveExternalLimits(String cpId, String csId, int evseId) {
        return chargingProfileProvider.findActiveExternalLimits(cpId, csId, evseId);
    }

    @Transactional
    @Override
    public void saveCsConfig(CsConfig config) {
        chargingProfileProvider.saveCsConfig(config);
    }

    @Transactional(readOnly = true)
    @Override
    public CsConfig findCsConfig(String cpId, String csId) {
        return chargingProfileProvider.findCsConfig(cpId, csId);
    }
}
