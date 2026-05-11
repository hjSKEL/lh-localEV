/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 ******************************************************************************/
package kr.co.kevit.localcsms.system.entity;

import java.util.List;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.system.entity.domain.RemoteLog;
import kr.co.kevit.localcsms.system.entity.shared.RemoteLogSearchCond;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
public interface RemoteLogProvider {

    void registerRemoteLog(RemoteLog remoteLog);

    void modifyRemoteLog(RemoteLog remoteLog);

    void removeRemoteLog(String uuid);

    RemoteLog retrieveRemoteLogByUuid(String uuid);

    List<RemoteLog> retrieveAllRemoteLog();

    Page<RemoteLog> retrieveRemoteLogBySearchCond(RemoteLogSearchCond searchCond);

}
