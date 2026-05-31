package kr.co.kevit.localcsms.smartcharging.process;

import java.util.List;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.domain.CompositeScheduleType;

/**
 * Smart Charging 도메인 엔진 (OCPP 2.1 K 그룹).
 *
 * <p>책임: 프로파일 검증·저장(K01), lifecycle(트랜잭션 종료 시 TxProfile 폐기),
 * 합성 스케줄 계산(K08), 활성 프로파일 해석.</p>
 */
public interface SmartChargingService {

    /** 검증 후 저장(신규)/수정. 실패 시 {@link SmartChargingException}. */
    void registerProfile(ChargingProfile profile, boolean isUpdate);

    /** 트랜잭션 종료 시 해당 transactionId 의 TxProfile 전부 폐기. 폐기 건수 반환. */
    int clearTxProfiles(String transactionId);

    /** EVSE 에 적용 가능한 활성 프로파일(스케줄 포함) 해석. */
    List<ChargingProfile> resolveActiveProfiles(String cpId, String csId, int evseId);

    /** 합성 스케줄 계산 (GetCompositeSchedule 응답용). */
    CompositeScheduleType calculateComposite(String cpId, String csId, int evseId,
                                             int durationSec, String rateUnit);

    /** ReportChargingProfiles(CS→CSMS) 수신분을 CSMS 저장소에 동기화(upsert). 반환: 동기화 건수. */
    int syncReportedProfiles(String cpId, String csId, int evseId, List<ChargingProfileType> profiles);

    /**
     * CSMS→CS 로 송신한 SetChargingProfile 의 프로파일을 CSMS 저장소에 upsert.
     *
     * <p>K28 PullDynamicScheduleUpdate 응답 시 송신 이력 조회가 가능하도록 보장한다.
     * REST API(외부 트리거) 와 내부 협상(followUp) 양쪽 경로 모두에서 호출.</p>
     */
    void persistSentProfile(String cpId, String csId, int evseId, ChargingProfileType profile);
}
