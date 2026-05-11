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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.process.AccessLogService;

/**
 * 충전 - 충전기상태 Controller
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 23.
 */
@Controller
@RequestMapping("charger/statusInfo")
public class ChargerStatusInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerStatusInfoController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 충전 - 충전기상태 목록
     * 
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String chargerStatusList(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/statusInfo/list");
        registerAccessLog(req, "/charger/statusInfo/list");
        return "charger/statusInfo/statusInfoList";
    }

    /**
     * 충전 - 충전기상태 목록
     * 
     * @return
     */
    @RequestMapping(value = "/fmwVersion", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String fmwVersion(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/statusInfo/fmwVersion");
        registerAccessLog(req, "/charger/statusInfo/fmwVersion");
        return "charger/statusInfo/fmwVersion";
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
