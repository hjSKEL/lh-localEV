package kr.co.kevit.localcsms.charger.process;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingProfileSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * TB_CHPF001 / TB_CHPF002 - 충전 프로파일 Service
 */
public interface ChargingProfileService {

    /** 프로파일 + 스케줄 저장 */
    void saveProfile(ChargingProfile profile);

    /** 프로파일 + 스케줄 수정 */
    void updateProfile(ChargingProfile profile);

    /** 단건 조회 (schedule JSON 포함) */
    ChargingProfile findOne(int profileId, String cpId, String csId);

    /** 페이지 목록 조회 */
    Page<ChargingProfile> retrieveBySearchCond(ChargingProfileSearchCond cond);

    /** 프로파일 삭제 */
    void deleteProfile(int profileId, String cpId, String csId);
}
