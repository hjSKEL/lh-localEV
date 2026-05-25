/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller.derctrl;

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
 * OCPP 2.1 R04 — DER Control 화면 Controller (3종).
 *
 * @author bckim
 * @since 2026. 5. 26.
 */
@Controller
@RequestMapping("derctrl")
public class DerControlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DerControlController.class);

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String derControlList(HttpServletRequest req) {
        LOGGER.debug("/derctrl/list");
        registerAccessLog(req, "/derctrl/list");
        return "derctrl/derControlList";
    }

    @RequestMapping(value = "/alarm/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String derAlarmList(HttpServletRequest req) {
        LOGGER.debug("/derctrl/alarm/list");
        registerAccessLog(req, "/derctrl/alarm/list");
        return "derctrl/derAlarmList";
    }

    @RequestMapping(value = "/startstop/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String derStartStopList(HttpServletRequest req) {
        LOGGER.debug("/derctrl/startstop/list");
        registerAccessLog(req, "/derctrl/startstop/list");
        return "derctrl/derStartStopList";
    }

    private void registerAccessLog(HttpServletRequest req, String url) {
        String userIPAddress = req.getHeader("X-Forwarded-For");
        if (userIPAddress == null) userIPAddress = req.getHeader("X-FORWARDED-FOR");
        if (userIPAddress == null) userIPAddress = req.getRemoteAddr();
        String contextPath = req.getContextPath();
        User loginUser = SessionManager.getLoginUser();
        AccessLog accessLog = new AccessLog(loginUser.getUserId(), contextPath + url, "WEB", userIPAddress);
        accessLogService.registerAccessLog(accessLog);
    }
}
