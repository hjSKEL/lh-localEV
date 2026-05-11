/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.charger.entity.domain.BreakdownMgtInfo;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownMgtInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
@Repository
public interface BreakdownMgtInfoMapper {
    
    int insertBreakdownMgtInfo(@Param("info")BreakdownMgtInfo info);
    
    int updateBreakdownMgtInfo(@Param("info")BreakdownMgtInfo info);
    
    int countBreakdownMgtInfoBySearchCond(@Param("searchCond")BreakdownSearchCond searchCond);
    
    List<BreakdownMgtInfoDto> selectBreakdownMgtInfoBySearchCond(@Param("searchCond")BreakdownSearchCond searchCond);
    
    BreakdownMgtInfoDto selectBreakdownMgtInfoById(@Param("id")String id);

}
