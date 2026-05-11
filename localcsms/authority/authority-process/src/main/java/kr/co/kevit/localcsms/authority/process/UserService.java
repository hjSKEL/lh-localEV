/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.process;

import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.entity.shared.UserInfoDto;
import kr.co.kevit.localcsms.authority.entity.shared.UserSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 12. 27.
 */
public interface UserService {
	
    User retrieveUserByIdNType(String loginId, String type);
    
    /**
     * 
     * @param loginId
     * @return
     */
    User retrieveUserById(String loginId);

    /**
     * 
     * @param user
     * @return
     */
    boolean modifyUser(User user);

    /**
     * 사용자아이디(USER_ID)에 해당하는 로그인 정보가 없으면 생성 있으면 수정
     * @param user
     * @return
     */
    boolean saveUser(User user);


    Page<UserInfoDto> retrieveUserInfoDtoBySearchCond(UserSearchCond searchCond);
    
    void modifyPassWord(String loginId, String newPassword);
}
