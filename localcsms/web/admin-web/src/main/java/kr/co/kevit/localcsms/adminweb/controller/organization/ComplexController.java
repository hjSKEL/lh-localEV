/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller.organization;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.process.AccessLogService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.common.util.string.StringUtils;

/**
 * 조직 - 단지 Controller
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 8. 10.
 */
@Controller
@RequestMapping("organization/complex")
public class ComplexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComplexController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 조직 - 단지 목록
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String complexList(HttpServletRequest req) {
        //
        LOGGER.debug("/organization/complex/list");
        registerAccessLog(req, "/organization/complex/list");
        return "organization/complex/complexList";
    }

    /**
     * 조직 - 단지 상세
     * @param complexId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public ModelAndView complexDetail(HttpServletRequest req, String complexId) {
        //
        LOGGER.debug("/organization/complex/detail/{}", complexId);
        registerAccessLog(req, "/organization/complex/detail");
        ModelAndView mav = new ModelAndView("organization/complex/complex");
        mav.addObject("complexId", StringUtils.isEmpty(complexId) ? StringConstants.BLANK : complexId);
        return mav;
    }

    /**
     * 조직 - 단지 생성
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String complexCreate(HttpServletRequest req) {
        //
        LOGGER.debug("/organization/complex");
        registerAccessLog(req, "/organization/complex");
        return "organization/complex/complex";
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
