/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;

import kr.co.kevit.localcsms.authority.entity.domain.User;


/**
 * 
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 10. 15.
 */
public class SessionManager {
    
    private static Map<String, HttpSession> sessionMap = new HashMap<>();

    public static User getLoginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        User loginUser = null;
        if(principal != null && principal instanceof User) {
            loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            loginUser.setUserPwd(null);
            loginUser.setSalt(null);
        }
        return loginUser;
    }
    
    public static HttpSession getSession(String userId) {
        return sessionMap.get(userId);
    }
    
    public static void setSession(String userId, HttpSession session) {
        sessionMap.put(userId, session);
    }

    public static void removeSession(String userId) {
        sessionMap.remove(userId);
    }

}
