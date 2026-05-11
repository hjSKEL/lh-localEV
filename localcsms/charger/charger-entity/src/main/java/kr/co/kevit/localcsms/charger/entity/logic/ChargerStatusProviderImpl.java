/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.charger.entity.ChargerStatusProvider;
import kr.co.kevit.localcsms.charger.entity.dao.ChargerStatusHisMapper;
import kr.co.kevit.localcsms.charger.entity.dao.ChargerStatusMapper;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 30.
 */
@Component
public class ChargerStatusProviderImpl implements ChargerStatusProvider {

    @Autowired
    private ChargerStatusMapper mapper;
    
    @Autowired
    private ChargerStatusHisMapper hisMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerChargerStatus(ChargerStatusInfo chargerStatusInfo) {
        //
        mapper.insertChargerStatus(chargerStatusInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyChargerStatus(ChargerStatusInfo chargerStatusInfo) {
        //
        mapper.updateChargerStatus(chargerStatusInfo);
        hisMapper.insertChargerStatusHis(chargerStatusInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyChargerStatusWithoutHis(ChargerStatusInfo chargerStatusInfo) {
        // 
        mapper.updateChargerStatus(chargerStatusInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChargerStatusInfo> retrieveChargerStatusByCpIdNCsId(String cpId, String csId) {
        //
        return mapper.selectChargerStatusByCpIdNCsId(cpId, csId);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Page<ChargerStatusInfoDto> retrieveChargerStatusBySearchCond(ChargerStatusSearchCond searchCond) {
        //
        int totalItemCount = mapper.countChargerStatusBySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        Page<ChargerStatusInfoDto> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) {
            resultSet.setResult(new ArrayList<>(0));
        }else {
            List<ChargerStatusInfoDto> result = mapper.selectChargerStatusBySearchCond(searchCond);
            resultSet.setResult(result);
        }
        return resultSet;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void removeChargerStatusByCpIdNCsId(String cpId, String csId){
        //
        mapper.deleteChargerStatusByCpIdNCsId(cpId, csId);
    }

}
