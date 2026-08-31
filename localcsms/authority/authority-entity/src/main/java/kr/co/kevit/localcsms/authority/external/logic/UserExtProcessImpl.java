/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.external.logic;

import kr.co.kevit.localcsms.authority.entity.dao.RoleAuthorityMapper;
import kr.co.kevit.localcsms.authority.entity.dao.UserMapper;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.entity.domain.UserRole;
import kr.co.kevit.localcsms.authority.external.UserExtProcess;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 5. 7.
 */
@Component
public class UserExtProcessImpl implements UserExtProcess {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;

    @Override
    public User retrieveUserByUserId(String userId) {
        //
        return userMapper.selectUserByUserId(userId);
    }

    @Override public boolean saveUser(User user) {
        //
        boolean result = false;
        User existUser = userMapper.selectUserByUserId(user.getUserId());

        if (existUser != null) {
            // 존재하면 수정
            if (StringUtils.isNoneEmpty(user.getLoginId())) {
                existUser.setLoginId(user.getLoginId());
            }
            if (StringUtils.isNoneEmpty(user.getUserPwd())) {
                existUser.setUserPwd(user.getUserPwd());
                existUser.setPwUpdateDate(user.getWriter().getUpdateDate());
            }
            existUser.setWriter(user.getWriter());
            result = userMapper.updateUserAll(existUser) == 1;
        } else {
            // 미존재 시 등록
            result = userMapper.insertUser(user) == 1;
        }
        // 관리자 또는 운영자가 아닌경우
        // 기존 사용자/역할 매핑 제거
        // TODO: RoleAuthorityMapper에 deleteUserRoleByUserId/insertUserRole 메서드 없음 - 컴파일 에러로 임시 주석 처리
        // roleAuthorityMapper.deleteUserRoleByUserId(user.getLoginId());
        // 현재기준 역할 등록
        // roleAuthorityMapper.insertUserRole(new UserRole(user.getLoginId(), UserRoleType.USER));
        return result;
    }

    @Override
    public void removeUser(String loginId) {
        //
        userMapper.deleteUser(loginId);
        // TODO: RoleAuthorityMapper에 deleteUserRoleByUserId 메서드 없음 - 컴파일 에러로 임시 주석 처리
        // roleAuthorityMapper.deleteUserRoleByUserId(loginId);
    }
}
