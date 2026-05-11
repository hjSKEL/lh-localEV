/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.system.entity;

import java.util.List;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.system.entity.domain.DaemonAccess;
import kr.co.kevit.localcsms.system.entity.shared.DaemonAccessSearchCond;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
public interface DaemonAccessProvider {

    void registerDaemonAccess(DaemonAccess daemonAccess);

    void modifyDaemonAccess(DaemonAccess daemonAccess);

    void removeDaemonAccess(String cpCsId);

    DaemonAccess retrieveDaemonAccessByCpCsId(String cpCsId);

    List<DaemonAccess> retrieveAllDaemonAccess();

    Page<DaemonAccess> retrieveDaemonAccessBySearchCond(DaemonAccessSearchCond searchCond);

}
