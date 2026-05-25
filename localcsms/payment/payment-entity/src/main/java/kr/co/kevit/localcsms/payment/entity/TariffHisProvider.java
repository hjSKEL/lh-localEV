/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.TariffHis;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 25.
 */
public interface TariffHisProvider {

    void registerTariffHis(TariffHis his);

    Page<TariffHisDto> retrieveTariffHisBySearchCond(TariffHisSearchCond searchCond);
}
