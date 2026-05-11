package kr.co.kevit.localcsms.adminweb.resource.charger;

import com.google.gson.Gson;
import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.charger.entity.domain.CsFirmware;
import kr.co.kevit.localcsms.charger.entity.shared.CsFirmwareSearchCond;
import kr.co.kevit.localcsms.charger.process.CsFirmwareService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TB_CHFW001 - 충전기 펌웨어 업데이트 REST Resource
 *
 * GET /ws/firmware/list - 페이지 목록 (cpId, csId, status 선택)
 */
@RestController
@RequestMapping("ws/firmware")
public class CsFirmwareResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsFirmwareResource.class);

    @Autowired
    private CsFirmwareService csFirmwareService;

    @GetMapping("/list")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<CsFirmware> searchList(CsFirmwareSearchCond cond) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/firmware/list DATA:{}", loginUser.getUserId(), new Gson().toJson(cond));
        try {
            Page<CsFirmware> result = csFirmwareService.retrieveBySearchCond(cond);
            LOGGER.info("[RES] USER:{} URL:ws/firmware/list SUCCESS count:{}", loginUser.getUserId(), result.getCriteria().getTotalItemCount());
            return result;
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/firmware/list FAIL error={}", e.getMessage(), e);
            return null;
        }
    }
}
