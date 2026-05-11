/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity;

import kr.co.kevit.localcsms.authority.entity.domain.Menu;
import kr.co.kevit.localcsms.authority.entity.shared.MenuDto;
import kr.co.kevit.localcsms.authority.entity.shared.MenuSearchCond;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
@Component
public interface MenuProvider {

    public List<Menu> retrieveMenuByMenuSearchCond(MenuSearchCond searchCond);

    public Menu retrieveMenuByMenuId(String menuId);

    public List<Menu> retrieveMenuByRoleType(List<UserRoleType> roleType);

    public List<MenuDto> retrieveAllChildMenu();

    public Page<MenuDto> retrieveMenuAllByCondition(MenuSearchCond searchCond);

    void modifyMenu(String menuId, Menu menu);

    void removeMenu(String menuId);

    void registerMenu(Menu menu);
}
