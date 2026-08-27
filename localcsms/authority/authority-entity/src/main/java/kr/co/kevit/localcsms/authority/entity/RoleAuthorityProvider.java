/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity;

import kr.co.kevit.localcsms.authority.entity.domain.RoleAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
@Component
public interface RoleAuthorityProvider {

    public List<RoleAuthority> retrieveRoleAuthorityByRole(String roleType);

    public int retrieveCountChildRoleByParentRole(RoleAuthority roleAuthority);

    public boolean registerRoleAuthority(RoleAuthority roleAuthority);

    public boolean modifyRoleAuthority(RoleAuthority roleAuthority);

    public boolean removeRoleAuthorityByRoleType(String roleType);

    public boolean removeRoleAuthorityByRoleTypeAndMenu(RoleAuthority roleAuthority);
}
