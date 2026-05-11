/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity;

import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.entity.shared.UserSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 18.
 */
public interface UserProvider {

    User retrieveUserByIdNType(String loginId, String type);
    
    /**
     * 
     * @param loginId
     * @return
     */
    User retrieveUserById(String loginId);

    /**
     *
     * @param userId
     * @return
     */
    User retrieveUserByUserId(String userId);

    /**
     * 
     * @param user
     * @return
     */
    boolean modifyUser(User user);

    /**
     *
     * @param user
     * @return
     */
    boolean modifyUserAll(User user);

    /**
     *
     * @param user
     * @return
     */
    boolean registerUser(User user);

    /**
     *
     * @param searchCond
     * @return
     */
    Page<User> retrieveUserBySearchCond(UserSearchCond searchCond);
    
    void removeUser(String loginId);
}
