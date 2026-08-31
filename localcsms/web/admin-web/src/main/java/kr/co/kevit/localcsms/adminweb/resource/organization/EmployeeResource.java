/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.organization;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.adminweb.view.ExcelDownloadView;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
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
            resultSet = employeeService.retrieveEmployeeWithUserBySearchCond(searchCond, roleType, loginUser.getUserId());
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    @RequestMapping(value = "/download/list", method = RequestMethod.GET)
    @Secured({"ROLE_OPER", "ROLE_ADMIN"})
    public View downloadEmployeeList(EmployeeSearchCond searchCond, Model model, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/download/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        try {
            searchCond.setPageNumber(0);
            searchCond.setPageItemSize(Integer.MAX_VALUE);
            UserRoleType roleType = loginUser.getRoles().get(0).getRoleType();
            Page<EmployeeDto> resultSet = employeeService.retrieveEmployeeWithUserBySearchCond(searchCond, roleType, loginUser.getUserId());
            List<Object> excelData = new ArrayList<>();
            for (EmployeeDto employee : resultSet.getResult()) {
                Map<String, Object> temp = new HashMap<>();
                temp.put("emplName", employee.getEmplName());
                temp.put("companyName", employee.getCompanyName());
                temp.put("loginId", employee.getLoginId() != null ? employee.getLoginId() : "-");
                temp.put("mblPhoneNo", formatPhone(employee.getMblPhoneNo()));
                temp.put("roleType", employee.getRoleType() != null ? employee.getRoleType().getDesc() : "-");
                temp.put("emplStatus", employeeStatusLabel(employee));
                temp.put("regDate", employee.getWriter() != null && employee.getWriter().getRegistrationDate() != null
                        ? DateUtils.dateToString(employee.getWriter().getRegistrationDate(), DateUtils.DATE_FORMAT) : "");
                temp.put("lastLoginDate", employee.getLastLoginDate() != null
                        ? DateUtils.dateToString(employee.getLastLoginDate(), DateUtils.DATE_FORMAT) : "-");
                excelData.add(temp);
            }
            model.addAttribute("excelData", excelData);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/download/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/employee/download/list, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return new ExcelDownloadView("RC_006.xlsx", "직원목록(" + DateUtils.getCurrentDateAsString(DateUtils.DATE_FORMAT) + ").xlsx");
    }

    // 재직상태 라벨 - employeeList.js의 상태 표시 로직과 동일 기준(emplStatus '1'=정상/계정잠김, '2'=퇴사)
    private String employeeStatusLabel(EmployeeDto employee) {
        if ("2".equals(employee.getEmplStatus())) {
            return "퇴사";
        }
        return employee.getPwFailCount() < 5 ? "정상" : "계정잠김";
    }

    // 휴대폰번호 포맷 - common.js의 phoneFormat과 동일 기준
    private String formatPhone(String value) {
        if (value == null || value.isEmpty()) {
            return "-";
        }
        if (value.length() == 10) {
            return value.substring(0, 2) + "-" + value.substring(2, 6) + "-" + value.substring(6);
        }
        if (value.length() == 11) {
            return value.substring(0, 3) + "-" + value.substring(3, 7) + "-" + value.substring(7);
        }
        return value;
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
