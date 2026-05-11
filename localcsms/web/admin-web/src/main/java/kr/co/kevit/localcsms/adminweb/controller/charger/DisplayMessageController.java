package kr.co.kevit.localcsms.adminweb.controller.charger;

import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.AccessLog;
import kr.co.kevit.localcsms.common.process.AccessLogService;
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
 * TB_CHDM001 - 충전기 디스플레이 메시지 페이지 Controller
 * GET /charger/displayMessage/list   - 목록 페이지
 * GET /charger/displayMessage/detail - 상세/등록 페이지 (mode=new 이면 신규 등록)
 */
@Controller
@RequestMapping("charger/displayMessage")
public class DisplayMessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayMessageController.class);

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public ModelAndView list(HttpServletRequest req,
                             String cpId, String csId) {
        LOGGER.debug("/charger/displayMessage/list cpId={} csId={}", cpId, csId);
        registerAccessLog(req, "/charger/displayMessage/list");
        ModelAndView mav = new ModelAndView("charger/displayMessage/list");
        mav.addObject("cpId", cpId != null ? cpId : "");
        mav.addObject("csId", csId != null ? csId : "");
        return mav;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public ModelAndView detail(HttpServletRequest req,
                               Integer messageId, String cpId, String csId,
                               String mode) {
        LOGGER.debug("/charger/displayMessage/detail messageId={} cpId={} csId={} mode={}", messageId, cpId, csId, mode);
        registerAccessLog(req, "/charger/displayMessage/detail");
        ModelAndView mav = new ModelAndView("charger/displayMessage/detail");
        mav.addObject("messageId", messageId != null ? messageId : 0);
        mav.addObject("cpId", cpId != null ? cpId : "");
        mav.addObject("csId", csId != null ? csId : "");
        mav.addObject("isNew", "new".equals(mode) || messageId == null);
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
