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
import kr.co.kevit.localcsms.batteryswap.entity.domain.BatterySwapStation;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.BatterySwapStationSearchCond;
import kr.co.kevit.localcsms.batteryswap.process.BatterySwapStationService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 배터리 교환 충전기 REST 엔드포인트
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@RestController
@RequestMapping("ws/batterySwap/station")
public class BatterySwapStationResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatterySwapStationResource.class);

    @Autowired
    private BatterySwapStationService batterySwapStationService;

    /** 목록 조회 (전체) */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<BatterySwapStationDto> searchStationList(BatterySwapStationSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station, GET, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<BatterySwapStationDto> resultSet = null;
        try {
            resultSet = batterySwapStationService.retrieveBatterySwapStationBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station, GET, SUCCESS",
                    loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station, GET, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultSet;
    }

    /** 단건 조회 */
    @RequestMapping(value = "/{cpId}/{csId}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public BatterySwapStation searchStationDetail(@PathVariable("cpId") String cpId,
                                                  @PathVariable("csId") String csId,
                                                  HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station/{}/{}, GET",
                loginUser.getUserId(), accessIp, cpId, csId);
        try {
            return batterySwapStationService.retrieveBatterySwapStation(cpId, csId);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /** 등록 */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet registerStation(@RequestBody BatterySwapStation station, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station, POST, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(station));
        try {
            station.setWriter(new Writer(loginUser.getUserId()));
            batterySwapStationService.registerBatterySwapStation(station);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station, POST, SUCCESS",
                    loginUser.getUserId(), accessIp);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station, POST, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 수정 */
    @RequestMapping(value = "/{cpId}/{csId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet updateStation(@PathVariable("cpId") String cpId,
                                       @PathVariable("csId") String csId,
                                       @RequestBody BatterySwapStation station,
                                       HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station/{}/{}, PUT, DATA : {}",
                loginUser.getUserId(), accessIp, cpId, csId, new Gson().toJson(station));
        try {
            station.setCpId(cpId);
            station.setCsId(csId);
            station.setWriter(new Writer(loginUser.getUserId()));
            batterySwapStationService.modifyBatterySwapStation(station);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station/{}/{}, PUT, SUCCESS",
                    loginUser.getUserId(), accessIp, cpId, csId);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station/{}/{}, PUT, FAIL",
                    loginUser.getUserId(), accessIp, cpId, csId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 삭제 */
    @RequestMapping(value = "/{cpId}/{csId}", method = RequestMethod.DELETE)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet deleteStation(@PathVariable("cpId") String cpId,
                                       @PathVariable("csId") String csId,
                                       HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station/{}/{}, DELETE",
                loginUser.getUserId(), accessIp, cpId, csId);
        try {
            batterySwapStationService.removeBatterySwapStation(cpId, csId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station/{}/{}, DELETE, SUCCESS",
                    loginUser.getUserId(), accessIp, cpId, csId);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/station/{}/{}, DELETE, FAIL",
                    loginUser.getUserId(), accessIp, cpId, csId);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

}
