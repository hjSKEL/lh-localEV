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
 * 조직 - 법인 Controller
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 29.
 */
@Controller
@RequestMapping("organization/company")
public class CompanyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 조직 - 법인 목록
     * 
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String companyList(HttpServletRequest req) {
        //
        LOGGER.debug("/organization/company/list");
        registerAccessLog(req, "/organization/company/list");
        return "organization/company/companyList";
    }

    /**
     * 조직 - 법인 상세
     * 
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public ModelAndView companyDetail(HttpServletRequest req, String companyId) {
        //
        LOGGER.debug("/organization/company/detail/{}", companyId);
        registerAccessLog(req, "/organization/company/detail");
        ModelAndView mav = new ModelAndView("organization/company/company");
        boolean isValid = Validation.isCompanyId(companyId);
        if(isValid) {
        	mav.addObject("companyId", companyId);
        } else {
        	mav.addObject("companyId", StringConstants.BLANK);
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
    public String companyCreate(HttpServletRequest req) {
        //
        LOGGER.debug("/organization/company");
        registerAccessLog(req, "/organization/company");
        return "organization/company/company";
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
