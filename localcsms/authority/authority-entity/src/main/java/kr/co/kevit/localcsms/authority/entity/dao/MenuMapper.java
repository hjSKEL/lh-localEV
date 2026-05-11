/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.dao;

import kr.co.kevit.localcsms.authority.entity.domain.Menu;
import kr.co.kevit.localcsms.authority.entity.shared.MenuDto;
import kr.co.kevit.localcsms.authority.entity.shared.MenuSearchCond;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
@Repository
public interface MenuMapper {

    List<Menu> selectMenuByMenuSearchCond(@Param("searchCond") MenuSearchCond searchCond);

    List<MenuDto> selectMenuAllByCondition(@Param("searchCond") MenuSearchCond searchCond);

    int countMenuAllByCondition(@Param("searchCond") MenuSearchCond searchCond);

    Menu selectMenuByMenuId(@Param("menuId") String menuId);

    List<Menu> selectMenuByRoleType(@Param("roleType") List<UserRoleType> roleType);

    int insertMenu(@Param("menu") Menu menu);

    int updateMenu(@Param("map") Map menuMap);

    int deleteMenu(@Param("menuId") String menuId);

    List<MenuDto> selectAllChildMenu();
}
