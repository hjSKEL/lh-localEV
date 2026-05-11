/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.charger;

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
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.common.util.string.StringConstants;


/**
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 2.
 */
@RestController
@RequestMapping("ws/charger")
public class ChargerStatusResource extends AbstractResource{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerStatusResource.class);

    @Autowired
    private ChargerStatusService chargerStatusService;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<ChargerStatusInfoDto> searchChargerStatus(ChargerStatusSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/status, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<ChargerStatusInfoDto> resultSet = null;
        try {
            resultSet = chargerStatusService.retrieveChargerStatusBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/status, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/status, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return resultSet;
    }

    /**
     * 충전기상태 엑셀 다운로드
     *
     * @param searchCond
     * @return
     */

    @RequestMapping(value = "/status/chargerList/{cpCsId}", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet downloadCharStatInfoList(@PathVariable("cpCsId")String cpCsId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : /ws/charger/status/chargerList/{}, GET", loginUser.getUserId(), accessIp, cpCsId);
        List<ChargerStatusInfo> result = null;
        try {
            String [] csIds = cpCsId.split(StringConstants.DASH); 
            result = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : /ws/charger/status/chargerList/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, cpCsId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : /ws/charger/status/chargerList/{}, GET, FAIL", loginUser.getUserId(), accessIp, cpCsId);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, result);
    }

}