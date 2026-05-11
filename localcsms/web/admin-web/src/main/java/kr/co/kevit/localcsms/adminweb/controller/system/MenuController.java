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
 * 시스템 - 메뉴관리 Controller
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 2.
 */
@Controller
@RequestMapping("system/menu")
public class MenuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 시스템 - 메뉴관리 목록
     * 
     * @return
     */
    @RequestMapping(value = "/menuList", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String menuList(HttpServletRequest req) {
        //
        LOGGER.debug("/system/menu/menuList");
        registerAccessLog(req, "/system/menu/menuList");
        return "system/menu/menuList";
    }

    /**
     * 시스템 - 메뉴관리 상세
     * 
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public ModelAndView menuDetail(HttpServletRequest req, String menuId) {
        //
        LOGGER.debug("/system/menu/{}/menuDetail", menuId);
        registerAccessLog(req, "/system/menu/menuDetail");
        ModelAndView mav = new ModelAndView("system/menu/menuDetail");
        boolean isValid = Validation.isMenuId(menuId);
        if(isValid) {
        	mav.addObject("menuId", menuId);
        }else {
        	mav.addObject("menuId", StringConstants.BLANK);
        }
        return mav;
    }

    /**
     * 시스템 - 메뉴관리 생성
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String menuCreate(HttpServletRequest req) {
        //
        LOGGER.debug("/system/menu/menuForm");
        registerAccessLog(req, "/system/menu/menuForm");
        return "system/menu/menuForm";
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
