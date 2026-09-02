/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.system.entity.domain.SystemConfig;

/**
 * @since 2026. 9. 2.
 */
@Repository
public interface SystemConfigMapper {

    SystemConfig selectSystemConfig();

    int updateSystemConfig(@Param("systemConfig") SystemConfig systemConfig);

}
