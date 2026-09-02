/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity;

import kr.co.kevit.localcsms.system.entity.domain.SystemConfig;

/**
 * @since 2026. 9. 2.
 */
public interface SystemConfigProvider {

    SystemConfig retrieveSystemConfig();

    void modifySystemConfig(SystemConfig systemConfig);

}
