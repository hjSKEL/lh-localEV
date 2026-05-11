/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 ******************************************************************************/
package kr.co.kevit.localcsms.system.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.system.entity.domain.RemoteLog;
import kr.co.kevit.localcsms.system.entity.shared.RemoteLogSearchCond;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 3. 17.
 */
@Repository
public interface RemoteLogMapper {

    int insertRemoteLog(@Param("remoteLog") RemoteLog remoteLog);

    int updateRemoteLog(@Param("remoteLog") RemoteLog remoteLog);

    int deleteRemoteLog(@Param("uuid") String uuid);

    RemoteLog selectRemoteLogByUuid(@Param("uuid") String uuid);

    List<RemoteLog> selectAllRemoteLog();

    int countRemoteLogBySearchCond(@Param("searchCond") RemoteLogSearchCond searchCond);

    List<RemoteLog> selectRemoteLogBySearchCond(@Param("searchCond") RemoteLogSearchCond searchCond);

}
