/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.charger.entity.ChargePointProvider;
import kr.co.kevit.localcsms.charger.entity.dao.ChargePointMapper;
import kr.co.kevit.localcsms.charger.entity.domain.ChargePoint;
import kr.co.kevit.localcsms.charger.entity.shared.ChargePointSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 9. 14.
 */
@Component
public class ChargePointProviderImpl implements ChargePointProvider {

    @Autowired
    private ChargePointMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChargePoint(ChargePoint chargePoint) {
        //
        mapper.insertChargePoint(chargePoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyChargePoint(ChargePoint chargePoint) {
        //
        mapper.updateChargePoint(chargePoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargePoint retrieveChargePointByCpId(String cpId) {
        //
        return mapper.selectChargePointByCpId(cpId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChargePoint> retrievetAllChargePoint(){
        //
        return mapper.selectAllChargePoint();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Page<ChargePoint> retrieveChargePointBySearchCond(ChargePointSearchCond searchCond) {
        //
        int totalItemCount = mapper.countChargePointBySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        Page<ChargePoint> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) {
            return resultSet;
        }
        List<ChargePoint> result = mapper.selectChargePointBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

    @Override
    public void removeChargePoint(ChargePoint chargePoint) {
        //
        // 삭제여부 Y로 변경
        ChargePoint existChargePoint = mapper.selectChargePointByCpId(chargePoint.getCpId());
        existChargePoint.setCpUseYn(StringConstants.N);
        existChargePoint.setDeleteYn(StringConstants.Y);
        existChargePoint.setDeleteDate(chargePoint.getWriter().getUpdateDate());
        existChargePoint.getWriter().setUpdUserId(chargePoint.getWriter().getUpdUserId());
        existChargePoint.getWriter().setUpdateDate(chargePoint.getWriter().getUpdateDate());
        mapper.updateChargePoint(existChargePoint);
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChargePoint> retrieveChargePointByCpIds(List<String> cpIds) {
        // 
        return mapper.selectChargePointByCpIds(cpIds);
    }

}
