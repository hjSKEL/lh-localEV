package kr.co.kevit.localcsms.adminweb.resource.charger;

import com.google.gson.Gson;
import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.charger.entity.domain.CsConfiguration;
import kr.co.kevit.localcsms.charger.entity.shared.CsConfigurationSearchCond;
import kr.co.kevit.localcsms.charger.process.CsConfigurationService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 충전기 설정정보 (OCPP 1.6) REST Resource
 * GET /ws/charger/csConfiguration/list    - 페이지 목록
 * GET /ws/charger/csConfiguration/all     - 전체 목록
 */
@RestController
@RequestMapping("ws/charger/csConfiguration")
public class CsConfigurationResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsConfigurationResource.class);

    @Autowired
    private CsConfigurationService csConfigurationService;

    /** 조건 검색 (페이지 처리) */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<CsConfiguration> searchList(CsConfigurationSearchCond cond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/charger/csConfiguration/list DATA:{}", loginUser.getUserId(), new Gson().toJson(cond));
        try {
            Page<CsConfiguration> result = csConfigurationService.retrieveBySearchCond(cond);
            LOGGER.info("[RES] USER:{} URL:ws/charger/csConfiguration/list SUCCESS count:{}", loginUser.getUserId(), result.getCriteria().getTotalItemCount());
            return result;
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/charger/csConfiguration/list FAIL error={}", e.getMessage(), e);
            return null;
        }
    }

    /** 충전기 전체 설정 조회 */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public List<CsConfiguration> searchAll(String cpId, String csId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/charger/csConfiguration/all cpId={} csId={}", loginUser.getUserId(), cpId, csId);
        try {
            return csConfigurationService.retrieveByCpIdAndCsId(cpId, csId);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/charger/csConfiguration/all FAIL error={}", e.getMessage(), e);
            return null;
        }
    }
}
