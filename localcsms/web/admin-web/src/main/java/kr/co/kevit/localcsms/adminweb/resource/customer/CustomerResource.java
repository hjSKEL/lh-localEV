/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.customer;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
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
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerSearchCond;
import kr.co.kevit.localcsms.customer.process.CustomerMgtService;
import kr.co.kevit.localcsms.customer.process.CustomerService;

/**
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 21.
 */
@RestController
@RequestMapping("ws/customer")
public class CustomerResource extends AbstractResource{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResource.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMgtService customerMgtService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN"})
    public Page<CustomerDto> searchEmployeeList(CustomerSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/search, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<CustomerDto> resultSet = null;
        try {
            resultSet = customerService.retrieveCustomerDtoByCustomerSearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/search, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/search, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    @RequestMapping(value = "/download/list", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN"})
    public View downloadCustomerList(CustomerSearchCond searchCond, Model model, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/download/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        try {
            searchCond.setPageNumber(0);
            searchCond.setPageItemSize(Integer.MAX_VALUE);
            Page<CustomerDto> resultSet = customerService.retrieveCustomerDtoByCustomerSearchCond(searchCond);
            List<Object> excelData = new ArrayList<>();
            for (CustomerDto customer : resultSet.getResult()) {
                Map<String, Object> temp = new HashMap<>();
                temp.put("complexName", customer.getComplexName());
                temp.put("dong", customer.getDong());
                temp.put("ho", customer.getHo());
                temp.put("custName", customer.getCustName());
                temp.put("mblPhoneNo", customer.getMblPhoneNo());
                temp.put("cutCardNo", customer.getCustomerMgt() != null ? customer.getCustomerMgt().getCutCardNo() : "");
                temp.put("regDate", customer.getWriter() != null && customer.getWriter().getRegistrationDate() != null
                        ? DateUtils.dateToString(customer.getWriter().getRegistrationDate(), DateUtils.DATE_FORMAT) : "");
                temp.put("deleteYn", customer.getCustomerMgt() != null && "Y".equals(customer.getCustomerMgt().getDeleteYn()) ? "Y" : "");
                excelData.add(temp);
            }
            model.addAttribute("excelData", excelData);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/download/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/download/list, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return new ExcelDownloadView("RC_007.xlsx", "고객목록(" + DateUtils.getCurrentDateAsString(DateUtils.DATE_FORMAT) + ").xlsx");
    }

    /**
     * 怨좉컼 ?깅줉
     *
     * @param customer
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet registerCustomer(@RequestBody CustomerDto customer, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer, POST, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(customer));
        try {
            customer.setWriter(new Writer(loginUser.getUserId()));
            customerService.registerCustomer(customer);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer, POST, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, customer.getCustomerId());
    }

    /**
     * 怨좉컼 ?섏젙
     *
     * @param customerId
     * @param customer
     * @return
     */
    @RequestMapping(value = "/{customerId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet updateCustomer(@PathVariable("customerId") String customerId, @RequestBody CustomerDto customer, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/{}, PUT, DATA : {}", loginUser.getUserId(), accessIp, customerId, new Gson().toJson(customer));
        try {
            customer.setWriter(new Writer(loginUser.getUserId()));
            customerService.modifyCustomer(customer);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/{}, PUT, FAIL", loginUser.getUserId(), accessIp, customerId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, customerId);
        return new JsonResultSet(ResultStatus.SUCCESS);
    }
    
    /**
     * 고객등급(CUT_GRD_CD)만 수정
     *
     * @param customerId
     * @param cutGrdCode
     * @return
     */
    @RequestMapping(value = "/{customerId}/grade", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet updateCustomerGrade(@PathVariable("customerId") String customerId,
                                             @RequestParam("cutGrdCode") String cutGrdCode,
                                             HttpServletRequest request) {
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/{}/grade, PUT, DATA : {}", loginUser.getUserId(), accessIp, customerId, cutGrdCode);
        try {
            customerMgtService.modifyCustomerGrade(customerId, cutGrdCode);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/{}/grade, PUT, FAIL", loginUser.getUserId(), accessIp, customerId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
        LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/{}/grade, PUT, SUCCESS", loginUser.getUserId(), accessIp, customerId);
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    /**
     * 정지여부(STOP_YN)만 수정
     *
     * @param customerId
     * @param stopYn
     * @return
     */
    @RequestMapping(value = "/{customerId}/stopYn", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet updateCustomerStopYn(@PathVariable("customerId") String customerId,
                                              @RequestParam("stopYn") String stopYn,
                                              HttpServletRequest request) {
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/{}/stopYn, PUT, DATA : {}", loginUser.getUserId(), accessIp, customerId, stopYn);
        try {
            customerMgtService.modifyCustomerStopYn(customerId, stopYn);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/{}/stopYn, PUT, FAIL", loginUser.getUserId(), accessIp, customerId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
        LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/{}/stopYn, PUT, SUCCESS", loginUser.getUserId(), accessIp, customerId);
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    /**
     * 怨좉컼 ?곸꽭議고쉶
     *
     * @param customerId
     * @return
     */
    @RequestMapping(value = "/detail/{customerId}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public CustomerDto searchCustomerDetail(@PathVariable("customerId") String customerId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/detail/{}, GET, DATA : {}", loginUser.getUserId(), accessIp, customerId);
        CustomerDto result = null;
        try {
            result = customerService.retrieveCustomer(customerId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/detail/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, customerId);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/detail/{}, GET, FAIL", loginUser.getUserId(), accessIp, customerId);
            LOGGER.error(ex.getMessage(), ex);
        }
        return result;
    }

}
