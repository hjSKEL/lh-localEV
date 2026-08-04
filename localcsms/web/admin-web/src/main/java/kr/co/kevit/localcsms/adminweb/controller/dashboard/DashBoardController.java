/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller.dashboard;

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
 * 대시보드 Controller
 */
@Controller
@RequestMapping("dashBoard")
public class DashBoardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashBoardController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 대시보드 메인
     *
     * @return
     */
    @RequestMapping(value = "/dashBoard", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String dashBoard(HttpServletRequest req) {
        LOGGER.debug("/dashBoard/dashBoard");
        registerAccessLog(req, "/dashBoard/dashBoard");
        return "dashBoard/dashBoard";
    }

    /**
     * 시스템 대시보드
     *
     * @return
     */
    @RequestMapping(value = "/systemDashBoard", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String systemDashBoard(HttpServletRequest req) {
        LOGGER.debug("/dashBoard/systemDashBoard");
        registerAccessLog(req, "/dashBoard/systemDashBoard");
        return "dashBoard/systemDashBoard";
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
