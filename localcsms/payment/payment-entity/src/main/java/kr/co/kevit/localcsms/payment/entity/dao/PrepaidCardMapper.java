/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardSearchCond;

/**
 * 선불카드 Mapper
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@Repository
public interface PrepaidCardMapper {

    int insertPrepaidCard(@Param("card") PrepaidCard card);

    int updatePrepaidCard(@Param("card") PrepaidCard card);

    /** 잔액/상태 변경 (낙관적 검증: 기대잔액과 일치할 때만 update) */
    int updateBalance(@Param("cardNo") String cardNo,
                      @Param("expectedBalance") Long expectedBalance,
                      @Param("newBalance") Long newBalance,
                      @Param("updUserId") String updUserId);

    /** 상태 코드만 변경 (활성/정지/만료 토글용) */
    int updateCardStatus(@Param("cardNo") String cardNo,
                         @Param("cardStatCode") String cardStatCode,
                         @Param("updUserId") String updUserId);

    PrepaidCard selectPrepaidCard(@Param("cardNo") String cardNo);

    /** SELECT ... FOR UPDATE (행 잠금) */
    PrepaidCard selectPrepaidCardForUpdate(@Param("cardNo") String cardNo);

    int countPrepaidCardBySearchCond(@Param("searchCond") PrepaidCardSearchCond searchCond);

    List<PrepaidCardDto> selectPrepaidCardBySearchCond(@Param("searchCond") PrepaidCardSearchCond searchCond);

}
