/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.certificate;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.certificate.CsCertService;
import kr.co.kevit.localcsms.certificate.entity.domain.CsCert;
import kr.co.kevit.localcsms.certificate.entity.shared.CsCertSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 인증서 관리 - 충전소 인증서 REST Resource
 */
@RestController
@RequestMapping("ws/certificate/cs")
public class CsCertResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsCertResource.class);

    @Autowired
    private CsCertService csCertService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<CsCert> searchCsCertList(CsCertSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/certificate/cs/search, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<CsCert> resultSet = null;
        try {
            resultSet = csCertService.retrieveCsCertBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/certificate/cs/search, GET, SUCCESS", loginUser.getUserId(), accessIp);
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/certificate/cs/search, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    @RequestMapping(value = "/detail/{certId}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public CsCert searchCsCertDetail(@PathVariable("certId") BigInteger certId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/certificate/cs/detail/{}, GET", loginUser.getUserId(), accessIp, certId);
        CsCert result = null;
        try {
            result = csCertService.retrieveCsCert(certId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/certificate/cs/detail/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, certId);
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/certificate/cs/detail/{}, GET, FAIL", loginUser.getUserId(), accessIp, certId);
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
}
