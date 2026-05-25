/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.process;

import java.util.Date;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.Tariff;
import kr.co.kevit.localcsms.payment.entity.domain.TariffAssignment;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.TariffDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.TariffSearchCond;

/**
 * Tariff 서비스 (OCPP 2.1 use case I07~I12 통합).
 *
 * <p>책임:<br>
 * 1) Tariff 마스터(불변) 등록/조회/폐기<br>
 * 2) Assignment 관리 (DEFAULT_EVSE / DRIVER_IDTOKEN)<br>
 * 3) CS push 결과 반영 (PENDING → ACTIVE/REJECTED)<br>
 * 4) 이력 자동 기록</p>
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
public interface TariffService {

    /** 다음 tariffId 생성. 포맷: T-{kind}-{yyyyMMdd}-{NNNN} */
    String generateNextTariffId(String tariffKind);

    /** Tariff 마스터 등록. tariffId 없으면 자동 생성. */
    Tariff registerTariff(Tariff tariff, String operId);

    /** Tariff 폐기 (REPLACED/CLEARED 등). 이력 기록. */
    void deprecateTariff(String tariffId, String newStatus, String reason, String operId);

    Tariff retrieveTariff(String tariffId);

    Page<TariffDto> retrieveTariffBySearchCond(TariffSearchCond cond);

    // === Driver Assignment ===

    /** idToken 에 driver tariff 매핑. 기존 활성 매핑은 REPLACED 로 전이. */
    TariffAssignment assignDriverTariff(String tariffId, String idToken, String operId);

    /** Driver Tariff 해제 (CLEARED). */
    void clearDriverTariff(String idToken, String operId);

    /** TC_I_109 - Authorize 응답에 동봉할 driver tariff lookup. */
    Tariff retrieveActiveDriverTariff(String idToken);

    // === Default EVSE Assignment ===

    /** Default Tariff push 사전 등록 (PENDING). 별도 PushService 가 CS 로 전송 + ack 시 ACTIVE. */
    TariffAssignment assignDefaultTariffPending(String tariffId, String cpId, String csId, Integer evseId,
                                                 String operId);

    /** CS push ack 반영: PENDING → ACTIVE (Accepted) 또는 REJECTED. */
    void acknowledgeDefaultTariffPush(long assignmentSeq, String csStatus, String reason, String operId);

    /** Default Tariff Clear (ClearTariffsRequest ack 시 호출). */
    void acknowledgeClearTariffs(long assignmentSeq, String csStatus, String reason, String operId);

    Tariff retrieveActiveDefaultTariff(String cpId, String csId, int evseId);

    // === 조회 ===

    TariffAssignment retrieveAssignment(long seq);

    Page<TariffAssignmentDto> retrieveAssignmentBySearchCond(TariffAssignmentSearchCond cond);

    Page<TariffHisDto> retrieveHisBySearchCond(TariffHisSearchCond cond);

    /** 외부 사용 (PushService 등) — 이력 기록 */
    void recordHistory(String tariffId, Long assignmentSeq, String actionCd,
                       String preStatusCd, String postStatusCd, String remark, String operId, Date occurredDate);
}
