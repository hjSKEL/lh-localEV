/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 10. 15.
 */
public class WebAuthenticationFailHandler implements AuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebAuthenticationFailHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res,
            AuthenticationException authenException) throws IOException, ServletException {

        LOGGER.debug("WebAuthenticationFailHandler.onAuthenticationFailure");
        req.setAttribute("authenException", authenException.getMessage());
        res.getWriter().println("{\"auth\":false,\"authenException\":\"" + authenException.getMessage() + "\"}");
    }

}
