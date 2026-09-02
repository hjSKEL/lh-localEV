/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.process;

import kr.co.kevit.localcsms.system.entity.domain.ConnConfig;

/**
 * @since 2026. 9. 2.
 */
public interface ConnConfigService {

    ConnConfig retrieveConnConfig();

    void modifyConnConfig(ConnConfig connConfig);

}
