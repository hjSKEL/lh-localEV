/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.system.entity.domain.DaemonAccess;
import kr.co.kevit.localcsms.system.entity.shared.DaemonAccessSearchCond;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
@Repository
public interface DaemonAccessMapper {

    int insertDaemonAccess(@Param("daemonAccess") DaemonAccess daemonAccess);

    int updateDaemonAccess(@Param("daemonAccess") DaemonAccess daemonAccess);

    int deleteDaemonAccess(@Param("cpCsId") String cpCsId);

    DaemonAccess selectDaemonAccessByCpCsId(@Param("cpCsId") String cpCsId);

    List<DaemonAccess> selectAllDaemonAccess();

    int countDaemonAccessBySearchCond(@Param("searchCond") DaemonAccessSearchCond searchCond);

    List<DaemonAccess> selectDaemonAccessBySearchCond(@Param("searchCond") DaemonAccessSearchCond searchCond);

}
