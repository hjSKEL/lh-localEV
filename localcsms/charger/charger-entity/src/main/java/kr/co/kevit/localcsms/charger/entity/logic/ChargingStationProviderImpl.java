/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.charger.entity.ChargingStationProvider;
import kr.co.kevit.localcsms.charger.entity.dao.ChargingStationMapper;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStationCsm;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 30.
 */
@Component
public class ChargingStationProviderImpl implements ChargingStationProvider {

    /** 충전 서비스 방식(CSST00) - 배터리 교환형 */
    private static final String CS_SVC_TYPE_BATTERY_SWAP = "CSST02";

    @Autowired
    private ChargingStationMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingStation retrieveChargingStationByCpIdNCsId(String cpId, String csId) {
        // 
        return mapper.selectChargingStationByCpIdNCsId(cpId, csId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChargingStation(ChargingStation chargingStation) {
        // 
        mapper.insertChargingStation(chargingStation);
        ChargingStationCsm csm = new ChargingStationCsm();
        csm.setCpId(chargingStation.getCpId());
        csm.setCsId(chargingStation.getCsId());
        mapper.insertChargingStationCsm(csm);

        // 배터리 교환형(CSST02)인 경우에만 확장정보(TB_BSCS001) 저장
        if(CS_SVC_TYPE_BATTERY_SWAP.equals(chargingStation.getCsServiceType())
                && chargingStation.getBatterySwapInfo() != null) {
            mapper.insertBatterySwapInfo(chargingStation);
        }

        ChargerStatusInfo chargerStatusInfo = new ChargerStatusInfo();
        chargerStatusInfo.setCpId(chargingStation.getCpId());
        chargerStatusInfo.setCsId(chargingStation.getCsId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyChargingStation(ChargingStation chargingStation) {
        //
        mapper.updateChargingStation(chargingStation);

        // 배터리 교환형 확장정보(TB_BSCS001) upsert / 일반 전환 시 정리
        if(CS_SVC_TYPE_BATTERY_SWAP.equals(chargingStation.getCsServiceType())
                && chargingStation.getBatterySwapInfo() != null) {
            if(mapper.updateBatterySwapInfo(chargingStation) == 0) {
                mapper.insertBatterySwapInfo(chargingStation);
            }
        } else {
            mapper.deleteBatterySwapInfo(chargingStation.getCpId(), chargingStation.getCsId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyChargingStationCsm(ChargingStationCsm chargingStation) {
        // 
        mapper.updateChargingStationCsm(chargingStation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingStationCsm retrieveChargingStationCsmByCpIdNCsId(String cpId, String csId) {
        // 
        return mapper.selectChargingStationCsmByCpIdNCsId(cpId, csId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChargingStationCsm> retrieveChargingStationCsmByCpId(String cpId) {
        // 
        return mapper.selectChargingStationCsmByCpId(cpId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ChargingStationDto> retrieveChargingStationBySearchCond(ChargingStationSearchCond searchCond) {
        // 
        Page<ChargingStationDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        searchCond.setTotalItemCount(mapper.countChargingStationBySearchCond(searchCond));
        if(searchCond.getTotalItemCount() > 0) {            
            resultSet.setResult(mapper.selectChargingStationBySearchCond(searchCond));
        }else {
            resultSet.setResult(new ArrayList<>(0));
        }
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingStationDto retrieveChargingStationDtoByCpIdNCsId(String cpId, String csId) {
        // 
        return mapper.selectChargingStationDtoByCpIdNCsId(cpId, csId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeChargingStation(String cpId, String csId) {
        // 자식 테이블(TB_BSCS001)을 FK 부모(TB_CHCS001)보다 먼저 삭제. 행 없으면 0건으로 무해.
        mapper.deleteBatterySwapInfo(cpId, csId);
        mapper.deleteChargingStation(cpId, csId);
        mapper.deleteChargingStationCsm(cpId, csId);
    }

}
