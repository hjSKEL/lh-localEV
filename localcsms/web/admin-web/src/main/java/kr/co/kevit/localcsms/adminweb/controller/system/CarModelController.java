/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller.system;

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
 * 시스템 - 차량모델 Controller
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 5. 29.
 */
@Controller
@RequestMapping("system/carModel")
public class CarModelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarModelController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 시스템 - 차량모델관리 목록
     * 
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String carModelList(HttpServletRequest req) {
        //
        LOGGER.debug("/system/carModel/carModelList");
        registerAccessLog(req, "/system/carModel/carModelList");
        return "system/carModel/carModelList";
    }

    /**
     * 시스템 - 차량모델관리 상세
     * 
     * @param carModelId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public ModelAndView carModelDetail(HttpServletRequest req, String carModelId) {
        //
        LOGGER.debug("/system/carModel/{}/detail", carModelId);
        registerAccessLog(req, "/system/carModel/detail");
        ModelAndView mav = new ModelAndView("system/carModel/carModel");
        boolean isValid = Validation.isCarModelId(carModelId);
        if(isValid) {
        	mav.addObject("carModelId", carModelId);
        }else {
        	mav.addObject("carModelId", StringConstants.BLANK);
        }
        return mav;
    }

    /**
     * 시스템 - 차량모델관리 생성
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String carModelCreate(HttpServletRequest req) {
        //
        LOGGER.debug("/system/carModel/carModel");
        registerAccessLog(req, "/system/carModel/carModel");
        return "system/carModel/carModel";
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
