/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.process.AccessLogService;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 10. 15.
 */
public class WebAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebAuthenticationSuccessHandler.class);

    @Autowired
    private AccessLogService accessLogService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication)
            throws IOException, ServletException {


        String contextPath = req.getContextPath();

        //로그 등록
        String userIPAddress = req.getHeader("X-Forwarded-For");
        if (userIPAddress == null) {
            userIPAddress = req.getHeader("X-FORWARDED-FOR");
        }
        if (userIPAddress == null) {
            userIPAddress = req.getRemoteAddr();
        }
        User loginUser = (User) authentication.getPrincipal();
        AccessLog accessLog = new AccessLog(loginUser.getUserId(), contextPath + "/login", "WEB", userIPAddress);
        accessLogService.registerAccessLog(accessLog);


        User user = (User) authentication.getPrincipal();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("login ... / {}", user.getLoginId());
        }
        HttpSession oldSession = SessionManager.getSession(user.getLoginId());
        if (oldSession != null) {
            try {
                oldSession.invalidate();
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        if (req.getSession() != null) {
            req.getSession().setAttribute("contextPath", req.getContextPath());
            req.getSession().setAttribute("remoteAddr", req.getRemoteAddr());
        }
        SessionManager.setSession(user.getLoginId(), req.getSession());

        // 권한별 메인 페이지 이동
        // 사용자 1명당 권한 1개로 정한다.
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        StringBuilder builder = new StringBuilder(100);
        builder.append("{\"auth\":true,\"url\":\"");
        builder.append(contextPath);

        for (GrantedAuthority grantedAuthority : authorities) {
            String authority = grantedAuthority.getAuthority();

            if (UserRoleType.ADMIN.getCode().equals(authority)) {
                builder.append("/admin/main\"}");
                break;
            } else if (UserRoleType.OPERATION.getCode().equals(authority)) {
                builder.append("/operation/main\"}");
                break;
            }
        }
        res.getWriter().print(builder.toString());
    }

}
