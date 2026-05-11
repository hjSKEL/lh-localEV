/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.system;

import java.util.ArrayList;
import java.util.List;

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
import kr.co.kevit.localcsms.authority.entity.domain.Menu;
import kr.co.kevit.localcsms.authority.entity.domain.RoleAuthority;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.entity.shared.MenuDto;
import kr.co.kevit.localcsms.authority.entity.shared.UserInfoDto;
import kr.co.kevit.localcsms.authority.entity.shared.UserSearchCond;
import kr.co.kevit.localcsms.authority.process.MenuService;
import kr.co.kevit.localcsms.authority.process.RoleAuthorityService;
import kr.co.kevit.localcsms.authority.process.UserService;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyCodeValue;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 시스템관리 - 권한관리 Resource
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a> 
 * @since 2019. 4. 1.
 */
@RestController
@RequestMapping("ws/system")
public class AuthorityResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityResource.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleAuthorityService roleAuthorityService;

    @Autowired
    private UserService userService;

    /**
     * UserRoleType 목록 조회
     * @return
     */
    @RequestMapping(value = "/userRoleTypeList", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public List<EnumKeyCodeValue> searchUserRoleTypeList(HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/userRoleTypeList, GET", loginUser.getUserId(), accessIp);
        List<EnumKeyCodeValue> resultSet = null;
        try {
            resultSet = UserRoleType.getKeyCodeValues();
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/userRoleTypeList, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/userRoleTypeList, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * Child 메뉴 전체목록 조회
     * @return
     */
    @RequestMapping(value = "/allChildMenuList", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public List<MenuDto> searchAllChildMenuList(HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/allChildMenuList, GET", loginUser.getUserId(), accessIp);
        List<MenuDto> resultSet = null;
        try {
            resultSet = menuService.retrieveAllChildMenu();
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/allChildMenuList, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/allChildMenuList, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * UserRoleType에 해당하는 메뉴목록 조회
     * @return
     */
    @RequestMapping(value = "/userRoleTypeMenuList", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public List<Menu> searchUserRoleTypeMenuList(String userRoleTypeStr, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/userRoleTypeMenuList, GET, DATA.userRoleTypeStr : {}", loginUser.getUserId(), accessIp, userRoleTypeStr);
        List<Menu> resultSet = null;
        try {
            List<UserRoleType> roleStr = new ArrayList<>();
            roleStr.add(UserRoleType.getRoleTypeByCode(userRoleTypeStr));
            resultSet = menuService.retrieveMenuByRoleType(roleStr);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/userRoleTypeMenuList, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/userRoleTypeMenuList, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * UserRoleType별 메뉴권한 변경
     * @return
     */
    @RequestMapping(value = "/modifyUserRoleTypeAuthForMenu", method = RequestMethod.POST)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public boolean modifyUserRoleTypeAuthForMenu(String checked, String menuId, String userRoleTypeStr, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/modifyUserRoleTypeAuthForMenu, POST, DATA checked:{}, menuId:[], userRoleType:{} ", 
                loginUser.getUserId(), accessIp, checked, menuId, userRoleTypeStr);
        
        
        boolean isResulted = roleAuthorityService.changeRoleAuthority(new RoleAuthority(UserRoleType.getRoleTypeByCode(userRoleTypeStr), menuId), checked);
        if(isResulted) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/modifyUserRoleTypeAuthForMenu, POST, SUCCESS", loginUser.getUserId(), accessIp);
        }else {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/modifyUserRoleTypeAuthForMenu, POST, FAIL", loginUser.getUserId(), accessIp);
        }
        return isResulted;
    }

    @RequestMapping(value = "/myUserInfo", method = RequestMethod.GET)
    @Secured({"ROLE_OPER","ROLE_ADMIN" })
    public User getMyUserInfo(HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/myUserInfo, GET", loginUser.getUserId(), accessIp);
        loginUser.setUserPwd(null);
        LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/myUserInfo, GET, SUCCESS", loginUser.getUserId(), accessIp);
        return loginUser;
    }

    @RequestMapping(value = "/findUserInfoDto", method = RequestMethod.GET)
    @Secured({"ROLE_OPER","ROLE_ADMIN" })
    public Page<UserInfoDto> findUserInfoDtoBySearchCond(UserSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/findUserInfoDto, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<UserInfoDto> resultSet = null;
        try {
            resultSet = userService.retrieveUserInfoDtoBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/findUserInfoDto, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/findUserInfoDto, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

}
