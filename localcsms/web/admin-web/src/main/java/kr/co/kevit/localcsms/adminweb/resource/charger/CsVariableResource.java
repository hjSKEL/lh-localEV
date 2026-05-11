package kr.co.kevit.localcsms.adminweb.resource.charger;

import com.google.gson.Gson;
import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.charger.entity.domain.CsVariable;
import kr.co.kevit.localcsms.charger.entity.shared.CsVariableSearchCond;
import kr.co.kevit.localcsms.charger.process.CsVariableService;
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
 * 충전기 설정 변수 (OCPP 2.x) REST Resource
 * GET /ws/charger/csVariable/list    - 페이지 목록
 * GET /ws/charger/csVariable/all     - 전체 목록 (검색 조건 없이 충전기 기준)
 */
@RestController
@RequestMapping("ws/charger/csVariable")
public class CsVariableResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsVariableResource.class);

    @Autowired
    private CsVariableService csVariableService;

    /** 조건 검색 (페이지 처리) */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<CsVariable> searchList(CsVariableSearchCond cond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/charger/csVariable/list DATA:{}", loginUser.getUserId(), new Gson().toJson(cond));
        try {
            Page<CsVariable> result = csVariableService.retrieveBySearchCond(cond);
            LOGGER.info("[RES] USER:{} URL:ws/charger/csVariable/list SUCCESS count:{}", loginUser.getUserId(), result.getCriteria().getTotalItemCount());
            return result;
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/charger/csVariable/list FAIL error={}", e.getMessage(), e);
            return null;
        }
    }

    /** 충전기 전체 변수 조회 (컴포넌트 목록 드롭다운용) */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public List<CsVariable> searchAll(String cpId, String csId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/charger/csVariable/all cpId={} csId={}", loginUser.getUserId(), cpId, csId);
        try {
            return csVariableService.retrieveByCpIdAndCsId(cpId, csId);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/charger/csVariable/all FAIL error={}", e.getMessage(), e);
            return null;
        }
    }
}
