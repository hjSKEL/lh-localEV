/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.logic;

import kr.co.kevit.localcsms.authority.entity.RoleAuthorityProvider;
import kr.co.kevit.localcsms.authority.entity.dao.RoleAuthorityMapper;
import kr.co.kevit.localcsms.authority.entity.domain.RoleAuthority;
import kr.co.kevit.localcsms.authority.entity.domain.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 12. 18.
 */
@Component
public class RoleAuthorityProviderImpl implements RoleAuthorityProvider {

	@Autowired
	private RoleAuthorityMapper mapper;
	
	/* (non-Javadoc)
	 */
	@Override
	public List<UserRole> retrieveUserRoleByUserId(String userId) {
		// 
		return mapper.selectUserRoleByUserId(userId);
	}

	/* (non-Javadoc)
	 */
	@Override
	public List<RoleAuthority> retrieveRoleAuthorityByRole(String roleType) {
		// 
		return mapper.selectRoleAuthorityByRole(roleType);
	}

	/* (non-Javadoc)
	 */
	@Override
	public List<UserRole> retrieveUserRoleWithRoleAuthorityByUserId(String userId) {
		// 
		List<UserRole> result = mapper.selectUserRoleByUserId(userId);
		for(UserRole role : result){
			role.setAuthority(mapper.selectRoleAuthorityByRole(role.getRoleType().getCode()));
		}
		return result;
	}

	@Override
	public int retrieveCountChildRoleByParentRole(RoleAuthority roleAuthority) {
		return mapper.countChildRoleByParentRole(roleAuthority);
	}

	/* (non-Javadoc)
	 */
	@Override
	public boolean registerUserRole(UserRole userRole) {
		// 
		int result = mapper.insertUserRole(userRole);
		return result == 1;
	}

	/* (non-Javadoc)
	 */
	@Override
	public boolean modifyUserRole(UserRole userRole) {
		// 
		int result = mapper.updateUserRole(userRole);
		return result == 1;
	}

	/* (non-Javadoc)
	 */
	@Override
	public boolean removeUserRoleByUserId(String userId) {
		// 
		mapper.deleteUserRoleByUserId(userId);
		return false;
	}

	/* (non-Javadoc)
	 */
	@Override
	public boolean registerRoleAuthority(RoleAuthority roleAuthority) {
		//
		List<RoleAuthority> roleAuthorities = mapper.selectRoleAuthorityByRoleAndMenu(roleAuthority);
		if (roleAuthorities.size() != 0) {
			return false;
		}

		int result = mapper.insertRoleAuthority(roleAuthority);
		return result == 1;
	}

	/* (non-Javadoc)
	 */
	@Override
	public boolean modifyRoleAuthority(RoleAuthority roleAuthority) {
		// 
		int result = mapper.updateRoleAuthority(roleAuthority);
		return result == 1;
	}

	/* (non-Javadoc)
	 */
	@Override
	public boolean removeRoleAuthorityByRoleType(String roleType) {
		// 
		mapper.deleteRoleAuthorityByRoleType(roleType);
		return false;
	}

	@Override
	public boolean removeRoleAuthorityByRoleTypeAndMenu(RoleAuthority roleAuthority) {
		//
		return mapper.deleteRoleAuthorityByRoleAndMenu(roleAuthority);
	}
}
