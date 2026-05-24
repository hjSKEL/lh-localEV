/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.Vat;
import kr.co.kevit.localcsms.payment.entity.domain.VatHis;
import kr.co.kevit.localcsms.payment.entity.shared.VatDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.VatSearchCond;

/**
 * 사업자(VAT) 서비스
 *
 * @author bckim
 * @since 2026. 5. 24.
 */
public interface VatService {

    /** 등록 */
    void registerVat(Vat vat);

    /** 수정 (전체 필드 갱신) */
    void modifyVat(Vat vat);

    /** 사용여부 변경 (Y/N) */
    void modifyUseYn(String vatNo, String useYn, String updUserId);

    Vat retrieveVat(String vatNo);

    Page<VatDto> retrieveVatBySearchCond(VatSearchCond searchCond);

    /** 검증 이력 기록. */
    void recordVatValidation(VatHis his);

    Page<VatHisDto> retrieveVatHisBySearchCond(VatHisSearchCond searchCond);
}
