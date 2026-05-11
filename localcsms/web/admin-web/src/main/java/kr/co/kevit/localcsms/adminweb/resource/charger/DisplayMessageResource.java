package kr.co.kevit.localcsms.adminweb.resource.charger;

import com.google.gson.Gson;
import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessage;
import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessageContent;
import kr.co.kevit.localcsms.charger.entity.shared.DisplayMessageSearchCond;
import kr.co.kevit.localcsms.charger.process.DisplayMessageService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * TB_CHDM001 / TB_CHDM002 - 충전기 디스플레이 메시지 REST Resource
 *
 * GET    /ws/displayMessage/list              - 페이지 목록
 * GET    /ws/displayMessage/{messageId}       - 단건 조회 (contents 포함)
 * POST   /ws/displayMessage                   - 등록 (MSG_ID 자동 생성)
 * PUT    /ws/displayMessage/{messageId}       - 수정
 * DELETE /ws/displayMessage/{messageId}       - 삭제
 */
@RestController
@RequestMapping("ws/displayMessage")
public class DisplayMessageResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayMessageResource.class);

    @Autowired
    private DisplayMessageService displayMessageService;

    @Autowired
    private SequenceService sequenceService;

    /** 목록 조회 (페이지 처리) */
    @GetMapping("/list")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public Page<DisplayMessage> searchList(DisplayMessageSearchCond cond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/displayMessage/list DATA:{}", loginUser.getUserId(), new Gson().toJson(cond));
        try {
            Page<DisplayMessage> result = displayMessageService.retrieveBySearchCond(cond);
            LOGGER.info("[RES] USER:{} URL:ws/displayMessage/list SUCCESS count:{}", loginUser.getUserId(), result.getCriteria().getTotalItemCount());
            return result;
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/displayMessage/list FAIL error={}", e.getMessage(), e);
            return null;
        }
    }

    /** 단건 조회 (contents 포함) */
    @GetMapping("/{messageId}")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public DisplayMessage findOne(@PathVariable int messageId,
                                  @RequestParam String cpId,
                                  @RequestParam String csId) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/displayMessage/{} cpId={} csId={}", loginUser.getUserId(), messageId, cpId, csId);
        return displayMessageService.findOne(messageId, cpId, csId);
    }

    /** 등록 (MSG_ID 는 Sequence 자동 생성) */
    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet register(@RequestBody DisplayMessage message) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/displayMessage POST cpId={} csId={}", loginUser.getUserId(), message.getCpId(), message.getCsId());
        try {
            message.setMessageId(sequenceService.generateDisplayMessageSeq());
            message.setWriter(new Writer(loginUser.getUserId()));
            if (message.getContents() != null) {
                for (DisplayMessageContent c : message.getContents()) {
                    c.setMessageId(message.getMessageId());
                }
            }
            displayMessageService.saveMessageWithContents(message);
            LOGGER.info("[RES] USER:{} URL:ws/displayMessage POST SUCCESS messageId={}", loginUser.getUserId(), message.getMessageId());
            return new JsonResultSet(ResultStatus.SUCCESS, message.getMessageId());
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/displayMessage POST FAIL error={}", e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }
    }

    /** 수정 */
    @PutMapping("/{messageId}")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet update(@PathVariable int messageId,
                                @RequestBody DisplayMessage message) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/displayMessage/{} PUT", loginUser.getUserId(), messageId);
        try {
            message.setMessageId(messageId);
            message.setWriter(new Writer(loginUser.getUserId()));
            displayMessageService.updateMessage(message);
            if (message.getContents() != null) {
                for (DisplayMessageContent c : message.getContents()) {
                    c.setMessageId(messageId);
                    displayMessageService.saveContent(c);
                }
            }
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/displayMessage/{} PUT FAIL error={}", messageId, e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }
    }

    /** 삭제 */
    @DeleteMapping("/{messageId}")
    @Secured({"ROLE_ADMIN", "ROLE_OPER"})
    public JsonResultSet delete(@PathVariable int messageId,
                                @RequestParam String cpId,
                                @RequestParam String csId) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER:{} URL:ws/displayMessage/{} DELETE cpId={} csId={}", loginUser.getUserId(), messageId, cpId, csId);
        try {
            displayMessageService.deleteMessage(messageId, cpId, csId);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("[RES] URL:ws/displayMessage/{} DELETE FAIL error={}", messageId, e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL, e.getMessage());
        }
    }
}
