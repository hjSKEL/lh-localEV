/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.common;

import java.util.List;

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
import kr.co.kevit.localcsms.common.domain.Code;
import kr.co.kevit.localcsms.common.process.CodeService;
import kr.co.kevit.localcsms.common.shared.CodeSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 2.
 */
@RestController
@RequestMapping("ws/common")
public class CommonCodeResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonCodeResource.class);

    @Autowired
    private CodeService codeService;

    @RequestMapping(value = "/codes", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN"})
    public List<Code> searchCommonCodes(CodeSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/common/codes, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        List<Code> resultSet = null;
        try {
            resultSet = codeService.retrieveCodeByParentCodes(searchCond.getHighCodes());
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/common/codes, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/common/codes, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return resultSet;
    }

    @RequestMapping(value = "/codes/{codeId}", method = RequestMethod.GET)
    @Secured({"ROLE_OPER", "ROLE_ADMIN"})
    public List<Code> searchCommonCodesByParentCode(@PathVariable("codeId") String codeId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/common/codes/{}, GET", loginUser.getUserId(), accessIp, codeId);
        List<Code> resultSet = null;
        try {
            resultSet = codeService.retrieveCodeByParentCode(codeId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/common/codes/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, codeId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/common/codes/{}, GET, FAIL", loginUser.getUserId(), accessIp, codeId);
        }
        return resultSet;
    }
}
