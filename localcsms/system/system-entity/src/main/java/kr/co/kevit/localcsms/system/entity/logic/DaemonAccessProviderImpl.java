/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.system.entity.DaemonAccessProvider;
import kr.co.kevit.localcsms.system.entity.dao.DaemonAccessMapper;
import kr.co.kevit.localcsms.system.entity.domain.DaemonAccess;
import kr.co.kevit.localcsms.system.entity.shared.DaemonAccessSearchCond;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
@Component
public class DaemonAccessProviderImpl implements DaemonAccessProvider {

    @Autowired
    private DaemonAccessMapper mapper;

    @Override
    public void registerDaemonAccess(DaemonAccess daemonAccess) {
        mapper.insertDaemonAccess(daemonAccess);
    }

    @Override
    public void modifyDaemonAccess(DaemonAccess daemonAccess) {
        mapper.updateDaemonAccess(daemonAccess);
    }

    @Override
    public void removeDaemonAccess(String cpCsId) {
        mapper.deleteDaemonAccess(cpCsId);
    }

    @Override
    public DaemonAccess retrieveDaemonAccessByCpCsId(String cpCsId) {
        return mapper.selectDaemonAccessByCpCsId(cpCsId);
    }

    @Override
    public List<DaemonAccess> retrieveAllDaemonAccess() {
        return mapper.selectAllDaemonAccess();
    }

    @Override
    public Page<DaemonAccess> retrieveDaemonAccessBySearchCond(DaemonAccessSearchCond searchCond) {
        int totalItemCount = mapper.countDaemonAccessBySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        Page<DaemonAccess> resultSet = new Page<>();
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0) {
            return resultSet;
        }
        List<DaemonAccess> result = mapper.selectDaemonAccessBySearchCond(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

}
