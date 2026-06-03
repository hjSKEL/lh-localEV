/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.customer;

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
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCert;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCertSearchCond;
import kr.co.kevit.localcsms.customer.process.CustomerCertService;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 인증서 관리 - 고객 인증서 REST Resource
 */
@RestController
@RequestMapping("ws/customer/cert")
public class CustomerCertResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCertResource.class);

    @Autowired
    private CustomerCertService customerCertService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<CustomerCert> searchCustomerCertList(CustomerCertSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/cert/search, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<CustomerCert> resultSet = null;
        try {
            resultSet = customerCertService.retrieveCustomerCertBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/cert/search, GET, SUCCESS", loginUser.getUserId(), accessIp);
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/cert/search, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    @RequestMapping(value = "/detail/{customerId}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public CustomerCert searchCustomerCertDetail(@PathVariable("customerId") String customerId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/cert/detail/{}, GET", loginUser.getUserId(), accessIp, customerId);
        CustomerCert result = null;
        try {
            List<CustomerCert> list = customerCertService.retrieveCustomerCert(customerId);
            if (list != null && !list.isEmpty()) {
                result = list.get(0);
            }
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/cert/detail/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, customerId);
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/cert/detail/{}, GET, FAIL", loginUser.getUserId(), accessIp, customerId);
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
}
