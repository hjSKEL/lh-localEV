/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.external;

import kr.co.kevit.localcsms.authority.entity.domain.User;

/**
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 5. 7.
 */
public interface UserExtProcess {

    User retrieveUserByUserId(String userId);

    boolean saveUser(User user);

    void removeUser(String loginId);
}
