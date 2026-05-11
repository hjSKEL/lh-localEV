/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.organization;

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
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.organization.entity.domain.Company;
import kr.co.kevit.localcsms.organization.entity.shared.CompanyDto;
import kr.co.kevit.localcsms.organization.entity.shared.CompanySearchCond;
import kr.co.kevit.localcsms.organization.process.CompanyService;

/**
 * 議곗쭅 - 議곗쭅?뚯궗 Resource
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 19.
 */
@RestController
@RequestMapping("ws/organization/company")
public class CompanyResource extends AbstractResource{
private static final Logger LOGGER = LoggerFactory.getLogger(CompanyResource.class);

    @Autowired
    private CompanyService companyService;

    /**
     * ?앹뾽 - ?뚯궗紐⑸줉
     * @param searchCond
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<Company> searchEmployeeBySearchCond(CompanySearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/search, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<Company> resultSet = null;
        try {
            resultSet = companyService.retrieveCompanyByCompanySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/search, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/search, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * ?뚯궗 紐⑸줉
     *
     * @param searchCond
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public Page<CompanyDto> searchCompanyList(CompanySearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<CompanyDto> resultSet = null;
        try {
            resultSet = companyService.retrieveCompanyDetailByCompanySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/list, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * ?뚯궗 ?곸꽭議고쉶
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/retrieve/{companyId}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public CompanyDto searchCompanyDetail(@PathVariable("companyId") String companyId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/retrieve/{}, GET", loginUser.getUserId(), accessIp, companyId);
        CompanyDto result = null;
        try {
            result = companyService.retrieveCompanyByCompanyId(companyId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/retrieve/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, companyId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/retrieve/{}, GET, FAIL", loginUser.getUserId(), accessIp, companyId);
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * ?뚯궗 ?깅줉
     *
     * @param company
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Secured({  "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet registerCompany(@RequestBody Company company, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/register, POST, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(company));
        try {
            company.setWriter(new Writer(loginUser.getUserId()));
            int seq = companyService.registerCompany(company);
            JsonResultSet resultSet = new JsonResultSet(seq > 0 ? ResultStatus.SUCCESS : ResultStatus.FAIL);
            resultSet.setResult(company);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/register, POST, SUCCESS", loginUser.getUserId(), accessIp);
            return resultSet;
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/register, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }

    /**
     * ?뚯궗 ?섏젙
     *
     * @param companyId
     * @param company
     * @return
     */
    @RequestMapping(value = "/modify/{companyId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet updateCompany(@PathVariable("companyId") String companyId, @RequestBody CompanyDto company, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/modify/{}, PUT, DATA : {}", loginUser.getUserId(), accessIp, companyId, new Gson().toJson(company));
        try {
            company.setWriter(new Writer(loginUser.getUserId()));
            boolean result = companyService.modifyCompany(company);
            JsonResultSet resultSet = new JsonResultSet(result ? ResultStatus.SUCCESS : ResultStatus.FAIL);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/modify/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, companyId);
            return resultSet;
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/modify/{}, PUT, FAIL", loginUser.getUserId(), accessIp, companyId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }

    /**
     * ?뚯궗 ??젣
     *
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/remove/{companyId}", method = RequestMethod.DELETE)
    @Secured({"ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet deleteCompany(@PathVariable("companyId") String companyId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/remove/{}, DELETE", loginUser.getUserId(), accessIp, companyId);
        try {            
            boolean result = companyService.removeCompany(companyId);
            JsonResultSet resultSet = new JsonResultSet(result ? ResultStatus.SUCCESS : ResultStatus.FAIL);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/remove/{}, DELETE, SUCCESS", loginUser.getUserId(), accessIp, companyId);
            return resultSet;
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/company/remove/{}, DELETE, FAIL", loginUser.getUserId(), accessIp, companyId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }
}
