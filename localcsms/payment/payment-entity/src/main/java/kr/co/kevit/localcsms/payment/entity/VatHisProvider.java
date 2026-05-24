/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.VatHis;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 24.
 */
public interface VatHisProvider {

    void registerVatHis(VatHis his);

    Page<VatHisDto> retrieveVatHisBySearchCond(VatHisSearchCond searchCond);
}
