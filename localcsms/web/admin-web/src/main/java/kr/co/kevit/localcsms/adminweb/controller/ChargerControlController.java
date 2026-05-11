package kr.co.kevit.localcsms.adminweb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 충전기 원격 제어 페이지 컨트롤러.
 */
@Controller
@RequestMapping("charger/control")
public class ChargerControlController {

    private static final Logger log = LoggerFactory.getLogger(ChargerControlController.class);

    @GetMapping("/ocpp16")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public String ocpp16ControlPage(HttpServletRequest request) {
        log.debug("/charger/control/ocpp16");
        return "charger/control/ocpp16Control";
    }
}
