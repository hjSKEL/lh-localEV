/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.system;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
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
import kr.co.kevit.localcsms.authority.process.UserService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.security.PasswordUtil;
import kr.co.kevit.localcsms.common.util.string.StringUtils;

/**
 * 시스템관리 - 로그인 Resource
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 5. 13.
 */
@RestController
@RequestMapping("ws/system/user")
public class UserResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserService userService;

    /**
     * LoginId 중복체크
     * 
     * @return
     */
    @RequestMapping(value = "/checkDupleLoginId/{loginId}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public boolean checkDupleLoginId(@PathVariable("loginId") String loginId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/user/checkDupleLoginId/{}, GET", loginUser.getUserId(), accessIp, loginId);
        try {
            User user = userService.retrieveUserById(loginId);
            boolean isDup = user == null || StringUtils.isEmpty(user.getLoginId()) ? false : true;
            if(isDup) {
                LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/user/checkDupleLoginId/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, loginId);
            }else {
                LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/user/checkDupleLoginId/{}, GET, FAIL", loginUser.getUserId(), accessIp, loginId);
            }
            return isDup;
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/user/checkDupleLoginId/{}, GET, FAIL", loginUser.getUserId(), accessIp, loginId);
            LOGGER.error(e.getMessage(), e);
            return true;
        }
    }

    /**
     * targetPaymentInfo 개인/법인 고객 지불정보 저장
     *
     * @param employeeId
     * @param user
     * @return
     */
    @RequestMapping(value = "/{employeeId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER", "ROLE_CP_MGT" })
    public JsonResultSet updateTargetPaymentInfo(@PathVariable("employeeId") String employeeId,@RequestBody User user, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/user/{}, PUT, DATA : {}", loginUser.getUserId(), accessIp, employeeId, new Gson().toJson(user));
        try {
            if (user.getWriter() == null) {
                user.setWriter(new Writer(loginUser.getUserId()));
            } else {
                user.getWriter().setUpdUserId(loginUser.getUserId());
                user.getWriter().setUpdateDate(new Date());
            }
            
            user.setUserId(employeeId);
            
            if (StringUtils.isNoneEmpty(user.getUserPwd())) {
                PasswordUtil passwordUtil = PasswordUtil.getInstance();
                user.setSalt(passwordUtil.generateSalt());
                user.setUserPwd(passwordUtil.encryptPassword(user.getUserPwd(), user.getSalt()));
                user.setPwUpdateDate(new Date());
            }
            if (loginUser.getUserId().equals(employeeId)) {
            	user.setPwInitYn("N");
            }
            boolean result = userService.saveUser(user);
            JsonResultSet resultSet = new JsonResultSet(result ? ResultStatus.SUCCESS : ResultStatus.FAIL);
            if(result) {
                LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/user/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, employeeId);
            }else {                
                LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/user/{}, PUT, FAIL", loginUser.getUserId(), accessIp, employeeId);
            }
            return resultSet;
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/user/{}, PUT, FAIL", loginUser.getUserId(), accessIp, employeeId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }
}
