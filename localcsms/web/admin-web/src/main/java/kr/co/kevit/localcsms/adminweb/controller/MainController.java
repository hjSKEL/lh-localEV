/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import kr.co.kevit.localcsms.authority.entity.domain.Menu;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.process.MenuService;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import kr.co.kevit.localcsms.organization.process.EmployeeService;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 13.
 */
@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/")
    public String root(HttpServletRequest req) {

        String contextPath = req.getContextPath();
        LOGGER.debug("CONTEXT_PATH : " + contextPath);
        User loginUser = SessionManager.getLoginUser();
        UserRoleType role = loginUser.getRoles().get(0).getRoleType();
        switch (role) {
        case ROOT_ADMIN:
        case ADMIN:
            return "redirect:admin/main";
        case OPERATION:
            return "redirect:operation/main";
        default:
            return "redirect:" + contextPath + "/index";
        }
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        LOGGER.debug("index Controller...");
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("menus", null);
        return mav;
    }

    @RequestMapping(value = "/admin/main", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN" })
    public ModelAndView mainAdmin() {
        User loginUser = SessionManager.getLoginUser();
        // ROOT_ADMIN은 RoleHierarchy 덕에 위 @Secured("ROLE_ADMIN")도 통과하지만, 화면은 실제 역할 기준으로
        // "모든 메뉴"를 보여줘야 하므로(TB_SYME002 권한표를 안 거침) 여기서 실제 역할을 다시 확인한다.
        UserRoleType actualRole = loginUser.getRoles().get(0).getRoleType();
        ModelAndView mav = new ModelAndView("layout/adminLayout");
        mav.addObject("user", loginUser);
        mav.addObject("pwInitYn", loginUser.getPwInitYn());
        mav.addObject("employeeId", loginUser.getUserId());
        mav.addObject("currentRole", UserRoleType.getKeyCodeValue(actualRole));

        Map<String, String> roleData = new HashMap<>();
        EmployeeDto employeeDto = retrieveEmployee(loginUser.getUserId());
        roleData.put("companyId", employeeDto.getCompanyId());
        roleData.put("companyName", employeeDto.getCompanyName());
        mav.addObject("roleData", roleData);
        mav.addObject("menus", actualRole == UserRoleType.ROOT_ADMIN ? menuService.retrieveAllMenu() : retrieveMenus(UserRoleType.ADMIN));
        return mav;
    }

    @RequestMapping(value = "/operation/main", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER" })
    public ModelAndView mainOperation() {
        User loginUser = SessionManager.getLoginUser();
        ModelAndView mav = new ModelAndView("layout/operatorLayout");
        mav.addObject("user", loginUser);
        mav.addObject("pwInitYn", loginUser.getPwInitYn());
        mav.addObject("employeeId", loginUser.getUserId());
        mav.addObject("currentRole", UserRoleType.getKeyCodeValue(UserRoleType.OPERATION));

        Map<String, String> roleData = new HashMap<>();
        EmployeeDto employeeDto = retrieveEmployee(loginUser.getUserId());
        roleData.put("companyId", employeeDto.getCompanyId());
        roleData.put("companyName", employeeDto.getCompanyName());
        mav.addObject("roleData", roleData);
        mav.addObject("menus", retrieveMenus(UserRoleType.OPERATION));
        return mav;
    }

    private List<Menu> retrieveMenus(UserRoleType type) {
        //
        List<UserRoleType> roleStr = new ArrayList<>();
        roleStr.add(type);
        return menuService.retrieveMenuByRoleType(roleStr);
    }

    private EmployeeDto retrieveEmployee(String userId) {
        return employeeService.retrieveEmployeeById(userId);
    }
}
