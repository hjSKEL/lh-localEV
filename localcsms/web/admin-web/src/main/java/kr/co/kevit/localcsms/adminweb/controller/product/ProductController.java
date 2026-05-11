/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller.product;

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
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 3.
 */
@Controller
@RequestMapping("product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public String productList(HttpServletRequest req) {
        //
        LOGGER.debug("/product/list");
        registerAccessLog(req, "/product/list");
        return "product/productList";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public ModelAndView productDetail(HttpServletRequest req, String productId) {
        //
        LOGGER.debug("/product/detail");
        registerAccessLog(req, "/product/detail");
        ModelAndView mav = new ModelAndView("product/productDetail");
        //TODO productId 정합성 체크
        boolean isValid = Validation.isProductId(productId);
        if(isValid) {
        	mav.addObject("productId", productId);
        }else {
        	mav.addObject("productId", StringConstants.BLANK);
        }
        return mav;
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