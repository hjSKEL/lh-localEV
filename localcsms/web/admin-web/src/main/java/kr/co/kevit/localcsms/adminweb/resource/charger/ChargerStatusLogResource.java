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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerAuthHis;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfoHis;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerAuthHisSearchCond;
import kr.co.kevit.localcsms.charger.entity.shared.ChargerStatusInfoHisSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargerStatusInfoHisService;
import kr.co.kevit.localcsms.common.util.page.Page;


/**
 * 충전관리 - 충전기 상태로그 Resource
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 25.
 */
@RestController
@RequestMapping("ws/charger")
public class ChargerStatusLogResource extends AbstractResource{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerStatusLogResource.class);

    @Autowired
    private ChargerStatusInfoHisService chargerStatusInfoHisService;

    @RequestMapping(value = "/logList", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<ChargerStatusInfoHis> searchChargerLog(ChargerStatusInfoHisSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/logList, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        if (searchCond.getPageItemSize() > 40) {
            searchCond.setPageItemSize(40);
        }
        Page<ChargerStatusInfoHis> resultSet = null;
        try {
            resultSet = chargerStatusInfoHisService.retrieveChargerStatusInfoHisBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/logList, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/logList, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return resultSet;
    }

    @RequestMapping(value = "/authorize/list", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<ChargerAuthHis> searchChargerAuthHis(ChargerAuthHisSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/authorize/list, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        if (searchCond.getPageItemSize() > 40) {
            searchCond.setPageItemSize(40);
        }
        Page<ChargerAuthHis> resultSet = null;
        try {
            resultSet = chargerStatusInfoHisService.retrieveChargerAuthHisBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/authorize/list, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/authorize/list, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return resultSet;
    }
}