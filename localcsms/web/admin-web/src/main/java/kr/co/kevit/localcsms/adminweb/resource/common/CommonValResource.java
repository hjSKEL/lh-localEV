/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of kevit Corporation.
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
import kr.co.kevit.localcsms.common.domain.CodeVal;
import kr.co.kevit.localcsms.common.process.CodeValService;
import kr.co.kevit.localcsms.common.shared.CodeSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 2.
 */
@RestController
@RequestMapping("ws/common")
public class CommonValResource extends AbstractResource{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonValResource.class);

    @Autowired
    private CodeValService codeValService;

    @RequestMapping(value = "/val", method = RequestMethod.GET)
    @Secured({"ROLE_OPER", "ROLE_ADMIN"})
    public List<CodeVal> searchCommonVals(CodeSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/common/val, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        List<CodeVal> resultSet = null;
        try {
            resultSet = codeValService.retrieveCodeValCodeValByParentCodes(searchCond.getHighCodes());
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/common/val, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/common/val, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return resultSet;
    }

    @RequestMapping(value = "/val/{pCode}", method = RequestMethod.GET)
    @Secured({"ROLE_OPER","ROLE_ADMIN"})
    public List<CodeVal> searchCommonVals(@PathVariable("pCode") String pCode, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/common/val/{}, GET", loginUser.getUserId(), accessIp, pCode);
        List<CodeVal> resultSet = null;
        try {
            resultSet = codeValService.retrieveCodeValCodeValByParentCode(pCode);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/common/val/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, pCode);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/common/val/{}, GET, FAIL", loginUser.getUserId(), accessIp, pCode);
        }
        return resultSet;
    }
}
