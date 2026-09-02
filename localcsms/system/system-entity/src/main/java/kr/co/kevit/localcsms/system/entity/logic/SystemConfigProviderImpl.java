/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.system.entity.SystemConfigProvider;
import kr.co.kevit.localcsms.system.entity.dao.SystemConfigMapper;
import kr.co.kevit.localcsms.system.entity.domain.SystemConfig;

/**
 * @since 2026. 9. 2.
 */
@Component
public class SystemConfigProviderImpl implements SystemConfigProvider {

    @Autowired
    private SystemConfigMapper mapper;

    @Override
    public SystemConfig retrieveSystemConfig() {
        return mapper.selectSystemConfig();
    }

    @Override
    public void modifySystemConfig(SystemConfig systemConfig) {
        mapper.updateSystemConfig(systemConfig);
    }

}
