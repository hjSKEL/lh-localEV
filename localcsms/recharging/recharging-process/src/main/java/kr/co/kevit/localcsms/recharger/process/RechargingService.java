/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingMonthlyCustomerDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingSearchCond;

import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 17.
 */
public interface RechargingService {
    
    /**
     * for IF
     * @param id
     * @return
     */
    Recharging retrieveRecharging4IfById(String id);
    
    /**
     * for IF
     * @param recharging
     * @return
     */
    void registerRecharging(Recharging recharging);
    
    /**
     * for IF
     * @param recharging
     * @return
     */
    void modifyRecharging(Recharging recharging);

    /**
     * 진행 중(RECS02) 트랜잭션의 최대 에너지 한도(Wh) 변경.
     * 0 이면 한도 해제. 진행 중이 아니면 예외.
     */
    void modifyMaxEnergy(String rechargingId, Double maxEnergy, String updUserId);

    void completeRecharging(Recharging recharging);

    /** LH 모드 proxy-eai StopTransaction 처리용 — OCPP transactionId 로 최근 1건 역매칭. 없으면 null. */
    Recharging retrieveLatestRechargingByCpCsIdAndTransactionId(String cpId, String csId, String transactionId);

    RechargingDto retrieveRechargingById(String id);
    
    List<RechargingDto> retrieveRechargingDtoByIds(List<String> ids);
    
    Page<RechargingDto> retrieveRechargingByRechargingSearchCond(RechargingSearchCond searchCond);
    
    List<RechargingDto> retrieveRecharging4DownloadByRechargingSearchCond(RechargingSearchCond searchCond);
    
    Page<RechargingDto> retrieveRechargingWithCustomerByRechargingSearchCond(RechargingSearchCond searchCond);
    
    List<RechargingDto> retrieveRechargingWithCustomer4DownloadByRechargingSearchCond(RechargingSearchCond searchCond);

    /** 월별 고객별(동/호) 충전요금 집계 - 엑셀다운로드(RC_100) 전용, year/month는 화면 표시용으로 각 행에 채워 반환 */
    List<RechargingMonthlyCustomerDto> retrieveMonthlyCustomerSummary4Download(int year, int month);
}
