package kr.co.kevit.localcsms.smartcharging.entity.dao;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingNeedsSnapshot;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedulePeriod;
import kr.co.kevit.localcsms.smartcharging.entity.domain.CsConfig;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ExternalChargingLimit;
import kr.co.kevit.localcsms.smartcharging.entity.domain.NegotiationState;
import kr.co.kevit.localcsms.smartcharging.entity.shared.ChargingProfileSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TB_CHPF001/002/003 (충전 프로파일 정규화) + TB_CHEN001 (EV ChargingNeeds) Mapper.
 */
@Repository
public interface ChargingProfileMapper {

    // === TB_CHPF001 (프로파일 헤더) ===
    int insertProfile(@Param("p") ChargingProfile profile);

    int updateProfile(@Param("p") ChargingProfile profile);

    int deleteProfile(@Param("profileId") int profileId,
                      @Param("cpId") String cpId,
                      @Param("csId") String csId);

    /** 프로파일 + 스케줄 + 기간 단건 조회 (nested) */
    ChargingProfile findOne(@Param("profileId") int profileId,
                            @Param("cpId") String cpId,
                            @Param("csId") String csId);

    List<ChargingProfile> selectBySearchCond(@Param("cond") ChargingProfileSearchCond cond);

    int countBySearchCond(@Param("cond") ChargingProfileSearchCond cond);

    /** transactionId(RC_ID) 기반 프로파일 헤더 목록 (스케줄 제외) */
    List<ChargingProfile> selectByTransactionId(@Param("transactionId") String transactionId);

    /** 특정 EVSE 의 특정 purpose 활성 프로파일 헤더 목록 (스케줄 제외) */
    List<ChargingProfile> selectActiveByEvse(@Param("cpId") String cpId,
                                             @Param("csId") String csId,
                                             @Param("evseId") int evseId,
                                             @Param("purpose") String purpose);

    // === TB_CHPF002 (스케줄) ===
    int insertSchedule(@Param("s") ChargingSchedule schedule);

    int deleteSchedulesByProfile(@Param("profileId") int profileId);

    // === TB_CHPF003 (기간) ===
    int insertPeriod(@Param("p") ChargingSchedulePeriod period);

    int deletePeriodsByProfile(@Param("profileId") int profileId);

    // === TB_CHEN001 (EV ChargingNeeds) ===
    int insertNeeds(@Param("n") ChargingNeedsSnapshot needs);

    /** 특정 EVSE 의 최신 ChargingNeeds 1건 */
    ChargingNeedsSnapshot selectLatestNeeds(@Param("cpId") String cpId,
                                            @Param("csId") String csId,
                                            @Param("evseId") int evseId);

    /** Dynamic kind 이고 dynUpdateInterval 경과한 프로파일 헤더 (push 대상) */
    List<ChargingProfile> selectDueDynamicProfiles();

    /** Dynamic 프로파일의 마지막 push 시각(DYN_UPDATE_TIME) 갱신 */
    int updateDynUpdateTime(@Param("profileId") int profileId);

    // === TB_CHNG001 (협상 상태) ===
    int upsertNegotiation(@Param("n") NegotiationState state);

    NegotiationState selectNegotiation(@Param("cpId") String cpId,
                                       @Param("csId") String csId,
                                       @Param("evseId") int evseId);

    // === TB_CHLM001 (외부 충전 제약) ===
    int insertExternalLimit(@Param("l") ExternalChargingLimit limit);

    /** source(+evse) 의 활성 제약 비활성화 */
    int deactivateExternalLimit(@Param("cpId") String cpId,
                                @Param("csId") String csId,
                                @Param("evseId") Integer evseId,
                                @Param("source") String source);

    /** 충전소 활성 외부 제약 목록 (evseId=0 전체 + 해당 evse) */
    List<ExternalChargingLimit> selectActiveExternalLimits(@Param("cpId") String cpId,
                                                           @Param("csId") String csId,
                                                           @Param("evseId") int evseId);

    // === TB_CSCF001 (CS OCPP 변수 캐시) ===
    int upsertCsConfig(@Param("c") CsConfig config);

    CsConfig selectCsConfig(@Param("cpId") String cpId,
                            @Param("csId") String csId);
}
