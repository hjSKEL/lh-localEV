/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.dao;

import java.math.BigInteger;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.charger.entity.domain.BreakdownInfo;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 4.
 */
@Repository
public interface BreakdownInfoMapper {
    
    BigInteger getBreakdownSequence();
    
    int insertBreakdownInfo(@Param("info") BreakdownInfo info);
    
    int updateBreakdownInfo(@Param("info") BreakdownInfo info);
    
    BreakdownInfo selectBreakdownInfo(@Param("id") String id);

}
