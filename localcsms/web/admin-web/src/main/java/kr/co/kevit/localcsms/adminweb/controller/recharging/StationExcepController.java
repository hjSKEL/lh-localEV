/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller.recharging;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.process.AccessLogService;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 6. 5.
 */
@Controller
@RequestMapping("recharging/exception")
public class StationExcepController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StationExcepController.class);

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String reservationList(HttpServletRequest req) {
        //
        LOGGER.debug("/recharging/exception/view");
        registerAccessLog(req, "/recharging/exception/view");
        return "recharging/exception/view";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String reservationExceptionList(HttpServletRequest req) {
        //
        LOGGER.debug("/recharging/exception/list");
        registerAccessLog(req, "/recharging/exception/list");
        return "recharging/exception/list";
    }

    private void registerAccessLog(HttpServletRequest req, String url) {
        String userIPAddress = req.getHeader("X-Forwarded-For");
        if (userIPAddress == null) {
            userIPAddress = req.getHeader("X-FORWARDED-FOR");
        }
        if (userIPAddress == null) {
            userIPAddress = req.getRemoteAddr();
        }
        String contextPath = req.getContextPath();
        User loginUser = SessionManager.getLoginUser();
        AccessLog accessLog = new AccessLog(loginUser.getUserId(), contextPath + url, "WEB", userIPAddress);
        accessLogService.registerAccessLog(accessLog);
    }
}