/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingMonthlyCustomerDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingSearchCond;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 17.
 */
@Repository
public interface RechargingMapper {
    
    int insertRecharging(@Param("recharging") Recharging recharging);
    
    int insertRechargingError(@Param("recharging") Recharging recharging);
    
    int updateRecharging(@Param("recharging") Recharging recharging);

    /** 진행 중 트랜잭션의 최대 에너지 한도(Wh)만 갱신. 0 = 한도 없음. */
    int updateMaxEnergy(@Param("rechargingId") String rechargingId,
                        @Param("maxEnergy") Double maxEnergy);
    
    Recharging selectRechargingById(@Param("rechargingId") String rechargingId);
    
    RechargingDto selectRechargingDtoById(String id);
    
    List<RechargingDto> selectRechargingDtoByIds(@Param("rechargingIds") List<String> ids);
    
    int countRechargingByRechargingSearchCond(@Param("searchCond") RechargingSearchCond searchCond);

    /** 검색조건에 걸리는 전체 결과의 청구금액(PAY_SUM) 합계 - 페이징 무관 */
    Long sumPaySumByRechargingSearchCond(@Param("searchCond") RechargingSearchCond searchCond);

    List<RechargingDto> selectRechargingByRechargingSearchCond(@Param("searchCond") RechargingSearchCond searchCond);

    /** 월별 고객별(동/호) 충전요금 집계 - 엑셀다운로드(RC_100) 전용 */
    List<RechargingMonthlyCustomerDto> selectMonthlyCustomerSummary(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

}
