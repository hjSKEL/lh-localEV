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
import kr.co.kevit.localcsms.authority.entity.domain.Menu;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.entity.shared.MenuDto;
import kr.co.kevit.localcsms.authority.entity.shared.MenuSearchCond;
import kr.co.kevit.localcsms.authority.process.MenuService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 시스템관리 - 메뉴관리 Resource
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 2.
 */
@RestController
@RequestMapping("ws/system/menu")
public class MenuResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuResource.class);

    @Autowired
    private MenuService menuService;

    /**
     * 메뉴 목록 조회
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public Page<MenuDto> searchMenuList(MenuSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<MenuDto> resultSet = null;
        try {
            resultSet = menuService.retrieveMenuAllByCondition(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * 메뉴 단건조회
     * 
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/{menuId}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public Menu searcMenuDetail(@PathVariable("menuId") String menuId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu/{}, GET", loginUser.getUserId(), accessIp, menuId);
        Menu result = null;
        try {
            result = menuService.retrieveMenuByMenuId(menuId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, menuId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu/{}, GET, FAIL", loginUser.getUserId(), accessIp, menuId);
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 메뉴 수정
     * 
     * @param menuId
     * @param menu
     * @return
     */
    @RequestMapping(value = "/{menuId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet updateMenu(@PathVariable("menuId") String menuId, @RequestBody Menu menu, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu/{}, PUT, DATA : {}", loginUser.getUserId(), accessIp, menuId, new Gson().toJson(menu));
        try {
            menu.setWriter(new Writer(loginUser.getUserId()));
            menuService.modifyMenu(menuId, menu);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, menuId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu/{}, PUT, FAIL", loginUser.getUserId(), accessIp, menuId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    /**
     * 메뉴 삭제
     * 
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/{menuId}", method = RequestMethod.DELETE)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet deleteMenu(@PathVariable("menuId") String menuId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu/{}, DELETE", loginUser.getUserId(), accessIp, menuId);
        try {
            menuService.removeMenu(menuId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu/{}, DELETE, SUCCESS", loginUser.getUserId(), accessIp, menuId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu/{}, DELETE, FAIL", loginUser.getUserId(), accessIp, menuId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }
    
    /**
     * 메뉴 등록
     * 
     * @param menuDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet registerMenuForm(@RequestBody MenuDto menuDto, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu, POST, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(menuDto));
        try {
            menuDto.setWriter(new Writer(loginUser.getUserId()));
            menuService.registerMenu(menuDto);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu, POST, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/menu, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, menuDto);
    }
}
