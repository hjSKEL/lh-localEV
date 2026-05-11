/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.process.logic;

import kr.co.kevit.localcsms.authority.entity.MenuProvider;
import kr.co.kevit.localcsms.authority.entity.domain.Menu;
import kr.co.kevit.localcsms.authority.entity.shared.MenuDto;
import kr.co.kevit.localcsms.authority.entity.shared.MenuSearchCond;
import kr.co.kevit.localcsms.authority.process.MenuService;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuProvider provider;

    @Override
    public void registerMenu(MenuDto menu) {
        //
        provider.registerMenu(menu);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Menu> retrieveMenuByParentId(String parentId) {
        //
        MenuSearchCond seachCond = new MenuSearchCond();
        seachCond.setParentMenuIds(new ArrayList<String>(1));
        seachCond.getParentMenuIds().add(parentId);
        return provider.retrieveMenuByMenuSearchCond(seachCond);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Menu retrieveMenuByMenuId(String menuId) {
        //
        return provider.retrieveMenuByMenuId(menuId);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Menu> retrieveMenuByRoleType(List<UserRoleType> roleType) {
        //
        List<Menu> resultSet = provider.retrieveMenuByRoleType(roleType);
        if (resultSet == null || resultSet.size() == 0)
            return resultSet;

        List<Menu> parentMenus = new ArrayList<Menu>();
        for (Menu menu : resultSet) {
            if (StringUtils.isEmpty(menu.getHighMenuId())) {
                parentMenus.add(menu);
            }
        }
        for (Menu menu : resultSet) {
            if (!StringUtils.isEmpty(menu.getHighMenuId())) {
                appendSubMen(menu, parentMenus);
            }
        }
        return parentMenus;
    }

    private void appendSubMen(Menu menu, List<Menu> parentMenus) {
        for (Menu parentMenu : parentMenus) {
            if (parentMenu.getMenuId().equals(menu.getHighMenuId())) {
                parentMenu.addMenu(menu);
            }
        }
    }

    @Transactional(readOnly = true)

    @Override
    public List<MenuDto> retrieveAllChildMenu() {
        //
        return provider.retrieveAllChildMenu();
    }

    @Transactional(readOnly = true)

    @Override
    public Page<MenuDto> retrieveMenuAllByCondition(MenuSearchCond searchCond) {
        //
        return provider.retrieveMenuAllByCondition(searchCond);
    }

    @Override
    public void modifyMenu(String menuId, Menu menu) {
        //
        provider.modifyMenu(menuId, menu);
    }

    @Override
    public void removeMenu(String menuId) {
        //
        provider.removeMenu(menuId);
    }
}
