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
 * 충전기 - 충전기 Controller
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 4.
 */
@Controller
@RequestMapping("charger/chargingStation")
public class ChargingStationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargingStationController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 충전 - 충전기 목록
     * 
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_CHGR_BAILOR", "ROLE_OPER", "ROLE_ADMIN" })
    public String chargingStationList(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/chargingStation/list");
        registerAccessLog(req, "/charger/chargingStation/list");
        return "charger/chargingStation/chargingStationList";
    }

    /**
     * 충전 - 충전기 상세
     * 
     * @param cpId
     * @param csId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public ModelAndView chargingStationDetail(HttpServletRequest req, String csId, String cpId) {
        //
        LOGGER.debug("/charger/chargingStation/{}/{}", cpId, csId);
        registerAccessLog(req, "/charger/chargingStation/detail");
        ModelAndView mav = new ModelAndView("charger/chargingStation/chargingStation");
        boolean isValid = Validation.isCpId(cpId) && Validation.isCsId(csId);
        if(isValid) {
        	mav.addObject("csId", csId);
            mav.addObject("cpId", cpId);
        } else {
        	mav.addObject("cpId", StringConstants.BLANK);
        	mav.addObject("csId", StringConstants.BLANK);
        }
        return mav;
    }

    /**
     * 충전 - 충전기 생성
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String chargingStationCreate(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/chargingStation/chargingStation");
        registerAccessLog(req, "/charger/chargingStation/chargingStation");
        return "charger/chargingStation/chargingStation";
    }

    // 로그인 기록 추적
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
