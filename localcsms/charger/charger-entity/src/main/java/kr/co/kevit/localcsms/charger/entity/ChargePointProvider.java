/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity;

import java.util.List;

import kr.co.kevit.localcsms.charger.entity.domain.ChargePoint;
import kr.co.kevit.localcsms.charger.entity.shared.ChargePointSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 9. 14.
 */
public interface ChargePointProvider {
    
    void registerChargePoint(ChargePoint chargePoint);
    
    void modifyChargePoint(ChargePoint chargePoint);
    
    ChargePoint retrieveChargePointByCpId(String cpId);
    
    List<ChargePoint> retrieveChargePointByCpIds(List<String> cpIds);
    
    List<ChargePoint> retrievetAllChargePoint();

    Page<ChargePoint> retrieveChargePointBySearchCond(ChargePointSearchCond searchCond);

    void removeChargePoint(ChargePoint chargePoint);


}
