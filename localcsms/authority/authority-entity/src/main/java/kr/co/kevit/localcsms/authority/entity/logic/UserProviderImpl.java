/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.logic;

import kr.co.kevit.localcsms.authority.entity.UserProvider;
import kr.co.kevit.localcsms.authority.entity.dao.RoleAuthorityMapper;
import kr.co.kevit.localcsms.authority.entity.dao.UserMapper;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.entity.shared.UserSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 27.
 */
@Component
public class UserProviderImpl implements UserProvider {

    @Autowired
    private UserMapper mapper;
    
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public User retrieveUserByIdNType(String loginId, String type) {
        // 
        return mapper.selectUserByIdNType(loginId, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User retrieveUserById(String loginId) {
        // 
        return mapper.selectUserById(loginId);
    }

    @Override
    public User retrieveUserByUserId(String userId) {
        //
        return mapper.selectUserByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean modifyUser(User user) {
        // 
        int result = mapper.updateUser(user);
        return result == 1;
    }

    @Override
    public boolean modifyUserAll(User user) {
        int result = mapper.updateUserAll(user);
        return result == 1;
    }

    @Override
    public boolean registerUser(User user) {
        int result = mapper.insertUser(user);
        return result == 1;
    }

    @Override public Page<User> retrieveUserBySearchCond(UserSearchCond searchCond) {
        //
        Page<User> resultSet = new Page<>();
        int totalItemCount = mapper.countUserBySearchCond(searchCond);
        searchCond.setTotalItemCount(totalItemCount);
        resultSet.setCriteria(searchCond);
        if(totalItemCount > 0) {
            resultSet.setResult(mapper.selectUserBySearchCond(searchCond));
        }
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUser(String loginId) {
        // 
        mapper.deleteUser(loginId);
        // TODO: RoleAuthorityMapper에 deleteUserRoleByUserId 메서드 없음 - 컴파일 에러로 임시 주석 처리
        // roleAuthorityMapper.deleteUserRoleByUserId(loginId);
    }
}
