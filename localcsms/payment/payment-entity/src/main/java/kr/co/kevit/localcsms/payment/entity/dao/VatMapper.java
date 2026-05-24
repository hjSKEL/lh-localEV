/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.payment.entity.domain.Vat;
import kr.co.kevit.localcsms.payment.entity.shared.VatDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatSearchCond;

/**
 * 사업자(VAT) Mapper
 *
 * @author bckim
 * @since 2026. 5. 24.
 */
@Repository
public interface VatMapper {

    int insertVat(@Param("vat") Vat vat);

    int updateVat(@Param("vat") Vat vat);

    int updateUseYn(@Param("vatNo") String vatNo,
                    @Param("useYn") String useYn,
                    @Param("updUserId") String updUserId);

    Vat selectVat(@Param("vatNo") String vatNo);

    int countVatBySearchCond(@Param("searchCond") VatSearchCond searchCond);

    List<VatDto> selectVatBySearchCond(@Param("searchCond") VatSearchCond searchCond);
}
