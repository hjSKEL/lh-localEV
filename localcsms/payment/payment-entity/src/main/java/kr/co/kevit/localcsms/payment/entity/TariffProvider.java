/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity;

import java.util.Date;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.Tariff;
import kr.co.kevit.localcsms.payment.entity.shared.TariffDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 25.
 */
public interface TariffProvider {

    void registerTariff(Tariff tariff);

    int modifyTariffStatus(String tariffId, String statusCd, Date validTo, String updUserId);

    Tariff retrieveTariff(String tariffId);

    Page<TariffDto> retrieveTariffBySearchCond(TariffSearchCond searchCond);

    int retrieveDailySequence(String tariffKind, String yyyymmdd);
}
