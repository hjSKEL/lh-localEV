/*******************************************************************************
 * Copyright(c) 2019 AEA All rights reserved.
 * This software is the proprietary information of AEA.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.logic;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.entity.AccessLogProvider;
import kr.co.kevit.localcsms.common.entity.dao.AccessLogMapper;
import kr.co.kevit.localcsms.common.shared.AccessLogSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 10. 18.
 */
@Component
public class AccessLogProviderImpl implements AccessLogProvider {

    @Autowired
    private AccessLogMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerAccessLog(AccessLog accessLog) {
        //
        accessLog.setSeq(mapper.getAccessLogSequence());
        mapper.insertAccessLog(accessLog);
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<AccessLog> retrieveAccessLogBySearchCond(AccessLogSearchCond searchCond) {
        // 
        Page<AccessLog> resultSet = new Page<>();
        searchCond.setTotalItemCount(mapper.countAccessLogBySearchCond(searchCond));
        resultSet.setCriteria(searchCond);
        if(searchCond.getTotalItemCount() > 0) {
            resultSet.setResult(mapper.selectAccessLogBySearchCond(searchCond));
        }else {
            resultSet.setResult(new ArrayList<>(0));
        }
        return resultSet;
    }
}
