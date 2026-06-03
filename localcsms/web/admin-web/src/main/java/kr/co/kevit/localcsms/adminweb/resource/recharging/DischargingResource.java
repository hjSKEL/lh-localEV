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
import kr.co.kevit.localcsms.recharger.entity.shared.DischargingDto;
import kr.co.kevit.localcsms.recharger.entity.shared.DischargingSearchCond;
import kr.co.kevit.localcsms.recharger.process.DischargingService;

/**
 * 방전현황(V2X) REST.
 */
@RestController
@RequestMapping("ws/recharging/discharging")
public class DischargingResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DischargingResource.class);

    @Autowired
    private DischargingService dischargingService;

    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<DischargingDto> searchList(DischargingSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER:{}, IP:{}, GET ws/recharging/discharging, DATA:{}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        return dischargingService.retrieveDischargingBySearchCond(searchCond);
    }

    @RequestMapping(value = "/{dcId}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public DischargingDto searchDetail(@PathVariable("dcId") String dcId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER:{}, IP:{}, GET ws/recharging/discharging/{}",
                loginUser.getUserId(), accessIp, dcId);
        return dischargingService.retrieveDischargingDtoById(dcId);
    }
}
