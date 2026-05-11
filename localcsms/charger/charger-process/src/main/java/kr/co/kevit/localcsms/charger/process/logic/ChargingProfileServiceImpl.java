package kr.co.kevit.localcsms.charger.process.logic;

import kr.co.kevit.localcsms.charger.entity.ChargingProfileProvider;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingProfileSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargingProfileService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

/**
 * TB_CHPF001 / TB_CHPF002 - 충전 프로파일 Service 구현
 */
@Service
public class ChargingProfileServiceImpl implements ChargingProfileService {

    private final ChargingProfileProvider chargingProfileProvider;

    public ChargingProfileServiceImpl(ChargingProfileProvider chargingProfileProvider) {
        this.chargingProfileProvider = chargingProfileProvider;
    }

    @Override
    public void saveProfile(ChargingProfile profile) {
        chargingProfileProvider.saveProfile(profile);
    }

    @Override
    public void updateProfile(ChargingProfile profile) {
        chargingProfileProvider.updateProfile(profile);
    }

    @Override
    public ChargingProfile findOne(int profileId, String cpId, String csId) {
        return chargingProfileProvider.findOne(profileId, cpId, csId);
    }

    @Override
    public Page<ChargingProfile> retrieveBySearchCond(ChargingProfileSearchCond cond) {
        return chargingProfileProvider.findBySearchCond(cond);
    }

    @Override
    public void deleteProfile(int profileId, String cpId, String csId) {
        chargingProfileProvider.deleteProfile(profileId, cpId, csId);
    }
}
