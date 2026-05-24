/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.payment.entity.domain.VatHis;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisSearchCond;

/**
 * VAT 검증 이력 Mapper
 *
 * @author bckim
 * @since 2026. 5. 24.
 */
@Repository
public interface VatHisMapper {

    int insertVatHis(@Param("his") VatHis his);

    int countVatHisBySearchCond(@Param("searchCond") VatHisSearchCond searchCond);

    List<VatHisDto> selectVatHisBySearchCond(@Param("searchCond") VatHisSearchCond searchCond);
}
