/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.charger.entity.domain.BreakdownRepairInfo;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
@Repository
public interface BreakdownRepairInfoMapper {
    
    int insertBreakdownRepairInfo(@Param("info")BreakdownRepairInfo info);
    
    int updateBreakdownRepairInfo(@Param("info")BreakdownRepairInfo info);
    
    BreakdownRepairInfo selectBreakdownRepairInfo(@Param("id")String id);

}
