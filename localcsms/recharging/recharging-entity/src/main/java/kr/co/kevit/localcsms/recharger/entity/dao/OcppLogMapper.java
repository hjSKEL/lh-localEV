/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.dao;

import kr.co.kevit.localcsms.recharger.entity.domain.OcppLog;
import kr.co.kevit.localcsms.recharger.entity.shared.OcppLogSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 2.
 */
@Repository
public interface OcppLogMapper {
    
    public int insertOcppLog(@Param("log") OcppLog log);
    
    public int countOcppLogByOcppLogSearchCond(@Param("searchCond") OcppLogSearchCond searchCond);
    
    public List<OcppLog> selectOcppLogByOcppLogSearchCond(@Param("searchCond") OcppLogSearchCond searchCond);

}
