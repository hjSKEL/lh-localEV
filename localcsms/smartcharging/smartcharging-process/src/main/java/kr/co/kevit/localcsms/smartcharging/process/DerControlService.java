/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.smartcharging.process;

import java.util.Date;
import java.util.Map;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerAlarm;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerControl;
import kr.co.kevit.localcsms.smartcharging.entity.domain.DerStartStop;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerAlarmDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerAlarmSearchCond;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlHisDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlHisSearchCond;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerControlSearchCond;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerStartStopDto;
import kr.co.kevit.localcsms.smartcharging.entity.shared.DerStartStopSearchCond;

/**
 * OCPP 2.1 R04 — DER Control 통합 서비스.
 *
 * @author bckim
 * @since 2026. 5. 26.
 */
public interface DerControlService {

    /** UUIDv4 기반 controlId 생성 */
    String generateControlId();

    /** CSMS 측 사전 등록 (ORIGIN=CSMS, STATUS=PENDING) */
    DerControl registerDerControl(DerControl ctrl, String operId);

    /** SetDERControl push ACK 반영 (PENDING → ACTIVE/REJECTED) */
    void acknowledgeSetPush(String controlId, String csStatus, String reason, String operId);

    /** ClearDERControl push ACK 반영 */
    void acknowledgeClear(String controlId, String csStatus, String reason, String operId);

    /** 운영자 직접 폐기 */
    void deprecate(String controlId, String originCd, String newStatus, String reason, String operId);

    /** CS 의 ReportDERControlRequest 수신 — multi-row INSERT (ORIGIN=CS_REPORT) */
    void recordReport(String cpId, String csId, Map<String, Object> reportPayload, String operId);

    /** NotifyDERAlarmRequest 수신 기록 */
    void recordAlarm(DerAlarm alarm);

    /** NotifyDERStartStopRequest 수신 기록 */
    void recordStartStop(DerStartStop event);

    // === 조회 ===

    DerControl retrieveDerControl(String controlId, String originCd);

    DerControl retrieveActiveCsms(String cpId, String csId, String controlType, String isDefault);

    Page<DerControlDto> retrieveDerControlBySearchCond(DerControlSearchCond cond);

    Page<DerControlHisDto> retrieveHisBySearchCond(DerControlHisSearchCond cond);

    Page<DerAlarmDto> retrieveAlarmBySearchCond(DerAlarmSearchCond cond);

    Page<DerStartStopDto> retrieveStartStopBySearchCond(DerStartStopSearchCond cond);

    /** 외부에서 이력 직접 기록 (PushService 등) */
    void recordHistory(String controlId, String originCd, String actionCd,
                       String preStatus, String postStatus, String remark, String operId, Date occurredDate);
}
