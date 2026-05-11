/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.charger;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.adminweb.view.ExcelDownloadView;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.charger.entity.domain.ChargePoint;
import kr.co.kevit.localcsms.charger.entity.shared.ChargePointSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargePointService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 18.
 */
@RestController
@RequestMapping("ws/charger/chargePoint")
public class ChargePointResource extends AbstractResource{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargePointResource.class);

    @Autowired
    private ChargePointService chargePointService;

    @RequestMapping(value = "/allChargePointList4Monitoring", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<ChargePoint> searchAllChargePointList4Monitoring(ChargePointSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/allChargePointList4Monitoring, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<ChargePoint> resultSet = null;
        try {            
            resultSet = chargePointService.retrieveChargePointBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/allChargePointList4Monitoring, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch(Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/allChargePointList4Monitoring, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return resultSet;
    }

    @RequestMapping(method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER", "ROLE_CM", "ROLE_CS"})
    public Page<ChargePoint> searchChargePointList(ChargePointSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<ChargePoint> resultSet = null;
        try {            
            resultSet = chargePointService.retrieveChargePointBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch(Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return resultSet;
    }
    
    @RequestMapping(value = "/{cpId}", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public ChargePoint searchChargePointDetail(@PathVariable("cpId") String cpId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/{}, GET", loginUser.getUserId(), accessIp, cpId);
        ChargePoint result = null;
        try {            
            result = chargePointService.retrieveChargePointBySpotId(cpId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, cpId);
        }catch(Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/{}, GET, FAIL", loginUser.getUserId(), accessIp, cpId);
        }
        return result;
    }
    
    /**
     * 충전소 등록
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet registerChargePointForm(@RequestBody ChargePoint chargePointCsm, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint, POST, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(chargePointCsm));
        chargePointCsm.setWriter(new Writer(loginUser.getUserId()));
        try {            
            chargePointService.registerChargePoint(chargePointCsm);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint, POST, SUCCESS", loginUser.getUserId(), accessIp);
        }catch(Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, chargePointCsm);
    }

    /**
     * 충전소 수정
     *
     * @param cpId
     * @param chargePoint
     * @return
     */
    @RequestMapping(value = "/{cpId}", method = RequestMethod.PUT)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet updateChargePoint(@PathVariable("cpId") String cpId, @RequestBody ChargePoint chargePoint, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/{}, PUT, DATA : {}", loginUser.getUserId(), accessIp, cpId, new Gson().toJson(chargePoint));
        chargePoint.setWriter(new Writer(loginUser.getUserId()));
        chargePoint.setCpId(cpId);
        try {            
            chargePointService.modifyChargePoint(chargePoint);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, cpId);
        }catch(Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/{}, PUT, FAIL", loginUser.getUserId(), accessIp, cpId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    /**
     * 충전소 삭제
     *
     * @param cpId
     * @return
     */
    @RequestMapping(value = "/delete/{cpId}", method = RequestMethod.PUT)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet deleteSpot(@PathVariable("cpId") String cpId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/delete/{}, PUT", loginUser.getUserId(), accessIp, cpId);
        ChargePoint chargePoint = new ChargePoint();
        chargePoint.setWriter(new Writer(loginUser.getUserId()));
        chargePoint.setCpId(cpId);
        try {            
            chargePointService.removeChargePoint(chargePoint);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/delete/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, cpId);
        }catch(Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/delete/{}, PUT, FAIL", loginUser.getUserId(), accessIp, cpId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }
    
    @RequestMapping(value = "/download/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public View downloadCharePointList(ChargePointSearchCond searchCond, Model model, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/download/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        try {
            searchCond.setPageNumber(0);
            searchCond.setPageItemSize(Integer.MAX_VALUE);
            Page<ChargePoint> resultSet = chargePointService.retrieveChargePointBySearchCond(searchCond);
            List<Object> excelData = new ArrayList<>();
            List<ChargePoint> resultList = resultSet.getResult();
            int seq = 1;
            for(ChargePoint chargePoint : resultList) {
                Map<String, Object> temp = new HashMap<>();
                temp.put("seq", seq++);
                temp.put("cpId", chargePoint.getCpId());
                temp.put("cpName", chargePoint.getCpName());
                temp.put("placeName", chargePoint.getCpLocation());
                temp.put("lowCsCount", chargePoint.getLowCsCount());
                temp.put("highCsCount", chargePoint.getHighCsCount());
                temp.put("electSupplyCapability", chargePoint.getElectSupplyCapability());
                temp.put("cpUseYn", chargePoint.getCpUseYn());
                temp.put("deleteYn", chargePoint.getDeleteYn());
                temp.put("registrationDate", DateUtils.dateToString(chargePoint.getWriter().getRegistrationDate(), DateUtils.DATE_TIME_FORMAT2));
                excelData.add(temp);
            }
            model.addAttribute("excelData", excelData);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/download/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch(Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/download/list, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return new ExcelDownloadView("CP_001.xlsx", "충전소목록(" + DateUtils.getCurrentDateAsString(DateUtils.DATE_FORMAT)  + ").xlsx");
    }
    
    /**
     * ChargePointId 중복체크
     * 
     * @return
     */
    @RequestMapping(value = "/checkDupleChargePointId/{cpId}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet checkDupleChargePointId(@PathVariable("cpId") String cpId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/checkDupleChargePointId/{}, GET", loginUser.getUserId(), accessIp, cpId);
        try {
            ChargePoint chargePoint = chargePointService.retrieveChargePointBySpotId(cpId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/checkDupleChargePointId/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, cpId);
            if(chargePoint == null) {
                return new JsonResultSet(ResultStatus.SUCCESS, "사용가능한 충전소ID 입니다.");
            }else {
                return new JsonResultSet(ResultStatus.FAIL, "등록된 충전소ID 입니다.");
            }
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargePoint/checkDupleChargePointId/{}, GET, FAIL", loginUser.getUserId(), accessIp, cpId);
            return new JsonResultSet(ResultStatus.FAIL, "관리자에게 문의 하세요.");
        }
    }

}
