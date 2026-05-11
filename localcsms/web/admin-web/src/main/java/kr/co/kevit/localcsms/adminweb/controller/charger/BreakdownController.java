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
import org.springframework.web.servlet.ModelAndView;

import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.util.Validation;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.process.AccessLogService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 충전 - 충전소 Controller
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 3.
 */
@Controller
@RequestMapping("charger/breakdown")
public class BreakdownController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BreakdownController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 고장관리목록
     * 
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String breakdownList(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/breakdown/list");
        registerAccessLog(req, "/charger/breakdown/list");
        return "charger/breakdown/breakdownList";
    }

    /**
     * 고장 관리 상세
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public ModelAndView breakdownDetail(HttpServletRequest req, String id) {
        //
        LOGGER.debug("/charger/breakdown/{}", id);
        registerAccessLog(req, "/charger/breakdown/detail");
        ModelAndView mav = new ModelAndView("charger/breakdown/breakdown");
        boolean isValid = Validation.isBreakdownId(id);
        if(isValid) {
        	mav.addObject("id", id);
        }else {
        	mav.addObject("id", StringConstants.BLANK);
        }
        return mav;
    }
    
    /**
     * 고장 관리 생성
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String breakdownCreate(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/breakdown/breakdown");
        registerAccessLog(req, "/charger/breakdown/breakdown");
        return "charger/breakdown/breakdown";
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
