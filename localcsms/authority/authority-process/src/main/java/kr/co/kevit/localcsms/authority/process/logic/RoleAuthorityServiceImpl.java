/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.process.logic;

import kr.co.kevit.localcsms.authority.entity.MenuProvider;
import kr.co.kevit.localcsms.authority.entity.RoleAuthorityProvider;
import kr.co.kevit.localcsms.authority.entity.domain.Menu;
import kr.co.kevit.localcsms.authority.entity.domain.RoleAuthority;
import kr.co.kevit.localcsms.authority.process.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
@Service
@Transactional
public class RoleAuthorityServiceImpl implements RoleAuthorityService {

    @Autowired
    private RoleAuthorityProvider provider;

    @Autowired
    private MenuProvider menuProvider;

    /**
     *
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<RoleAuthority> retrieveRoleAuthorityByRole(String roleType) {
        //
        return provider.retrieveRoleAuthorityByRole(roleType);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean registerRoleAuthority(RoleAuthority roleAuthority) {
        //
        return provider.registerRoleAuthority(roleAuthority);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean modifyRoleAuthority(RoleAuthority roleAuthority) {
        //
        return provider.modifyRoleAuthority(roleAuthority);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean removeRoleAuthorityByRoleType(String roleType) {
        //
        return provider.removeRoleAuthorityByRoleType(roleType);
    }

    @Override
    public boolean changeRoleAuthority(RoleAuthority roleAuthority, String checked) {
        //
        Menu menu = menuProvider.retrieveMenuByMenuId(roleAuthority.getMenuId());
        RoleAuthority parentRoleAuthority = new RoleAuthority(roleAuthority.getRoleType(), menu.getHighMenuId());

        if (Boolean.valueOf(checked)) {
            // 등록
            // 1.부모메뉴권한 확인(없으면 등록)
            provider.registerRoleAuthority(parentRoleAuthority);
            // 2.자식메뉴권한 등록
            return provider.registerRoleAuthority(roleAuthority);
        } else {
            // 삭제
            // 1. 자식메뉴권한 삭제
            if (provider.removeRoleAuthorityByRoleTypeAndMenu(roleAuthority)) {
                // 2. 부모메뉴권한에 자식메뉴 존재여부 체크(없으면 삭제)
                if (provider.retrieveCountChildRoleByParentRole(parentRoleAuthority) == 0) {
                    provider.removeRoleAuthorityByRoleTypeAndMenu(parentRoleAuthority);
                }
            }
            return true;
        }
    }
}
