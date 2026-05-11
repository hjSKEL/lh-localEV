/*******************************************************************************
 * Copyright(c) 2019 AEA All rights reserved.
 * This software is the proprietary information of AEA.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.entity.AccessLogProvider;
import kr.co.kevit.localcsms.common.process.AccessLogService;
import kr.co.kevit.localcsms.common.shared.AccessLogSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 10. 18.
 */
@Service
@Transactional
public class AccessLogServiceImpl implements AccessLogService {

    @Autowired
    private AccessLogProvider provider;

    @Override
    public void registerAccessLog(AccessLog accessLog) {
        provider.registerAccessLog(accessLog);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<AccessLog> retrieveAccessLogBySearchCond(AccessLogSearchCond searchCond) {
        // 
        return provider.retrieveAccessLogBySearchCond(searchCond);
    }
}
