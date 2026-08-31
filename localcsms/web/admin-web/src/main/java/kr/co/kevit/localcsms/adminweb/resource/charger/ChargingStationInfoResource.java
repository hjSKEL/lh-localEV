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
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStation;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingStationCsm;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.domain.Code;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.process.CodeService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 22.
 */
@RestController
@RequestMapping("ws/charger")
public class ChargingStationInfoResource extends AbstractResource{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargingStationInfoResource.class);

    @Autowired
    private ChargingStationService chargingStationService;
    
    @Autowired
    private CodeService codeService;

    @RequestMapping(value = "/{cpId}/{csId}/info", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public ChargingStation searchChargerInfo(@PathVariable("cpId") String cpId,@PathVariable("csId") String csId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/{}/{}/info, GET", loginUser.getUserId(), accessIp, cpId, csId);
        ChargingStation result = null;
        try {
            result = chargingStationService.retrieveChargingStationByCpIdNCsId(cpId, csId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/{}/{}/info, GET, SUCCESS", loginUser.getUserId(), accessIp, cpId, csId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/{}/{}/info, GET, FAIL", loginUser.getUserId(), accessIp, cpId, csId);
        }
        return result;
    }
    
    @RequestMapping(value = "/chargingStation", method = RequestMethod.POST)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet registerChargingStation(@RequestBody ChargingStation chargingStation, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation, POST, DATA :{}", loginUser.getUserId(), accessIp, new Gson().toJson(chargingStation));
        
        try {
            chargingStation.setWriter(new Writer(loginUser.getUserId()));
            chargingStationService.registerChargingStation(chargingStation);
            LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation, POST, SUCCESS", loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, chargingStation);
    }
    
    @RequestMapping(value = "/chargingStation/{cpId}/{csId}", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public ChargingStationDto searchChargingStationDto(@PathVariable("cpId") String cpId,@PathVariable("csId") String csId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/{}/{}, GET", loginUser.getUserId(), accessIp, cpId, csId);
        ChargingStationDto result = null;
        try {
            result = chargingStationService.retrieveChargingStationDtoByCpIdNCsId(cpId, csId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/{}/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, cpId, csId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/{}/{}, GET, FAIL", loginUser.getUserId(), accessIp, cpId, csId);
        }
        return result;
    }
    
    @RequestMapping(value = "/chargingStation/{cpId}/{csId}", method = RequestMethod.DELETE)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet removeChargingStation(@PathVariable("cpId") String cpId,@PathVariable("csId") String csId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/{}/{}, DELETE", loginUser.getUserId(), accessIp, cpId, csId);
        try {            
            chargingStationService.removeChargingStation(cpId, csId, loginUser.getUserId());
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/{}/{}, DELETE, SUCCESS", loginUser.getUserId(), accessIp, cpId, csId);
            return new JsonResultSet(ResultStatus.SUCCESS);
        }catch(Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/{}/{}, DELETE, FAIL", loginUser.getUserId(), accessIp, cpId, csId);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }
    
    @RequestMapping(value = "/chargingStation/{cpId}/{csId}", method = RequestMethod.PUT)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet searchChargingStationDto(@PathVariable("cpId") String cpId,@PathVariable("csId") String csId,@RequestBody ChargingStation chargingStation, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/{}/{}, PUT, DATA : {}", loginUser.getUserId(), accessIp, cpId, csId, new Gson().toJson(chargingStation));
        try {
            chargingStation.setWriter(new Writer(loginUser.getUserId()));
            chargingStationService.modifyChargingStation(chargingStation);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/{}/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, cpId, csId);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/{}/{}, PUT, FAIL", loginUser.getUserId(), accessIp, cpId, csId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
        return new JsonResultSet(ResultStatus.SUCCESS, chargingStation);
    }
    
    
    @RequestMapping(value = "/{cpId}/list", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public List<ChargingStationCsm> searchChargerList(@PathVariable("cpId") String cpId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/{}, GET", loginUser.getUserId(), accessIp, cpId);
        List<ChargingStationCsm> result = null;
        try {
            result = chargingStationService.retrieveChargingStationCsmByCpId(cpId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, cpId);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/{}, GET, FAIL", loginUser.getUserId(), accessIp, cpId);
            LOGGER.error(ex.getMessage(), ex);
        }
        return result;
    }

    @RequestMapping(value = "/finder", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<ChargingStationDto> findChargerByChargerSearchCond(ChargingStationSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/finder, GET, DATA : {}", loginUser.getUserId(), accessIp,new Gson().toJson(searchCond));
        Page<ChargingStationDto> resultSet = null;
        try {
            resultSet = chargingStationService.retrieveChargingStationBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/finder, GET, SUCCESS", loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/finder, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultSet;
    }

    @RequestMapping(value = "/maxCpId/{cpId}", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet retrieveMaxCsId(@PathVariable("cpId") String cpId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/maxCpId/{}, GET", loginUser.getUserId(), accessIp, cpId);
        try {
            String nextCsId = chargingStationService.retrieveNextCsIdByCpId(cpId);
            LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/maxCpId/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, cpId);
            return new JsonResultSet(ResultStatus.SUCCESS, nextCsId);
        }catch(Exception ex) {
            LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/maxCpId/{}, GET, FAIL", loginUser.getUserId(), accessIp, cpId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }
    
    @RequestMapping(value = "/chargingStation/download/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER"})
    public View downloadChargingStationList(ChargingStationSearchCond searchCond, Model model, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/download/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        try {
            List<Code> makers = codeService.retrieveCodeByParentCode("CHMK00");
            Map<String, String> makerMap = new HashMap<>();
            for (Code code : makers) {
                makerMap.put(code.getCode(), code.getCodeName());
            }
            
            List<Code> csKindTypes = codeService.retrieveCodeByParentCode("CHKT00");
            Map<String, String> csKindTypeMap = new HashMap<>();
            for (Code code : csKindTypes) {
                csKindTypeMap.put(code.getCode(), code.getCodeName());
            }
            
            List<Code> csCatCodes = codeService.retrieveCodeByParentCode("CHRA00");
            Map<String, String> csCatCodeMap = new HashMap<>();
            for (Code code : csCatCodes) {
                csCatCodeMap.put(code.getCode(), code.getCodeName());
            }
            searchCond.setPageNumber(0);
            searchCond.setPageItemSize(Integer.MAX_VALUE);
            Page<ChargingStationDto> resultSet = chargingStationService.retrieveChargingStationBySearchCond(searchCond);
            
            List<Object> excelData = new ArrayList<>();
            
            List<ChargingStationDto> resultList = resultSet.getResult();
            int seq = 1;
            for(ChargingStationDto chargingStation : resultList) {
                Map<String, Object> temp = new HashMap<>();
                
                temp.put("seq", seq++);
                temp.put("cpName", chargingStation.getCpName());
                temp.put("csId", chargingStation.getCsId());
                temp.put("csUniqId", chargingStation.getCsUniqId());
                temp.put("csKindType", chargingStation.getCsKindType() != null ? csKindTypeMap.get(chargingStation.getCsKindType()) : "");
                temp.put("csChanelCount", chargingStation.getCsChanelCount());
                temp.put("electronicSupplyCapability", chargingStation.getElectSupplyCapability());
                temp.put("useYn", chargingStation.getUseYn());
                temp.put("brkdownYn", chargingStation.getBrkdownYn());
                temp.put("csCatCode", chargingStation.getCsCatCode() != null ? csCatCodeMap.get(chargingStation.getCsCatCode()) : "");
                temp.put("insYearMon", chargingStation.getInsYearMon());
                temp.put("registrationDate", DateUtils.dateToString(chargingStation.getWriter().getRegistrationDate(), DateUtils.DATE_TIME_FORMAT2));
                
                excelData.add(temp);
            }
            model.addAttribute("excelData", excelData);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/download/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch(Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/chargingStation/download/list, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return new ExcelDownloadView("CS_001.xlsx", "충전기목록(" + DateUtils.getCurrentDateAsString(DateUtils.DATE_FORMAT)  + ").xlsx");
    }
}
