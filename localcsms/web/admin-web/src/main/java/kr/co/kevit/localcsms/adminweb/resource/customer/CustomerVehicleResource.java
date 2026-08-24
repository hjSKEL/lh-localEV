/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
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
import kr.co.kevit.localcsms.customer.entity.domain.CustomerVehicle;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerVehicleSearchCond;
import kr.co.kevit.localcsms.customer.process.CustomerVehicleService;

/**
 * 고객 차량(EVCCID) REST.
 *
 * @author bckim
 */
@RestController
@RequestMapping("ws/customer/vehicle")
public class CustomerVehicleResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerVehicleResource.class);

    @Autowired
    private CustomerVehicleService customerVehicleService;

    /** 검색조건 페이지 목록 */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<CustomerVehicle> searchVehicles(CustomerVehicleSearchCond searchCond,
                                                HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER:{}, IP:{}, GET ws/customer/vehicle, DATA:{}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        return customerVehicleService.retrieveVehiclesBySearchCond(searchCond);
    }

    /** 고객 보유 차량 목록 */
    @RequestMapping(value = "/byCustomer/{customerId}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public List<CustomerVehicle> listByCustomer(@PathVariable("customerId") String customerId,
                                                HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER:{}, IP:{}, GET ws/customer/vehicle/byCustomer/{}",
                loginUser.getUserId(), accessIp, customerId);
        return customerVehicleService.retrieveVehiclesByCustomerId(customerId);
    }

    /** VIN 단건 조회 */
    @RequestMapping(value = "/{vinNo}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public CustomerVehicle getVehicle(@PathVariable("vinNo") String vinNo,
                                      HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER:{}, IP:{}, GET ws/customer/vehicle/{}",
                loginUser.getUserId(), accessIp, vinNo);
        return customerVehicleService.retrieveVehicle(vinNo);
    }

    /** 차량번호 중복확인 */
    @RequestMapping(value = "/checkCarNo/{carNo}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet checkCarNo(@PathVariable("carNo") String carNo, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER:{}, IP:{}, GET ws/customer/vehicle/checkCarNo/{}", loginUser.getUserId(), accessIp, carNo);
        CustomerVehicle exists = customerVehicleService.retrieveVehicleByCarNo(carNo);
        if (exists == null) {
            return new JsonResultSet(ResultStatus.SUCCESS, "사용 가능한 차량번호입니다.");
        }
        return new JsonResultSet(ResultStatus.FAIL, "이미 등록된 차량번호입니다.");
    }

    /** 차대번호(VIN) 중복확인 */
    @RequestMapping(value = "/checkVinNo/{vinNo}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet checkVinNo(@PathVariable("vinNo") String vinNo, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER:{}, IP:{}, GET ws/customer/vehicle/checkVinNo/{}", loginUser.getUserId(), accessIp, vinNo);
        CustomerVehicle exists = customerVehicleService.retrieveVehicle(vinNo);
        if (exists == null) {
            return new JsonResultSet(ResultStatus.SUCCESS, "사용 가능한 차대번호입니다.");
        }
        return new JsonResultSet(ResultStatus.FAIL, "이미 등록된 차대번호입니다.");
    }

    /** 등록 */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet registerVehicle(@RequestBody CustomerVehicle vehicle,
                                         HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER:{}, IP:{}, POST ws/customer/vehicle, DATA:{}",
                loginUser.getUserId(), accessIp, new Gson().toJson(vehicle));
        try {
            vehicle.setWriter(new Writer(loginUser.getUserId()));
            customerVehicleService.registerVehicle(vehicle);
        } catch (Exception ex) {
            LOGGER.error("[RES] POST ws/customer/vehicle FAIL: {}", ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    /** 수정 */
    @RequestMapping(value = "/{vinNo}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet modifyVehicle(@PathVariable("vinNo") String vinNo,
                                       @RequestBody CustomerVehicle vehicle,
                                       HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER:{}, IP:{}, PUT ws/customer/vehicle/{}, DATA:{}",
                loginUser.getUserId(), accessIp, vinNo, new Gson().toJson(vehicle));
        try {
            vehicle.setVinNo(vinNo);
            vehicle.setWriter(new Writer(loginUser.getUserId()));
            customerVehicleService.modifyVehicle(vehicle);
        } catch (Exception ex) {
            LOGGER.error("[RES] PUT ws/customer/vehicle/{} FAIL: {}", vinNo, ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    /** 삭제 */
    @RequestMapping(value = "/{vinNo}", method = RequestMethod.DELETE)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet removeVehicle(@PathVariable("vinNo") String vinNo,
                                       HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER:{}, IP:{}, DELETE ws/customer/vehicle/{}",
                loginUser.getUserId(), accessIp, vinNo);
        try {
            customerVehicleService.removeVehicle(vinNo);
        } catch (Exception ex) {
            LOGGER.error("[RES] DELETE ws/customer/vehicle/{} FAIL: {}", vinNo, ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }
}
