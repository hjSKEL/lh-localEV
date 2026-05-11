package kr.co.kevit.localcsms.adminweb.controller.charger;

import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.process.AccessLogService;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 충전기 설정정보 (OCPP 1.6) 페이지 Controller
 * GET /charger/csConfiguration/list?cpId=&csId=
 */
@Controller
@RequestMapping("charger/csConfiguration")
public class CsConfigurationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsConfigurationController.class);

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public ModelAndView csConfigurationList(HttpServletRequest req, String cpId, String csId) {
        LOGGER.debug("/charger/csConfiguration/list cpId={} csId={}", cpId, csId);
        registerAccessLog(req, "/charger/csConfiguration/list");
        ModelAndView mav = new ModelAndView("charger/csConfiguration/csConfigurationList");
        mav.addObject("cpId", cpId != null ? cpId : "");
        mav.addObject("csId", csId != null ? csId : "");
        return mav;
    }

    private void registerAccessLog(HttpServletRequest req, String url) {
        String userIPAddress = req.getHeader("X-Forwarded-For");
        if (userIPAddress == null) userIPAddress = req.getHeader("X-FORWARDED-FOR");
        if (userIPAddress == null) userIPAddress = req.getRemoteAddr();
        String contextPath = req.getContextPath();
        User loginUser = SessionManager.getLoginUser();
        AccessLog accessLog = new AccessLog(loginUser.getUserId(), contextPath + url, "WEB", userIPAddress);
        accessLogService.registerAccessLog(accessLog);
    }
}
