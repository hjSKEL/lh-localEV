/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.batteryswap;

import java.util.Map;

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
import kr.co.kevit.localcsms.batteryswap.entity.domain.SwapSlotStatus;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusDto;
import kr.co.kevit.localcsms.batteryswap.entity.shared.SwapSlotStatusSearchCond;
import kr.co.kevit.localcsms.batteryswap.process.SwapSlotStatusService;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 배터리 슬롯 현황(Read-only) REST 엔드포인트
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@RestController
@RequestMapping("ws/batterySwap/slot")
public class SwapSlotStatusResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwapSlotStatusResource.class);

    @Autowired
    private SwapSlotStatusService swapSlotStatusService;

    /** 목록 조회 */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<SwapSlotStatusDto> searchSlotList(SwapSlotStatusSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/slot, GET, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<SwapSlotStatusDto> resultSet = null;
        try {
            resultSet = swapSlotStatusService.retrieveSwapSlotStatusBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/slot, GET, SUCCESS",
                    loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/slot, GET, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultSet;
    }

    /** 상태별 카운트 — 칩 데이터 */
    @RequestMapping(value = "/count-by-state", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Map<String, Integer> searchSlotCountByState(SwapSlotStatusSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/slot/count-by-state, GET, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        try {
            // 상태 필터는 칩 카운트에 무의미하므로 비워서 호출
            searchCond.setSlotState(null);
            Map<String, Integer> result = swapSlotStatusService.retrieveSlotStateCountBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/slot/count-by-state, GET, SUCCESS",
                    loginUser.getUserId(), accessIp);
            return result;
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/slot/count-by-state, GET, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /** 단건 조회 */
    @RequestMapping(value = "/{cpId}/{csId}/{evseId}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public SwapSlotStatus searchSlotDetail(@PathVariable("cpId") String cpId,
                                           @PathVariable("csId") String csId,
                                           @PathVariable("evseId") int evseId,
                                           HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/batterySwap/slot/{}/{}/{}, GET",
                loginUser.getUserId(), accessIp, cpId, csId, evseId);
        try {
            return swapSlotStatusService.retrieveSwapSlotStatus(cpId, csId, evseId);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

}
