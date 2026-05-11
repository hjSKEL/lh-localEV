/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.dao;

import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.entity.shared.UserSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
@Repository
public interface UserMapper {

    /**
     * 
     * @param userId
     * @param type
     * @return
     */
    User selectUserByIdNType(@Param("loginId") String loginId, @Param("userType") String type);
    
    /**
     * 
     * @param loginId
     * @return
     */
    User selectUserById(@Param("loginId") String loginId);

    /**
     *
     * @param userId
     * @return
     */
    User selectUserByUserId(@Param("userId") String userId);

    /**
     * 
     * @param user
     * @return
     */
    int updateUser(@Param("user") User user);

    /**
     *
     * @param user
     * @return
     */
    int updateUserAll(@Param("user") User user);
    
    /**
     * 
     * @param loginId
     * @return
     */
    int deleteUser(@Param("loginId") String loginId);

    /**
     *
     * @param user
     * @return
     */
    int insertUser(@Param("user") User user);

    int countUserBySearchCond(@Param("searchCond") UserSearchCond searchCond);

    List<User> selectUserBySearchCond(@Param("searchCond") UserSearchCond searchCond);
}
