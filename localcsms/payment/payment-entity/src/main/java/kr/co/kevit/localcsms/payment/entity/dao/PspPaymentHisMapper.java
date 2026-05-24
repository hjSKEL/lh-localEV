/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.payment.entity.domain.PspPaymentHis;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisSearchCond;

/**
 * PSP 결제 상태변경 이력 Mapper
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
@Repository
public interface PspPaymentHisMapper {

    int insertPspPaymentHis(@Param("his") PspPaymentHis his);

    int countPspPaymentHisBySearchCond(@Param("searchCond") PspPaymentHisSearchCond searchCond);

    List<PspPaymentHisDto> selectPspPaymentHisBySearchCond(@Param("searchCond") PspPaymentHisSearchCond searchCond);
}
