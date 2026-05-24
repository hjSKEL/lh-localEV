/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.recharging;

import java.util.List;

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
import kr.co.kevit.localcsms.common.util.exception.KEVITException;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.RechargingSearchCond;
import kr.co.kevit.localcsms.recharger.process.RechargingService;

/**
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 2.
 */
@RestController
@RequestMapping("ws/recharging")
public class RechargingResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RechargingResource.class);

    @Autowired
    private RechargingService rechargingService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public Page<RechargingDto> findRechargingList(RechargingSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<RechargingDto> resultSet = null;
        try {
            resultSet = rechargingService.retrieveRechargingByRechargingSearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/list, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    @RequestMapping(value = "/detail/{rechargingId}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public RechargingDto getRechargingDetail(@PathVariable("rechargingId") String rechargingId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/detail/{}, GET", loginUser.getUserId(), accessIp, rechargingId);
        RechargingDto result = null;
        try {
            result = rechargingService.retrieveRechargingById(rechargingId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/detail/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, rechargingId);
            if (result == null) {
                return new RechargingDto();
            }
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/detail/{}, GET, FAIL", loginUser.getUserId(), accessIp, rechargingId);
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet completeRechargingInfo(@RequestBody Recharging recharging, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging, PUT, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(recharging));
        try {
            rechargingService.completeRecharging(recharging);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging, PUT, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (KEVITException e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging, PUT, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging, PUT, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }
    
    /**
     * 진행 중 트랜잭션의 최대 에너지 한도(Wh) 변경. body: {"maxEnergy": 10000}
     */
    @RequestMapping(value = "/{rechargingId}/maxEnergy", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet changeMaxEnergy(@PathVariable("rechargingId") String rechargingId,
                                         @RequestBody Recharging body,
                                         HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/{}/maxEnergy, PUT, DATA : {}",
                loginUser.getUserId(), accessIp, rechargingId, new Gson().toJson(body));
        try {
            Double maxEnergy = body == null ? null : body.getMaxEnergy();
            rechargingService.modifyMaxEnergy(rechargingId, maxEnergy, loginUser.getUserId());
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/{}/maxEnergy, PUT, SUCCESS",
                    loginUser.getUserId(), accessIp, rechargingId);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/{}/maxEnergy, PUT, FAIL",
                    loginUser.getUserId(), accessIp, rechargingId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    @RequestMapping(value = "/customer/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public Page<RechargingDto> findRechargingListWithCustomer(RechargingSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/customer/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<RechargingDto> resultSet = null;
        try {
            resultSet = rechargingService.retrieveRechargingWithCustomerByRechargingSearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/customer/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/customer/list, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }
    
    @RequestMapping(value = "/download/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public View downloadRechargingList(RechargingSearchCond searchCond, Model model, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/download/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        try {
            searchCond.setPageNumber(0);
            searchCond.setPageItemSize(Integer.MAX_VALUE);
            List<RechargingDto> rcList = rechargingService.retrieveRecharging4DownloadByRechargingSearchCond(searchCond);
            model.addAttribute("excelData", rcList);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/download/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/download/list, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return new ExcelDownloadView("RC_002.xlsx", "충전내역(" + searchCond.getFromDate().substring(0, 8) + "_" + searchCond.getToDate().substring(0, 8) + ").xlsx");
    }
    
    @RequestMapping(value = "/download/customer/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public View downloadRechargingListWithCustomer(RechargingSearchCond searchCond, Model model, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/download/customer/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        try {
            searchCond.setPageNumber(0);
            searchCond.setPageItemSize(Integer.MAX_VALUE);
            List<RechargingDto> rcList = rechargingService.retrieveRechargingWithCustomer4DownloadByRechargingSearchCond(searchCond);
            model.addAttribute("excelData", rcList);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/download/customer/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/recharging/download/customer/list, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return new ExcelDownloadView("RC_001.xlsx", "고객충전내역(" + searchCond.getFromDate().substring(0, 8) + "_" + searchCond.getToDate().substring(0, 8) + ").xlsx");
    }

}