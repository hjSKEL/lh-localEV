/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.process.logic;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.OcppLogProvider;
import kr.co.kevit.localcsms.recharger.entity.domain.OcppLog;
import kr.co.kevit.localcsms.recharger.entity.shared.OcppLogSearchCond;
import kr.co.kevit.localcsms.recharger.process.OcppLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 2.
 */
@Service
@Transactional
public class OcppLogServiceImpl implements OcppLogService {
    
    @Autowired
    private OcppLogProvider provider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerOcppLog(OcppLog log) {
        // 
        provider.registerOcppLog(log);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<OcppLog> retrieveOcppLogByOcppLogSearchCond(OcppLogSearchCond searchCond) {
        // 
        return provider.retrieveOcppLogByOcppLogSearchCond(searchCond);
    }

}
