/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
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
 * 인증서 관리 - 충전소 인증서 Controller
 */
@Controller
@RequestMapping("charger/cert")
public class CsCertController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsCertController.class);

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String csCert(HttpServletRequest req) {
        LOGGER.debug("/charger/cert");
        registerAccessLog(req, "/charger/cert");
        return "charger/cert/csCert";
    }

    private void registerAccessLog(HttpServletRequest req, String url) {
        String userIPAddress = req.getHeader("X-Forwarded-For");
        if (userIPAddress == null) userIPAddress = req.getHeader("X-FORWARDED-FOR");
        if (userIPAddress == null) userIPAddress = req.getRemoteAddr();
        User loginUser = SessionManager.getLoginUser();
        accessLogService.registerAccessLog(
            new AccessLog(loginUser.getUserId(), req.getContextPath() + url, "WEB", userIPAddress));
    }
}
