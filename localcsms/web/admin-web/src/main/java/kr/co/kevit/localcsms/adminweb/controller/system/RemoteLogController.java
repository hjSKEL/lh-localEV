package kr.co.kevit.localcsms.adminweb.controller.system;

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
 * 원격명령 로그 페이지 Controller
 * GET /system/remoteLog/list
 */
@Controller
@RequestMapping("system/remoteLog")
public class RemoteLogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteLogController.class);

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN" })
    public ModelAndView remoteLogList(HttpServletRequest req) {
        LOGGER.debug("/system/remoteLog/list");
        registerAccessLog(req, "/system/remoteLog/list");
        return new ModelAndView("system/remoteLog/remoteLogList");
    }

    private void registerAccessLog(HttpServletRequest req, String url) {
        String userIPAddress = req.getHeader("X-Forwarded-For");
        if (userIPAddress == null)
            userIPAddress = req.getHeader("X-FORWARDED-FOR");
        if (userIPAddress == null)
            userIPAddress = req.getRemoteAddr();
        String contextPath = req.getContextPath();
        User loginUser = SessionManager.getLoginUser();
        AccessLog accessLog = new AccessLog(loginUser.getUserId(), contextPath + url, "WEB", userIPAddress);
        accessLogService.registerAccessLog(accessLog);
    }
}
