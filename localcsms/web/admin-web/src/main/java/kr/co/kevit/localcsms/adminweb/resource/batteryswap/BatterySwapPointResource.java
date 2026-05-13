/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.batteryswap;

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
import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapPoint;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapPointSearchCond;
import kr.co.kevit.localcsms.batteryswap.process.BatterySwapPointService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 배터리 교체 충전소 REST 엔드포인트
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@RestController
@RequestMapping("ws/batterySwap/point")
public class BatterySwapPointResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatterySwapPointResource.class);

    @Autowired
    private BatterySwapPointService batterySwapPointService;

    /** 목록 조회 */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<BatterySwapPointDto> searchPointList(BatterySwapPointSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point, GET, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<BatterySwapPointDto> resultSet = null;
        try {
            resultSet = batterySwapPointService.retrieveBatterySwapPointBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point, GET, SUCCESS",
                    loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point, GET, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultSet;
    }

    /** 단건 조회 (소속 교환충전기 목록 포함) */
    @RequestMapping(value = "/{cpId}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public BatterySwapPoint searchPointDetail(@PathVariable("cpId") String cpId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point/{}, GET",
                loginUser.getUserId(), accessIp, cpId);
        try {
            BatterySwapPoint point = batterySwapPointService.retrieveBatterySwapPoint(cpId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point/{}, GET, SUCCESS",
                    loginUser.getUserId(), accessIp, cpId);
            return point;
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point/{}, GET, FAIL",
                    loginUser.getUserId(), accessIp, cpId);
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /** CP_ID 중복 체크 (SUCCESS = 사용가능) */
    @RequestMapping(value = "/check/{cpId}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet checkCpId(@PathVariable("cpId") String cpId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point/check/{}, GET",
                loginUser.getUserId(), accessIp, cpId);
        try {
            BatterySwapPoint point = batterySwapPointService.retrieveBatterySwapPoint(cpId);
            if (point == null) {
                return new JsonResultSet(ResultStatus.SUCCESS);
            }
            return new JsonResultSet(ResultStatus.FAIL);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }

    /** 등록 */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet registerPoint(@RequestBody BatterySwapPoint point, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point, POST, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(point));
        try {
            point.setWriter(new Writer(loginUser.getUserId()));
            batterySwapPointService.registerBatterySwapPoint(point);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point, POST, SUCCESS",
                    loginUser.getUserId(), accessIp);
            return new JsonResultSet(ResultStatus.SUCCESS, point.getCpId());
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point, POST, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 수정 */
    @RequestMapping(value = "/{cpId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet updatePoint(@PathVariable("cpId") String cpId,
                                     @RequestBody BatterySwapPoint point,
                                     HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point/{}, PUT, DATA : {}",
                loginUser.getUserId(), accessIp, cpId, new Gson().toJson(point));
        try {
            point.setCpId(cpId);
            point.setWriter(new Writer(loginUser.getUserId()));
            batterySwapPointService.modifyBatterySwapPoint(point);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point/{}, PUT, SUCCESS",
                    loginUser.getUserId(), accessIp, cpId);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point/{}, PUT, FAIL",
                    loginUser.getUserId(), accessIp, cpId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 삭제 (소속 교환충전기까지 함께 제거) */
    @RequestMapping(value = "/{cpId}", method = RequestMethod.DELETE)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet deletePoint(@PathVariable("cpId") String cpId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point/{}, DELETE",
                loginUser.getUserId(), accessIp, cpId);
        try {
            batterySwapPointService.removeBatterySwapPoint(cpId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point/{}, DELETE, SUCCESS",
                    loginUser.getUserId(), accessIp, cpId);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/point/{}, DELETE, FAIL",
                    loginUser.getUserId(), accessIp, cpId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

}
