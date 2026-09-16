/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingMonthlyCustomerDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingSearchCond;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 17.
 */
@Component
public interface RechargingProvider {
    
    void registerRecharging(Recharging recharging);
    
    void registerRechargingError(Recharging recharging);
    
    void modifyRecharging(Recharging recharging);

    /** 진행 중 트랜잭션의 최대 에너지 한도(Wh)만 갱신. */
    int modifyMaxEnergy(String rechargingId, Double maxEnergy);
    
    Recharging retrieveRechargingById(String id);

    /** LH 모드 proxy-eai StopTransaction 처리용 — OCPP transactionId 로 최근 1건 역매칭. */
    Recharging retrieveLatestRechargingByCpCsIdAndTransactionId(String cpId, String csId, String transactionId);

    List<RechargingDto> retrieveRechargingDtoByIds(List<String> ids);
    
    Page<RechargingDto> retrieveRechargingByRechargingSearchCond(RechargingSearchCond searchCond);
    
    List<RechargingDto> retrieveRecharging4DownloadByRechargingSearchCond(RechargingSearchCond searchCond);

    RechargingDto retrieveRechargingDtoById(String id);

    /** 월별 고객별(동/호) 충전요금 집계 - 엑셀다운로드(RC_100) 전용 */
    List<RechargingMonthlyCustomerDto> retrieveMonthlyCustomerSummary(String fromDate, String toDate);
}
