package kr.co.kevit.localcsms.charger.entity.logic;

import kr.co.kevit.localcsms.charger.entity.ChargingProfileProvider;
import kr.co.kevit.localcsms.charger.entity.dao.ChargingProfileMapper;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingProfileSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TB_CHPF001 / TB_CHPF002 - 충전 프로파일 Provider 구현
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
        mapper.insertSchedule(profile);
    }

    @Override
    public void updateProfile(ChargingProfile profile) {
        mapper.updateProfile(profile);
        mapper.updateSchedule(profile);
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
    public void deleteProfile(int profileId, String cpId, String csId) {
        mapper.deleteSchedule(profileId);
        mapper.deleteProfile(profileId, cpId, csId);
    }
}
