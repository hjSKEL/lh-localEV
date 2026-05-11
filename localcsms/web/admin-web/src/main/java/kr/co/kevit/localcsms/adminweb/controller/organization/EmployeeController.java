/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
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
import kr.co.kevit.localcsms.adminweb.util.Validation;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.process.AccessLogService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 조직 - 직원 Controller
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 5. 7.
 */
@Controller
@RequestMapping("organization/employee")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 조직 - 직원 목록
     * 
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String employeeList(HttpServletRequest req) {
        //
        LOGGER.debug("/organization/employee/list");
        registerAccessLog(req, "/organization/employee/list");
        return "organization/employee/employeeList";
    }

    /**
     * 조직 - 직원 상세
     * 
     * @param employeeId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public ModelAndView employeeDetail(HttpServletRequest req, String employeeId, String pwInitYn) {
        //
        LOGGER.debug("/organization/employee/detail/{}", employeeId);
        registerAccessLog(req, "/organization/employee/detail");
        User loginUser = SessionManager.getLoginUser();
        pwInitYn = loginUser.getPwInitYn();
        ModelAndView mav = new ModelAndView("organization/employee/employee");
        boolean isValid = Validation.isEmployeeId(employeeId);
        if(isValid) {
        	mav.addObject("employeeId", employeeId);
        	mav.addObject("pwInitYn", pwInitYn);
        } else {
        	mav.addObject("employeeId", StringConstants.BLANK);
        }
        return mav;
    }

    /**
     * 조직 - 법인 생성
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String employeeCreate(HttpServletRequest req) {
        //
        LOGGER.debug("/organization/employee");
        registerAccessLog(req, "/organization/employee");
        return "organization/employee/employee";
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
