/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.process.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.system.entity.SystemConfigProvider;
import kr.co.kevit.localcsms.system.entity.domain.SystemConfig;
import kr.co.kevit.localcsms.system.process.SystemConfigService;

/**
 * @since 2026. 9. 2.
 */
@Service
@Transactional
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    private SystemConfigProvider provider;

    @Transactional(readOnly = true)
    @Override
    public SystemConfig retrieveSystemConfig() {
        return provider.retrieveSystemConfig();
    }

    @Override
    public void modifySystemConfig(SystemConfig systemConfig) {
        provider.modifySystemConfig(systemConfig);
    }

}
