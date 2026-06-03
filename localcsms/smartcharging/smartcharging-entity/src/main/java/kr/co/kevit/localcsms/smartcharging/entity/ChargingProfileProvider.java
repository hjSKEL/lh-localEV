package kr.co.kevit.localcsms.smartcharging.entity;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingNeedsSnapshot;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.CsConfig;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ExternalChargingLimit;
import kr.co.kevit.localcsms.smartcharging.entity.domain.NegotiationState;
import kr.co.kevit.localcsms.smartcharging.entity.shared.ChargingProfileSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * TB_CHPF001/002/003 (정규화) + TB_CHEN001 - 충전 프로파일 Provider.
 */
public interface ChargingProfileProvider {

    /** 프로파일 + 스케줄 + 기간 저장 (INSERT) */
    void saveProfile(ChargingProfile profile);

    /** 프로파일 + 스케줄 + 기간 수정 (헤더 UPDATE + 스케줄/기간 재구성) */
    void updateProfile(ChargingProfile profile);

    /** 단건 조회 (스케줄/기간 포함) */
    ChargingProfile findOne(int profileId, String cpId, String csId);

    /** 조건 검색 (페이지, 헤더만) */
    Page<ChargingProfile> findBySearchCond(ChargingProfileSearchCond cond);

    /** transactionId 기반 프로파일 헤더 목록 */
    List<ChargingProfile> findByTransactionId(String transactionId);

    /** EVSE + purpose 활성 프로파일 헤더 목록 */
    List<ChargingProfile> findActiveByEvse(String cpId, String csId, int evseId, String purpose);

    /** Dynamic + dynUpdateInterval 경과 프로파일 헤더 (push 대상) */
    List<ChargingProfile> findDueDynamicProfiles();

    /** Dynamic 프로파일 push 시각 갱신 */
    void touchDynUpdateTime(int profileId);

    /** UpdateDynamicSchedule 자동 push 대상 (Dynamic + Central operationMode + 도래시각 경과) */
    List<ChargingProfile> findAutoPushTargets();

    /** 프로파일 + 스케줄 + 기간 삭제 */
    void deleteProfile(int profileId, String cpId, String csId);

    // === EV ChargingNeeds ===
    void saveNeeds(ChargingNeedsSnapshot needs);

    ChargingNeedsSnapshot findLatestNeeds(String cpId, String csId, int evseId);

    // === 협상 상태 ===
    void saveNegotiation(NegotiationState state);

    NegotiationState findNegotiation(String cpId, String csId, int evseId);

    // === 외부 충전 제약 ===
    void saveExternalLimit(ExternalChargingLimit limit);

    void deactivateExternalLimit(String cpId, String csId, Integer evseId, String source);

    List<ExternalChargingLimit> findActiveExternalLimits(String cpId, String csId, int evseId);

    // === CS OCPP 변수 캐시 ===
    void saveCsConfig(CsConfig config);

    CsConfig findCsConfig(String cpId, String csId);
}
