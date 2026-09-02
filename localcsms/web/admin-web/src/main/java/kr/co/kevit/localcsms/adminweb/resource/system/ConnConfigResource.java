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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.system.entity.domain.ConnConfig;
import kr.co.kevit.localcsms.system.process.ConnConfigService;

/**
 * 시스템관리 - 연결설정 Resource
 *
 * @since 2026. 9. 2.
 */
@RestController
@RequestMapping("ws/system/connConfig")
public class ConnConfigResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnConfigResource.class);

    @Autowired
    private ConnConfigService connConfigService;

    /**
     * 연결설정 조회
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN" })
    public ConnConfig searchConnConfig(HttpServletRequest request) {
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/connConfig, GET", loginUser.getUserId(), accessIp);
        ConnConfig result = null;
        try {
            result = connConfigService.retrieveConnConfig();
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/connConfig, GET, SUCCESS", loginUser.getUserId(), accessIp);
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/connConfig, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 연결설정 저장
     */
    @RequestMapping(method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN" })
    public JsonResultSet updateConnConfig(@RequestBody ConnConfig connConfig, HttpServletRequest request) {
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/connConfig, PUT, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(connConfig));
        try {
            connConfig.setUpdId(loginUser.getUserId());
            connConfigService.modifyConnConfig(connConfig);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/connConfig, PUT, SUCCESS", loginUser.getUserId(), accessIp);
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/connConfig, PUT, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

}
