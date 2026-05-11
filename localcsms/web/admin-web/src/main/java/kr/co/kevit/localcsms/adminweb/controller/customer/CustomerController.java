/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller.customer;

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
 * 고객 - 고객 Controller
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 5. 14.
 */
@Controller
@RequestMapping("customer")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private AccessLogService accessLogService;
    
    /**
     * 고객 - 고객 목록
     * 
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String customerList(HttpServletRequest req) {
        //
        LOGGER.debug("/customer/list");
        registerAccessLog(req, "/customer/list");
        return "customer/customerList";
    }

    /**
     * 고객 - 고객 상세
     * 
     * @param customerId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public ModelAndView customerDetail(HttpServletRequest req, String customerId) {
        //
        LOGGER.debug("/customer/detail/{}", customerId);
        registerAccessLog(req, "/customer/detail");
        ModelAndView mav = new ModelAndView("customer/customer");
        boolean isValid = Validation.isCustomerId(customerId);
        if(isValid) {
        	mav.addObject("customerId", customerId);
        } else {
        	mav.addObject("customerId", StringConstants.BLANK);
        }
        return mav;
    }

    /**
     * 고객 - 고객 생성
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String customerCreate(HttpServletRequest req) {
        //
        LOGGER.debug("/customer/customer");
        registerAccessLog(req, "/customer/customer");
        return "customer/customer";
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
