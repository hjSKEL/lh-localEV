/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
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
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 3.
 */
@Controller
@RequestMapping("recharging")
public class RechargingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RechargingController.class);

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String rechargingList(HttpServletRequest req) {
        //
        LOGGER.debug("/recharging/list");
        registerAccessLog(req, "/recharging/list");
        return "recharging/rechargingList";
    }

    /**
     * 인증이력
     *
     * @return
     */
    @RequestMapping(value = "/authorize/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String rechargingAuthList(HttpServletRequest req) {
        //
        LOGGER.debug("/recharging/authorize/list");
        registerAccessLog(req, "/recharging/authorize/list");
        return "recharging/customer/rechargingAuthList";
    }

    /**
     * 고객별 충전현황
     *
     * @return
     */
    @RequestMapping(value = "/customer/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER", "ROLE_CO_USER" })
    public String rechargingCustomerList(HttpServletRequest req) {
        //
        LOGGER.debug("/recharging/customer/list");
        registerAccessLog(req, "/recharging/customer/list");
        return "recharging/customer/rechargingCustomerList";
    }

    @RequestMapping(value = "/adjustment/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER", "ROLE_ADJUST" })
    public String rechargingAdjustmentList(HttpServletRequest req) {
        //
        LOGGER.debug("/recharging/adjustment/list");
        registerAccessLog(req, "/recharging/adjustment/list");
        return "recharging/adjustmentList";
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