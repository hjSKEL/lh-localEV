/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.dao;

import kr.co.kevit.localcsms.authority.entity.domain.RoleAuthority;
import kr.co.kevit.localcsms.authority.entity.domain.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
@Repository
public interface RoleAuthorityMapper {

    List<UserRole> selectUserRoleByUserId(@Param("loginId") String loginId);

    List<RoleAuthority> selectRoleAuthorityByRole(@Param("roleType") String roleType);

    List<RoleAuthority> selectRoleAuthorityByRoleAndMenu(@Param("roleAuthority") RoleAuthority roleAuthority);

    int countChildRoleByParentRole(@Param("roleAuthority") RoleAuthority roleAuthority);

    int insertUserRole(@Param("userRole") UserRole userRole);

    int updateUserRole(@Param("userRole") UserRole userRole);

    boolean deleteUserRoleByUserId(@Param("loginId") String loginId);

    int insertRoleAuthority(@Param("roleAuthority") RoleAuthority roleAuthority);

    int updateRoleAuthority(@Param("roleAuthority") RoleAuthority roleAuthority);

    int deleteRoleAuthorityByRoleType(@Param("roleType") String roleType);

    boolean deleteRoleAuthorityByRoleAndMenu(@Param("roleAuthority") RoleAuthority roleAuthority);

}
