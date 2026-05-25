/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.derctrl.entity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.derctrl.entity.domain.DerControlHis;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlHisDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlHisSearchCond;

@Repository
public interface DerControlHisMapper {

    int insertHis(@Param("his") DerControlHis his);

    int countHisBySearchCond(@Param("searchCond") DerControlHisSearchCond cond);

    List<DerControlHisDto> selectHisBySearchCond(@Param("searchCond") DerControlHisSearchCond cond);
}
