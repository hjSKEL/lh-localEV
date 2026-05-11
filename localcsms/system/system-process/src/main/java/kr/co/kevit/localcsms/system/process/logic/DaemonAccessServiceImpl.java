/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.process.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.system.entity.DaemonAccessProvider;
import kr.co.kevit.localcsms.system.entity.domain.DaemonAccess;
import kr.co.kevit.localcsms.system.entity.shared.DaemonAccessSearchCond;
import kr.co.kevit.localcsms.system.process.DaemonAccessService;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
@Service
@Transactional
public class DaemonAccessServiceImpl implements DaemonAccessService {

    @Autowired
    private DaemonAccessProvider provider;

    @Override
    public void registerDaemonAccess(DaemonAccess daemonAccess) {
        provider.registerDaemonAccess(daemonAccess);
    }

    @Override
    public void modifyDaemonAccess(DaemonAccess daemonAccess) {
        provider.modifyDaemonAccess(daemonAccess);
    }

    @Override
    public void removeDaemonAccess(String cpCsId) {
        provider.removeDaemonAccess(cpCsId);
    }

    @Override
    public void registerOrModifyDaemonAccess(DaemonAccess daemonAccess) {
        DaemonAccess existing = provider.retrieveDaemonAccessByCpCsId(daemonAccess.getCpCsId());
        if (existing != null) {
            provider.modifyDaemonAccess(daemonAccess);
        } else {
            provider.registerDaemonAccess(daemonAccess);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public DaemonAccess retrieveDaemonAccessByCpCsId(String cpCsId) {
        return provider.retrieveDaemonAccessByCpCsId(cpCsId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DaemonAccess> retrieveAllDaemonAccess() {
        return provider.retrieveAllDaemonAccess();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DaemonAccess> retrieveDaemonAccessBySearchCond(DaemonAccessSearchCond searchCond) {
        return provider.retrieveDaemonAccessBySearchCond(searchCond);
    }

}
