package kr.co.kevit.localcsms.adminweb.resource.system;

import com.google.gson.Gson;
import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.system.entity.domain.RemoteLog;
import kr.co.kevit.localcsms.system.entity.shared.RemoteLogSearchCond;
import kr.co.kevit.localcsms.system.process.RemoteLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 원격명령 로그 REST Resource
 */
@RestController
@RequestMapping("ws/system/remoteLog")
public class RemoteLogResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteLogResource.class);

    @Autowired
    private RemoteLogService remoteLogService;

    /** 조건 검색 (페이지 처리) */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN"})
    public Page<RemoteLog> searchList(RemoteLogSearchCond cond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/system/remoteLog/list DATA:{}", loginUser.getUserId(), new Gson().toJson(cond));
        try {
            Page<RemoteLog> result = remoteLogService.retrieveRemoteLogBySearchCond(cond);
            LOGGER.info("[RES] USER:{} URL:ws/system/remoteLog/list SUCCESS count:{}", loginUser.getUserId(), result.getCriteria().getTotalItemCount());
            return result;
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/system/remoteLog/list FAIL error={}", e.getMessage(), e);
            return null;
        }
    }

    /** 단건 조회 */
    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN"})
    public RemoteLog detail(@PathVariable String uuid, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/system/remoteLog/{}", loginUser.getUserId(), uuid);
        try {
            return remoteLogService.retrieveRemoteLogByUuid(uuid);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/system/remoteLog/{} FAIL error={}", uuid, e.getMessage(), e);
            return null;
        }
    }

    /** 단건 삭제 */
    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    @Secured({"ROLE_ADMIN"})
    public Map<String, String> delete(@PathVariable String uuid, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/system/remoteLog/{} DELETE", loginUser.getUserId(), uuid);
        Map<String, String> result = new HashMap<>();
        try {
            remoteLogService.removeRemoteLog(uuid);
            result.put("status", "SUCCESS");
            LOGGER.info("[RES] USER:{} URL:ws/system/remoteLog/{} DELETE SUCCESS", loginUser.getUserId(), uuid);
        } catch (Exception e) {
            result.put("status", "FAIL");
            result.put("message", e.getMessage());
            LOGGER.error("[RES] URL:ws/system/remoteLog/{} DELETE FAIL error={}", uuid, e.getMessage(), e);
        }
        return result;
    }
}
