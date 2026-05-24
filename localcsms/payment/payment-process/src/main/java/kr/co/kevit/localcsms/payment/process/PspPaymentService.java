/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.process;

import java.math.BigDecimal;
import java.util.Date;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.PspPayment;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentSearchCond;

/**
 * PSP 결제 서비스
 *
 * <p>상태 전이: AUTHORIZED → STARTED → SETTLED
 *               AUTHORIZED → CANCELED
 *               STARTED    → CANCELED</p>
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
public interface PspPaymentService {

    /**
     * 다음 PSP_REF 생성. 포맷: PSP-yyyyMMdd-NNNN (NNNN = 일 단위 시퀀스)
     */
    String generateNextPspRef();

    /** 결제 인증 등록 (AUTHORIZED). pspRef 없으면 자동 생성. */
    PspPayment registerPspPayment(PspPayment payment);

    /** 상태 변경 + 이력 자동 기록 */
    void modifyStatus(String pspRef, String postStatus, String reasonCd, String remark, String operId);

    /** TransactionEvent.Started 도착 시 호출: rechargingId 연동 + STARTED 전이 + 이력 기록 */
    void linkTransaction(String pspRef, String rechargingId, Date startedDate, String operId);

    /** 정산 처리 (SETTLED). settledAmount 와 영수증 정보 기록 + 이력 기록 */
    void settle(String pspRef, BigDecimal settledAmount, String receiptUrl, String receiptId, String operId);

    /** 취소 처리 (CANCELED). + 이력 기록 */
    void cancel(String pspRef, String reasonCd, String remark, String operId);

    PspPayment retrievePspPayment(String pspRef);

    PspPayment retrievePspPaymentByRechargingId(String rechargingId);

    Page<PspPaymentDto> retrievePspPaymentBySearchCond(PspPaymentSearchCond searchCond);

    Page<PspPaymentHisDto> retrievePspPaymentHisBySearchCond(PspPaymentHisSearchCond searchCond);
}
