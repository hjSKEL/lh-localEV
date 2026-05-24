/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.PspPaymentHis;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 25.
 */
public interface PspPaymentHisProvider {

    void registerPspPaymentHis(PspPaymentHis his);

    Page<PspPaymentHisDto> retrievePspPaymentHisBySearchCond(PspPaymentHisSearchCond searchCond);
}
