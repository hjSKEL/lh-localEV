/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.system.entity.ConnConfigProvider;
import kr.co.kevit.localcsms.system.entity.dao.ConnConfigMapper;
import kr.co.kevit.localcsms.system.entity.domain.ConnConfig;

/**
 * @since 2026. 9. 2.
 */
@Component
public class ConnConfigProviderImpl implements ConnConfigProvider {

    @Autowired
    private ConnConfigMapper mapper;

    @Override
    public ConnConfig retrieveConnConfig() {
        return mapper.selectConnConfig();
    }

    @Override
    public void modifyConnConfig(ConnConfig connConfig) {
        mapper.updateConnConfig(connConfig);
    }

}
