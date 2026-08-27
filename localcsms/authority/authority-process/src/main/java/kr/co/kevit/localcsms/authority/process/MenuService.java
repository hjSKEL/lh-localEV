/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.process;

import kr.co.kevit.localcsms.authority.entity.domain.Menu;
import kr.co.kevit.localcsms.authority.entity.shared.MenuDto;
import kr.co.kevit.localcsms.authority.entity.shared.MenuSearchCond;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
public interface MenuService {

    List<Menu> retrieveMenuByParentId(String parentId);

    Menu retrieveMenuByMenuId(String menuId);

    List<Menu> retrieveMenuByRoleType(List<UserRoleType> roleType);

    /**
     * 역할-메뉴 권한표(TB_SYME002)를 거치지 않고 전체 메뉴를 반환 - ROOT_ADMIN(모든 권한) 전용.
     */
    List<Menu> retrieveAllMenu();

    List<MenuDto> retrieveAllChildMenu();

    Page<MenuDto> retrieveMenuAllByCondition(MenuSearchCond searchCond);

    void modifyMenu(String menuId, Menu menu);

    void removeMenu(String menuId);

    void registerMenu(MenuDto menuDto);
}
