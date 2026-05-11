/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.common;

import java.util.List;

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
import kr.co.kevit.localcsms.common.domain.CarModel;
import kr.co.kevit.localcsms.common.process.CarModelService;
import kr.co.kevit.localcsms.common.shared.CarModelSearchCond;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 23.
 */
@RestController
@RequestMapping("ws/carModel")
public class CarModelResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarModelResource.class);

    @Autowired
    private CarModelService service;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public List<CarModel> serachCarModel(CarModelSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/carModel/search, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        List<CarModel> resultSet = null;
        try {
            resultSet = service.retrieveCarModelByCarModelSearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/carModel/search, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/carModel/search, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return resultSet;
    }
}