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
import kr.co.kevit.localcsms.common.domain.Code;
import kr.co.kevit.localcsms.common.process.CodeService;
import kr.co.kevit.localcsms.common.shared.CodeSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 시스템관리 - 권한관리 Resource
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 1.
 */
@RestController
@RequestMapping("ws/system")
public class CommonCodeMgtResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonCodeMgtResource.class);

    @Autowired
    private CodeService commonCodeService;

    @RequestMapping(value = "/commonCodeList", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public Page<Code> retrieveCodeByCodeSearchCond(CodeSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/commonCodeList, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<Code> resultSet = null;
        try {
            resultSet = commonCodeService.retrieveCodeByCodeSearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/commonCodeList, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/commonCodeList, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }
}
