/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
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
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusHisDto;
import kr.co.kevit.localcsms.charger.entity.shared.SwapSlotStatusHisSearchCond;
import kr.co.kevit.localcsms.charger.process.SwapSlotStatusHisService;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * 배터리 슬롯 상태이력(Read-only) REST 엔드포인트
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@RestController
@RequestMapping("ws/charger/swapSlot/his")
public class SwapSlotStatusHisResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwapSlotStatusHisResource.class);

    @Autowired
    private SwapSlotStatusHisService swapSlotStatusHisService;

    /** 이력 목록 조회 */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<SwapSlotStatusHisDto> searchHisList(SwapSlotStatusHisSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/swapSlot/his, GET, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<SwapSlotStatusHisDto> resultSet = null;
        try {
            resultSet = swapSlotStatusHisService.retrieveSwapSlotStatusHisBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/swapSlot/his, GET, SUCCESS",
                    loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/charger/swapSlot/his, GET, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultSet;
    }

}
