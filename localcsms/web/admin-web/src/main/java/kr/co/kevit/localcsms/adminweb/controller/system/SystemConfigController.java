/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation.
 * All rights reserved. This software is the proprietary information of
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller.system;

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
 * 시스템 - 운영환경설정 Controller
 */
@Controller
@RequestMapping("system")
public class SystemConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemConfigController.class);

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN" })
    public String config(HttpServletRequest req) {
        //
        LOGGER.debug("/system/config");
        registerAccessLog(req, "/system/config");
        return "system/config";
    }

    @RequestMapping(value = "/connConfig", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN" })
    public String connConfig(HttpServletRequest req) {
        //
        LOGGER.debug("/system/connConfig");
        registerAccessLog(req, "/system/connConfig");
        return "system/connConfig";
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
