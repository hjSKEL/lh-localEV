/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.recharging;

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
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.recharger.entity.domain.BatterySwapRecord;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordDto;
import kr.co.kevit.localcsms.recharger.entity.shared.BatterySwapRecordSearchCond;
import kr.co.kevit.localcsms.recharger.process.BatterySwapRecordService;

/**
 * 배터리 교체 기록 REST 엔드포인트 (Read-only)
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@RestController
@RequestMapping("ws/batterySwap/record")
public class BatterySwapRecordResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatterySwapRecordResource.class);

    @Autowired
    private BatterySwapRecordService batterySwapRecordService;

    /** 목록 조회 (페이징) */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<BatterySwapRecordDto> searchRecordList(BatterySwapRecordSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/record, GET, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<BatterySwapRecordDto> resultSet = null;
        try {
            resultSet = batterySwapRecordService.retrieveBatterySwapRecordBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/record, GET, SUCCESS",
                    loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/record, GET, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultSet;
    }

    /** 단건 조회 (디테일 포함, 페이징 없음) */
    @RequestMapping(value = "/{requestId}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public BatterySwapRecord searchRecordDetail(@PathVariable("requestId") Long requestId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/record/{}, GET",
                loginUser.getUserId(), accessIp, requestId);
        try {
            return batterySwapRecordService.retrieveBatterySwapRecord(requestId);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/record/{}, GET, FAIL",
                    loginUser.getUserId(), accessIp, requestId);
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

}
