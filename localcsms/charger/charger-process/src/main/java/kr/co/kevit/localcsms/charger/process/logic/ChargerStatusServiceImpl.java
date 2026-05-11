/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.process.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.charger.entity.ChargerStatusProvider;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
@Service
@Transactional
public class ChargerStatusServiceImpl implements ChargerStatusService {

    @Autowired
    private ChargerStatusProvider provider;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<ChargerStatusInfo> retrieveChargerStatusByCpIdNCsId(String cpId, String csId) {
        // 
        return provider.retrieveChargerStatusByCpIdNCsId(cpId, csId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyChargerStatus(ChargerStatusInfo chargerStatusInfo) {
        // 
        provider.modifyChargerStatus(chargerStatusInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyChargerStatusWithoutHis(ChargerStatusInfo chargerStatusInfo) {
        // 
        provider.modifyChargerStatusWithoutHis(chargerStatusInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<ChargerStatusInfoDto> retrieveChargerStatusBySearchCond(ChargerStatusSearchCond searchCond) {
        // 
        return provider.retrieveChargerStatusBySearchCond(searchCond);
    }

}
