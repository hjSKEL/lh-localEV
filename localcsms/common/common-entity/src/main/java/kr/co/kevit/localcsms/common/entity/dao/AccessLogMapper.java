/*******************************************************************************
 * Copyright(c) 2019 AEA All rights reserved.
 * This software is the proprietary information of AEA.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.shared.AccessLogSearchCond;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 10. 18.
 */
@Repository
public interface AccessLogMapper {
    //
    Long getAccessLogSequence();

    int insertAccessLog(@Param("accessLog") AccessLog accessLog);
    
    int countAccessLogBySearchCond(@Param("searchCond")AccessLogSearchCond searchCond);
    
    List<AccessLog> selectAccessLogBySearchCond(@Param("searchCond")AccessLogSearchCond searchCond);
}
