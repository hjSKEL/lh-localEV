/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.payment.entity.domain.TariffHis;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisSearchCond;

@Repository
public interface TariffHisMapper {

    int insertTariffHis(@Param("his") TariffHis his);

    int countTariffHisBySearchCond(@Param("searchCond") TariffHisSearchCond searchCond);

    List<TariffHisDto> selectTariffHisBySearchCond(@Param("searchCond") TariffHisSearchCond searchCond);
}
