/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 7. 30.
 */
public class AbstractResource {
    
    public String getAccessIp(HttpServletRequest request) {
        String userIPAddress = request.getHeader("X-Forwarded-For");
        if (userIPAddress == null) {
            userIPAddress = request.getHeader("X-FORWARDED-FOR");
        }
        if (userIPAddress == null) {
            userIPAddress = request.getRemoteAddr();
        }
        return userIPAddress;
    }
}
