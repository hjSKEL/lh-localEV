/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.payment.entity.domain.Tariff;
import kr.co.kevit.localcsms.payment.entity.shared.TariffDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffSearchCond;

@Repository
public interface TariffMapper {

    int insertTariff(@Param("tariff") Tariff tariff);

    int updateTariffStatus(@Param("tariffId") String tariffId,
                           @Param("statusCd") String statusCd,
                           @Param("validTo") Date validTo,
                           @Param("updUserId") String updUserId);

    Tariff selectTariff(@Param("tariffId") String tariffId);

    int countTariffBySearchCond(@Param("searchCond") TariffSearchCond searchCond);

    List<TariffDto> selectTariffBySearchCond(@Param("searchCond") TariffSearchCond searchCond);

    Integer selectDailySequence(@Param("kind") String kind, @Param("datePrefix") String datePrefix);
}
