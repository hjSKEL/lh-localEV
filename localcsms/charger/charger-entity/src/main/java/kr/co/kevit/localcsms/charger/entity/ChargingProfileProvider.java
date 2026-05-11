package kr.co.kevit.localcsms.charger.entity;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingProfileSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * TB_CHPF001 / TB_CHPF002 - 충전 프로파일 Provider
 */
public interface ChargingProfileProvider {

    /** 프로파일 + 스케줄 저장 (INSERT) */
    void saveProfile(ChargingProfile profile);

    /** 프로파일 + 스케줄 수정 (UPDATE) */
    void updateProfile(ChargingProfile profile);

    /** 단건 조회 (schedule JSON 포함) */
    ChargingProfile findOne(int profileId, String cpId, String csId);

    /** 조건 검색 (페이지 처리) */
    Page<ChargingProfile> findBySearchCond(ChargingProfileSearchCond cond);

    /** 프로파일 + 스케줄 삭제 */
    void deleteProfile(int profileId, String cpId, String csId);
}
