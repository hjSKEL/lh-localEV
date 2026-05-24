/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity;

import java.util.Date;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.PspPayment;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 25.
 */
public interface PspPaymentProvider {

    void registerPspPayment(PspPayment payment);

    int modifyPspPayment(PspPayment payment);

    int modifyStatus(String pspRef, String statusCd, String reasonCd, String updUserId);

    int linkRechargingId(String pspRef, String rechargingId, Date startedDate, String updUserId);

    int settle(String pspRef, java.math.BigDecimal settledAmount, String receiptUrl, String receiptId,
               Date settledDate, String updUserId);

    int cancel(String pspRef, String reasonCd, Date canceledDate, String updUserId);

    PspPayment retrievePspPayment(String pspRef);

    PspPayment retrievePspPaymentByRechargingId(String rechargingId);

    Page<PspPaymentDto> retrievePspPaymentBySearchCond(PspPaymentSearchCond searchCond);

    int retrieveDailySequence(String yyyymmdd);
}
