/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import kr.co.kevit.localcsms.charger.entity.domain.ChargePoint;
import kr.co.kevit.localcsms.charger.entity.shared.ChargePointSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 10. 29.
 */
public interface ChargePointService {
    
    /**
     * 충전소 검색
     * @param searchCond
     * @return
     */
    @Cacheable(value="retrievetAllChargePoint")
    List<ChargePoint> retrievetAllChargePoint();
    
    /**
     * 충전소 검색
     * @param searchCond
     * @return
     */
    Page<ChargePoint> retrieveChargePointBySearchCond(ChargePointSearchCond searchCond);
    
    /**
     * 
     * @param cpId
     * @return
     */
    ChargePoint retrieveChargePointBySpotId(String cpId);

    /**
     * 충전소 등록
     * @param chargePoint
     * @return
     */
    void registerChargePoint(ChargePoint chargePoint);

    /**
     * 충전소 수정
     * @param chargePoint
     * @return
     */
    void modifyChargePoint(ChargePoint chargePoint);

    /**
     * 충전소 삭제
     * @param chargePoint
     * @return
     */
    void removeChargePoint(ChargePoint chargePoint);

}
