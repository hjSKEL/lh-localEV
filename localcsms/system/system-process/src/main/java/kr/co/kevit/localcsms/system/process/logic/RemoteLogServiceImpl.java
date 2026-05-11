/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 ******************************************************************************/
package kr.co.kevit.localcsms.system.process.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.system.entity.RemoteLogProvider;
import kr.co.kevit.localcsms.system.entity.domain.RemoteLog;
import kr.co.kevit.localcsms.system.entity.shared.RemoteLogSearchCond;
import kr.co.kevit.localcsms.system.process.RemoteLogService;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
@Service
@Transactional
public class RemoteLogServiceImpl implements RemoteLogService {

    @Autowired
    private RemoteLogProvider provider;

    @Override
    public void registerRemoteLog(RemoteLog remoteLog) {
        provider.registerRemoteLog(remoteLog);
    }

    @Override
    public void modifyRemoteLog(RemoteLog remoteLog) {
        provider.modifyRemoteLog(remoteLog);
    }

    @Override
    public void removeRemoteLog(String uuid) {
        provider.removeRemoteLog(uuid);
    }

    @Transactional(readOnly = true)
    @Override
    public RemoteLog retrieveRemoteLogByUuid(String uuid) {
        return provider.retrieveRemoteLogByUuid(uuid);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RemoteLog> retrieveAllRemoteLog() {
        return provider.retrieveAllRemoteLog();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RemoteLog> retrieveRemoteLogBySearchCond(RemoteLogSearchCond searchCond) {
        return provider.retrieveRemoteLogBySearchCond(searchCond);
    }

}
