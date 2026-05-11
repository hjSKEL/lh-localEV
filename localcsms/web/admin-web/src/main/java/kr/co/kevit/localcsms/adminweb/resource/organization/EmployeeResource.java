/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.organization;

import java.util.Date;
import java.util.List;

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
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.enumtype.PropertyKey;
import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import kr.co.kevit.localcsms.common.util.loader.PropertyLoader;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.domain.Employee;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeDto;
import kr.co.kevit.localcsms.organization.entity.shared.EmployeeSearchCond;
import kr.co.kevit.localcsms.organization.process.EmployeeService;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 21.
 */
@RestController
@RequestMapping("ws/organization/employee")
public class EmployeeResource extends AbstractResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeResource.class);

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @Secured({"ROLE_OPER", "ROLE_ADMIN"})
    public Page<EmployeeDto> searchEmployeeBySearchCond(EmployeeSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/search, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<EmployeeDto> resultSet = null;
        try {
            UserRoleType roleType = loginUser.getRoles().get(0).getRoleType();
            resultSet = employeeService.retrieveEmployeeBySearchCond(searchCond, roleType);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/search, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/search, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    @RequestMapping(method = RequestMethod.GET)
    @Secured({"ROLE_OPER", "ROLE_ADMIN"})
    public Page<EmployeeDto> searchEmployeeList(EmployeeSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<EmployeeDto> resultSet = null;
        try {
            UserRoleType roleType = loginUser.getRoles().get(0).getRoleType();
            resultSet = employeeService.retrieveEmployeeWithUserBySearchCond(searchCond, roleType);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * 吏곸썝 ?곸꽭議고쉶
     *
     * @param employeeId
     * @return
     */
    @RequestMapping(value = "/{employeeId}", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public EmployeeDto searchEmployeeDetail(@PathVariable("employeeId") String employeeId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/{}, GET", loginUser.getUserId(), accessIp, employeeId);
        EmployeeDto resultSet = null;
        try {
            resultSet = employeeService.retrieveEmployeeById(employeeId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, employeeId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/{}, GET, FAIL", loginUser.getUserId(), accessIp, employeeId);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * 吏곸썝 ?깅줉
     *
     * @param employee
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet registerEmployee(@RequestBody Employee employee, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee, POST, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(employee));
        try {
            employee.setWriter(new Writer(loginUser.getUserId()));
            employeeService.registerEmployee(employee);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee, POST, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, employee);
    }

    /**
     * 吏곸썝 ?섏젙
     *
     * @param employeeId
     * @param employee
     * @return
     */
    @RequestMapping(value = "/{employeeId}", method = RequestMethod.PUT)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet updateCompany(@PathVariable("employeeId") String employeeId, @RequestBody Employee employee, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/{}, PUT, DATA : {}", loginUser.getUserId(), accessIp, employeeId, new Gson().toJson(employee));
        try {
            employee.getWriter().setUpdUserId(loginUser.getUserId());
            employee.getWriter().setUpdateDate(new Date());
            employeeService.modifyEmployee(employee);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, employeeId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/{}, PUT, FAIL", loginUser.getUserId(), accessIp, employeeId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    /**
     * 吏곸썝 ?댁궗泥섎━
     *
     * @param employeeId
     * @param employee
     * @return
     */
    @RequestMapping(value = "/{employeeId}", method = RequestMethod.DELETE)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet removeCompany(@PathVariable("employeeId") String employeeId, @RequestBody Employee employee, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/{}, DELETE, DATA : {}", loginUser.getUserId(), accessIp, employeeId, new Gson().toJson(employee));
        try {
            employee.getWriter().setUpdUserId(loginUser.getUserId());
            employee.getWriter().setUpdateDate(new Date());
            employeeService.removeEmployee(employee);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/{}, DELETE, SUCCESS", loginUser.getUserId(), accessIp, employeeId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/{}, DELETE, FAIL", loginUser.getUserId(), accessIp, employeeId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    @RequestMapping(value = "/searchAdmin", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public List<Employee> searchAdminEmployee(HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/searchAdmin, GET", loginUser.getUserId(), accessIp);
        List<Employee> resultSet = null;
        try {
            String name = PropertyLoader.getInstance().getProperty(PropertyKey.KEVIT_LOGIN_CO);
            resultSet = employeeService.retrieveAdminEmployee(name);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/searchAdmin, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/searchAdmin, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }
}
