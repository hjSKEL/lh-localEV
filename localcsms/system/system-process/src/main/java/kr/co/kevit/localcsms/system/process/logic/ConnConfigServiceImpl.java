/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.process.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kevit.localcsms.system.entity.ConnConfigProvider;
import kr.co.kevit.localcsms.system.entity.domain.ConnConfig;
import kr.co.kevit.localcsms.system.process.ConnConfigService;

/**
 * @since 2026. 9. 2.
 */
@Service
@Transactional
public class ConnConfigServiceImpl implements ConnConfigService {

    @Autowired
    private ConnConfigProvider provider;

    @Transactional(readOnly = true)
    @Override
    public ConnConfig retrieveConnConfig() {
        return provider.retrieveConnConfig();
    }

    @Override
    public void modifyConnConfig(ConnConfig connConfig) {
        provider.modifyConnConfig(connConfig);
    }

}
