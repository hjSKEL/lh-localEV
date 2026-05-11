/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.charger;

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
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownInfo;
import kr.co.kevit.localcsms.charger.entity.domain.BreakdownRepairInfo;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownMgtInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.BreakdownSearchCond;
import kr.co.kevit.localcsms.charger.process.BreakdownInfoService;
import kr.co.kevit.localcsms.charger.process.BreakdownMgtInfoService;
import kr.co.kevit.localcsms.charger.process.BreakdownRepairInfoService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.enumtype.charger.BreakdownStatus;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 18.
 */
@RestController
@RequestMapping("ws/charger/breakdown")
public class BreakdownResource extends AbstractResource{

    private static final Logger LOGGER = LoggerFactory.getLogger(BreakdownResource.class);

    @Autowired
    private BreakdownMgtInfoService bdMgtService;
    
    @Autowired
    private BreakdownInfoService bdInfoService;
    
    @Autowired
    private BreakdownRepairInfoService bdRepService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<BreakdownMgtInfoDto> searchBreakdownMgtInfoBySearchCond(BreakdownSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<BreakdownMgtInfoDto> resultSet = null;
        try {
            resultSet = bdMgtService.retrieveBreakdownMgtInfoBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown/list, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return resultSet;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public BreakdownMgtInfoDto searchBreakdownMgtInfoById(@PathVariable("id") String id, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown/{}, GET", loginUser.getUserId(), accessIp, id);
        BreakdownMgtInfoDto result = null;
        try {
            result = bdMgtService.retrieveBreakdownMgtInfoById(id);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, id);
        }catch(Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown/{}, GET, Fail", loginUser.getUserId(), accessIp, id);
        }
        return result;
    }
    
    /**
     * 고장등록
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet registerBreakdownInfo(@RequestBody BreakdownInfo info, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown, POST, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(info));
        info.setWriter(new Writer(loginUser.getUserId()));
        try {
            info.getBreakdownMgtInfo().setBreakdownStatus(BreakdownStatus.BDST01.getCode());
            bdInfoService.registerBreakdownInfo(info);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{},  URL : ws/charger/breakdown, POST, SUCCESS", accessIp, loginUser.getUserId());
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{},  URL : ws/charger/breakdown, POST, FAIL", accessIp, loginUser.getUserId());
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, info.getId());
    }

    /**
     * 고장 수정
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet updateChargePoint(@RequestBody BreakdownInfo info, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown, PUT, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(info));
        info.setWriter(new Writer(loginUser.getUserId()));
        try {
            bdInfoService.modifyBreakdownInfo(info);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown, PUT, SUCCESS", loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown, PUT, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }
    
    @RequestMapping(value = "/repair", method = RequestMethod.POST)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet registerBreakdownRepairInfo(@RequestBody BreakdownRepairInfo info, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown/repair, POST, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(info));
        info.setWriter(new Writer(loginUser.getUserId()));
        try {
            bdRepService.registerBreakdownRepairInfo(info);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown/repair, POST, SUCCESS", loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/breakdown/repair, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, info.getId());
    }
}
