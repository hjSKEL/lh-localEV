/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.logic;

import kr.co.kevit.localcsms.authority.entity.MenuProvider;
import kr.co.kevit.localcsms.authority.entity.dao.MenuMapper;
import kr.co.kevit.localcsms.authority.entity.domain.Menu;
import kr.co.kevit.localcsms.authority.entity.shared.MenuDto;
import kr.co.kevit.localcsms.authority.entity.shared.MenuSearchCond;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
@Component
public class MenuProviderImpl implements MenuProvider {

    @Autowired
    private MenuMapper mapper;

    @Override
    public void registerMenu(Menu menu) {
        mapper.insertMenu(menu);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Menu retrieveMenuByMenuId(String menuId) {
        //
        return mapper.selectMenuByMenuId(menuId);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<Menu> retrieveMenuByRoleType(List<UserRoleType> roleType) {
        //
        return mapper.selectMenuByRoleType(roleType);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public List<Menu> retrieveMenuByMenuSearchCond(MenuSearchCond searchCond) {
        //
        return mapper.selectMenuByMenuSearchCond(searchCond);
    }

    @Override
    public List<MenuDto> retrieveAllChildMenu() {
        //
        return mapper.selectAllChildMenu();
    }

    @Override
    public Page<MenuDto> retrieveMenuAllByCondition(MenuSearchCond searchCond) {
        //
        int totalItemCount = mapper.countMenuAllByCondition(searchCond);

        searchCond.setTotalItemCount(totalItemCount);
        Page<MenuDto> resultSet = new Page<MenuDto>();
        resultSet.setCriteria(searchCond);
        if (totalItemCount == 0)
            return resultSet;
        List<MenuDto> result = mapper.selectMenuAllByCondition(searchCond);
        resultSet.setResult(result);
        return resultSet;
    }

    @Override
    public void modifyMenu(String menuId, Menu menu) {
        //
        Map<String, Object> map = new HashMap<String, Object>(); // MAP을 이용해 담기
        map.put("menuId", menuId);
        map.put("menu", menu);

        mapper.updateMenu(map);
    }

    @Override
    public void removeMenu(String menuId) {
        //
        mapper.deleteMenu(menuId);
    }
}
