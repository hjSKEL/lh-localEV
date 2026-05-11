/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 ******************************************************************************/
package kr.co.kevit.localcsms.system.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.system.entity.RemoteLogProvider;
import kr.co.kevit.localcsms.system.entity.dao.RemoteLogMapper;
import kr.co.kevit.localcsms.system.entity.domain.RemoteLog;
import kr.co.kevit.localcsms.system.entity.shared.RemoteLogSearchCond;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
@Component
public class RemoteLogProviderImpl implements RemoteLogProvider {

    @Autowired
    private RemoteLogMapper mapper;

    @Override
    public void registerRemoteLog(RemoteLog remoteLog) {
        mapper.insertRemoteLog(remoteLog);
    }

    @Override
    public void modifyRemoteLog(RemoteLog remoteLog) {
        mapper.updateRemoteLog(remoteLog);
    }

    @Override
    public void removeRemoteLog(String uuid) {
        mapper.deleteRemoteLog(uuid);
    }

    @Override
    public RemoteLog retrieveRemoteLogByUuid(String uuid) {
        return mapper.selectRemoteLogByUuid(uuid);
    }

    @Override
    public List<RemoteLog> retrieveAllRemoteLog() {
        return mapper.selectAllRemoteLog();
    }

    @Override
    public Page<RemoteLog> retrieveRemoteLogBySearchCond(RemoteLogSearchCond searchCond) {
        int totalItemCount = mapper.countRemoteLogBySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        Page<RemoteLog> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) {
            return resultSet;
        }
        List<RemoteLog> result = mapper.selectRemoteLogBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

}
