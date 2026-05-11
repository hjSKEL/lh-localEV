/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller.charger;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 충전기 - 충전기 Controller
 *
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 4.
 */
@Controller
@RequestMapping("charger/chargingStation")
public class CSDrCmdController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSDrCmdController.class);
    
    @RequestMapping(value = "/dr/control", method = RequestMethod.GET)
    public String kev001Control(HttpServletRequest req) {
        //
        LOGGER.debug("/charger/chargingStation/dr/control");
        return "charger/csControl/OCPP16SchedControl";
    }
}
