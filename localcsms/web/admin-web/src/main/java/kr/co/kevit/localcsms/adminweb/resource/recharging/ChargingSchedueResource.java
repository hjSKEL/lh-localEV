/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.recharging;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.recharger.entity.shared.ChargingScheduleSearchCond;
import kr.co.kevit.localcsms.recharger.process.ChargingScheduleService;

/**
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 2.
 */
@RestController
@RequestMapping("ws/charging/schedule")
public class ChargingSchedueResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargingSchedueResource.class);

    @Autowired
    private ChargingScheduleService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Page<ChargingSchedule> findChargingScheduleBySearchCond(ChargingScheduleSearchCond searchCond, HttpServletRequest request){
        //
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] ACCESS_IP:{}, URL : ws/recharging/schedule/list, GET, DATA : {}", accessIp, new Gson().toJson(searchCond));
        Page<ChargingSchedule> resultSet = null;
        try {
            resultSet = service.retrieveChargingScheduleBySearchCond(searchCond);
            LOGGER.info("[RES]  ACCESS_IP:{}, URL : ws/recharging/schedule/list, GET, SUCCESS", accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] ACCESS_IP:{}, URL : ws/recharging/schedule/list, GET, FAIL", accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public JsonResultSet saveChargingSchedule(@RequestBody ChargingSchedule sched, HttpServletRequest request){
        //
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] ACCESS_IP:{}, URL : ws/charging/schedule, PUT, DATA : {}", accessIp, new Gson().toJson(sched));
        try {
            service.registerChargingSchedule(sched);
            LOGGER.info("[RES] ACCESS_IP:{}, URL : ws/charging/schedule, PUT, SUCCESS", accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] ACCESS_IP:{}, URL : ws/charging/schedule, PUT, FAIL", accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

}