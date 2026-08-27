/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.process;

import kr.co.kevit.localcsms.authority.entity.domain.RoleAuthority;

import java.util.List;

/**
 *
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
public interface RoleAuthorityService {

    List<RoleAuthority> retrieveRoleAuthorityByRole(String roleType);

    boolean registerRoleAuthority(RoleAuthority roleAuthority);

    boolean modifyRoleAuthority(RoleAuthority roleAuthority);

    boolean removeRoleAuthorityByRoleType(String roleType);

    boolean changeRoleAuthority(RoleAuthority roleAuthority, String checked);
}
