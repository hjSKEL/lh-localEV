package kr.co.kevit.localcsms.adminweb.resource.charger;

import com.google.gson.Gson;
import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomer;
import kr.co.kevit.localcsms.charger.entity.domain.CsLocalCustomerToken;
import kr.co.kevit.localcsms.charger.entity.shared.CsLocalCustomerSearchCond;
import kr.co.kevit.localcsms.charger.process.CsLocalCustomerService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TB_CHLC001 / TB_CHLC002 - 충전기 로컬 인증 목록 REST Resource
 *
 * GET    /ws/localCustomer/list                          - 페이지 목록
 * GET    /ws/localCustomer/detail?cpId=&csId=            - 단건 조회 (토큰 포함)
 * POST   /ws/localCustomer                               - 헤더 등록
 * DELETE /ws/localCustomer?cpId=&csId=                   - 헤더 + 토큰 삭제
 * POST   /ws/localCustomer/token                         - 토큰 추가
 * DELETE /ws/localCustomer/token?cpId=&csId=&token=&tokenType= - 토큰 삭제
 */
@RestController
@RequestMapping("ws/localCustomer")
public class LocalCustomerResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalCustomerResource.class);

    @Autowired
    private CsLocalCustomerService csLocalCustomerService;

    /** 목록 조회 (페이지 처리) */
    @GetMapping("/list")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<CsLocalCustomer> searchList(CsLocalCustomerSearchCond cond) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/localCustomer/list DATA:{}", loginUser.getUserId(), new Gson().toJson(cond));
        try {
            Page<CsLocalCustomer> result = csLocalCustomerService.retrieveBySearchCond(cond);
            LOGGER.info("[RES] USER:{} URL:ws/localCustomer/list SUCCESS count:{}", loginUser.getUserId(), result.getCriteria().getTotalItemCount());
            return result;
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/localCustomer/list FAIL error={}", e.getMessage(), e);
            return null;
        }
    }

    /** 단건 조회 (토큰 목록 포함) */
    @GetMapping("/detail")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public CsLocalCustomer findOne(@RequestParam String cpId, @RequestParam String csId) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/localCustomer/detail cpId={} csId={}", loginUser.getUserId(), cpId, csId);
        return csLocalCustomerService.findOne(cpId, csId);
    }

    /** 헤더 등록 */
    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet register(@RequestBody CsLocalCustomer header) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/localCustomer POST cpId={} csId={}", loginUser.getUserId(), header.getCpId(), header.getCsId());
        try {
            header.setWriter(new Writer(loginUser.getUserId()));
            csLocalCustomerService.saveHeader(header);
            LOGGER.info("[RES] USER:{} URL:ws/localCustomer POST SUCCESS", loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/localCustomer POST FAIL error={}", e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }
    }

    /** 헤더 + 토큰 삭제 */
    @DeleteMapping
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet delete(@RequestParam String cpId, @RequestParam String csId) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/localCustomer DELETE cpId={} csId={}", loginUser.getUserId(), cpId, csId);
        try {
            csLocalCustomerService.deleteHeader(cpId, csId);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/localCustomer DELETE FAIL error={}", e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }
    }

    /** 토큰 추가 */
    @PostMapping("/token")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet addToken(@RequestBody CsLocalCustomerToken token) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/localCustomer/token POST cpId={} csId={} token={}", loginUser.getUserId(), token.getCpId(), token.getCsId(), token.getToken());
        try {
            csLocalCustomerService.addToken(token);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/localCustomer/token POST FAIL error={}", e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }
    }

    /** 토큰 삭제 */
    @DeleteMapping("/token")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet deleteToken(@RequestParam String cpId, @RequestParam String csId,
                                     @RequestParam String token, @RequestParam String tokenType) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/localCustomer/token DELETE cpId={} csId={} token={}", loginUser.getUserId(), cpId, csId, token);
        try {
            csLocalCustomerService.deleteToken(cpId, csId, token, tokenType);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/localCustomer/token DELETE FAIL error={}", e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }
    }
}
