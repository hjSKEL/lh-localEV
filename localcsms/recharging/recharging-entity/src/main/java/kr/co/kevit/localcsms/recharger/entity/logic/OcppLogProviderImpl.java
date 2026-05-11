/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.logic;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.OcppLogProvider;
import kr.co.kevit.localcsms.recharger.entity.dao.OcppLogMapper;
import kr.co.kevit.localcsms.recharger.entity.domain.OcppLog;
import kr.co.kevit.localcsms.recharger.entity.shared.OcppLogSearchCond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 2.
 */
@Component
public class OcppLogProviderImpl implements OcppLogProvider {
    
    @Autowired
    private OcppLogMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerOcppLog(OcppLog log) {
        // 
        mapper.insertOcppLog(log);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<OcppLog> retrieveOcppLogByOcppLogSearchCond(OcppLogSearchCond searchCond) {
        //
        Page<OcppLog> resultSet = new Page<>();
        int totalItemCount = mapper.countOcppLogByOcppLogSearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if(totalItemCount == 0)
            return resultSet;
        
        resultSet.setResult(mapper.selectOcppLogByOcppLogSearchCond(searchCond));
        return resultSet;
    }

}
