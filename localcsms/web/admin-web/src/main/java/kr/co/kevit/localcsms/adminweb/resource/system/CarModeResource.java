/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.system;

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
import kr.co.kevit.localcsms.common.domain.CarModel;
import kr.co.kevit.localcsms.common.process.CarModelService;
import kr.co.kevit.localcsms.common.shared.CarModelSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 시스템관리 - 차량모델관리 Resource
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 5. 29.
 */
@RestController
@RequestMapping("ws/system/carModel")
public class CarModeResource  extends AbstractResource{

    private static final Logger LOGGER = LoggerFactory.getLogger(CarModeResource.class);

    @Autowired
    private CarModelService carModelService;

    /**
     * 차량모델 목록 조회
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public Page<CarModel> searchCarModelList(CarModelSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<CarModel> resultSet = null;
        try {
            resultSet = carModelService.retrieveCarModelAllByCondition(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * 차량모델 상세조회
     * 
     * @param carModelId
     * @return
     */
    @RequestMapping(value = "/{carModelId}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public CarModel searcMenuDetail(@PathVariable("carModelId") String carModelId, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel/{}, GET", loginUser.getUserId(), accessIp, carModelId);
        CarModel result = null;
        try {
            result = carModelService.retrieveCarModel(carModelId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, carModelId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel/{}, GET, FAIL", loginUser.getUserId(), accessIp, carModelId);
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 차량모델 수정
     * 
     * @param carModelId
     * @param carModel
     * @return
     */
    @RequestMapping(value = "/{carModelId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet updateMenu(@PathVariable("carModelId") String carModelId, @RequestBody CarModel carModel, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel/{}, PUT, DATA : {}", loginUser.getUserId(), accessIp, carModelId, new Gson().toJson(carModel));
        try {
            carModelService.modifyCarModel(carModel);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, carModelId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel/{}, PUT, FAIL", loginUser.getUserId(), accessIp, carModelId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, carModel.getCarModelId());
    }

    /**
     * 차량모델 등록
     * 
     * @param carModel
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet registerCarModel(@RequestBody CarModel carModel, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel, POST, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(carModel));
        try {
            carModelService.registerCarModel(carModel);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel, POST, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/system/carModel, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS, carModel);
    }
}
