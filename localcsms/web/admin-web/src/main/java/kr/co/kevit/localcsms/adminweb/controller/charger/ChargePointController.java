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
@RequestMapping("charger/chargePoint")
public class ChargePointController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargePointController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 충전 - 충전소 목록
     * 
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String chargePointList(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/chargePoint/list");
        registerAccessLog(req, "/charger/chargePoint/list");
        return "charger/chargePoint/chargePointList";
    }

    /**
     * 충전 - 충전소 상세(읽기전용)
     * 
     * @param cpId
     * @return
     */
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public ModelAndView chargePointRead(HttpServletRequest req, String cpId) {
        //
        LOGGER.debug("/charger/chargePoint/{}", cpId);
        registerAccessLog(req, "/charger/chargePoint/read");
        ModelAndView mav = new ModelAndView("charger/chargePoint/chargePointRead");
        boolean isValid = Validation.isCpId(cpId);
        if(isValid) {
        	mav.addObject("cpId", cpId);
        } else {
        	mav.addObject("cpId", StringConstants.BLANK);
        }
        return mav;
    }

    /**
     * 충전 - 충전소 상세(수정)
     * 
     * @param cpId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public ModelAndView chargePointDetail(HttpServletRequest req, String cpId) {
        //
        LOGGER.debug("/charger/chargePoint/{}", cpId);
        registerAccessLog(req, "/charger/chargePoint/detail");
        ModelAndView mav = new ModelAndView("charger/chargePoint/chargePoint");
        boolean isValid = Validation.isCpId(cpId);
        if(isValid) {
        	mav.addObject("cpId", cpId);
        } else {
        	mav.addObject("cpId", StringConstants.BLANK);
        }
        return mav;
    }

    /**
     * 충전 - 충전소 생성
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public String chargePointCreate(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/chargePoint/chargePoint");
        registerAccessLog(req, "/charger/chargePoint/chargePoint");
        return "charger/chargePoint/chargePoint";
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
