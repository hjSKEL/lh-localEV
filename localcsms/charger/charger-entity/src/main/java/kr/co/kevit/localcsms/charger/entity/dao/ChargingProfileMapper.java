package kr.co.kevit.localcsms.charger.entity.dao;

import kr.co.kevit.localcsms.charger.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingProfileSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TB_CHPF001 / TB_CHPF002 - 충전 프로파일 Mapper
 */
@Repository
public interface ChargingProfileMapper {

    /** TB_CHPF001 단건 INSERT */
    int insertProfile(@Param("p") ChargingProfile profile);

    /** TB_CHPF002 스케줄 JSON INSERT */
    int insertSchedule(@Param("p") ChargingProfile profile);

    /** TB_CHPF001 단건 UPDATE */
    int updateProfile(@Param("p") ChargingProfile profile);

    /** TB_CHPF002 스케줄 JSON UPDATE */
    int updateSchedule(@Param("p") ChargingProfile profile);

    /** TB_CHPF001 단건 조회 (schedule JOIN) */
    ChargingProfile findOne(@Param("profileId") int profileId,
                            @Param("cpId") String cpId,
                            @Param("csId") String csId);

    /** TB_CHPF001 조건 검색 목록 */
    List<ChargingProfile> selectBySearchCond(@Param("cond") ChargingProfileSearchCond cond);

    int countBySearchCond(@Param("cond") ChargingProfileSearchCond cond);

    /** TB_CHPF001 삭제 */
    int deleteProfile(@Param("profileId") int profileId,
                      @Param("cpId") String cpId,
                      @Param("csId") String csId);

    /** TB_CHPF002 스케줄 삭제 */
    int deleteSchedule(@Param("profileId") int profileId);
}
