/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller.charger;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.process.AccessLogService;

/**
 * 충전기 - 충전기 Controller
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 4.
 */
@Controller
@RequestMapping("charger/chargingStation")
public class CSCmdController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSCmdController.class);

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "/control", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String control(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/chargingStation/control");
        registerAccessLog(req, "/charger/chargingStation/control");
        return "charger/csControl/OCPP16Control";
    }

    @RequestMapping(value = "/{protocol}/control", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String kev001Control(HttpServletRequest req, @PathVariable("protocol") String protocol) {
        //
        LOGGER.debug("/charger/chargingStation/{}/control", protocol);
        registerAccessLog(req, "/charger/chargingStation/protocolControl");
        return "charger/csControl/" + protocol + "Control";
    }

    @RequestMapping(value = "/devControl", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String devControl(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/chargingStation/dev/control");
        registerAccessLog(req, "/charger/chargingStation/dev/control");
        return "charger/csControl/OCPP16DevControl";
    }

    @RequestMapping(value = "/{protocol}/devControl", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String kevDevControl(HttpServletRequest req, @PathVariable("protocol") String protocol) {
        //
        LOGGER.debug("/charger/chargingStation/{}/devControl", protocol);
        registerAccessLog(req, "/charger/chargingStation/protocolDevControl");
        return "charger/csControl/" + protocol + "DevControl";
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
