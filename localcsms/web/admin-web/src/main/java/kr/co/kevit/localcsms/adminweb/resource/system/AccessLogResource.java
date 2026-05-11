/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.system;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.process.AccessLogService;
import kr.co.kevit.localcsms.common.shared.AccessLogSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 *
 * @author wj.lee <a href="mailto:wj.lee@kevit.co.kr">wj.lee@kevit.co.kr</a>
 * @since 2022. 02. 24.
 */
@RestController
@RequestMapping("ws/system/access")
public class AccessLogResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogResource.class);

    @Autowired
    private AccessLogService accessLogService;

    /**
     * AccessLog 조회
     * 
     * @return
     */
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN" })
    public Page<AccessLog> retrieveAccessLogBySearchCond(AccessLogSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/access/log, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<AccessLog> resultSet = null;
        try {
            resultSet = accessLogService.retrieveAccessLogBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/access/log, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/access/log, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

}
