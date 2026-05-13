/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCardHis;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisSearchCond;

/**
 * 선불카드 거래이력 Mapper
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Repository
public interface PrepaidCardHisMapper {

    /** 거래이력 등록 (SEQ는 AUTO_INCREMENT 후 history.seq 에 반환) */
    int insertPrepaidCardHis(@Param("history") PrepaidCardHis history);

    PrepaidCardHis selectPrepaidCardHis(@Param("seq") Long seq);

    int countPrepaidCardHisBySearchCond(@Param("searchCond") PrepaidCardHisSearchCond searchCond);

    List<PrepaidCardHisDto> selectPrepaidCardHisBySearchCond(@Param("searchCond") PrepaidCardHisSearchCond searchCond);

}
