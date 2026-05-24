/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.Vat;
import kr.co.kevit.localcsms.payment.entity.shared.VatDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatSearchCond;

/**
 * @author bckim
 * @since 2026. 5. 24.
 */
public interface VatProvider {

    void registerVat(Vat vat);

    int modifyVat(Vat vat);

    /** 사용여부만 변경 (Y/N 토글). */
    int modifyUseYn(String vatNo, String useYn, String updUserId);

    Vat retrieveVat(String vatNo);

    Page<VatDto> retrieveVatBySearchCond(VatSearchCond searchCond);
}
