/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.payment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.PspPayment;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentSearchCond;
import kr.co.kevit.localcsms.payment.process.PspPaymentService;

/**
 * PSP 결제 REST 엔드포인트
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
@RestController
@RequestMapping("ws/payment/psp")
public class PspPaymentResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PspPaymentResource.class);

    @Autowired
    private PspPaymentService pspPaymentService;

    /** 목록 조회 */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<PspPaymentDto> searchPspPaymentList(PspPaymentSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/psp, GET, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        try {
            return pspPaymentService.retrievePspPaymentBySearchCond(searchCond);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /** 단건 조회 */
    @RequestMapping(value = "/{pspRef}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public PspPayment searchPspPayment(@PathVariable("pspRef") String pspRef, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER ID :{}, URL : ws/payment/psp/{}, GET", loginUser.getUserId(), pspRef);
        try {
            return pspPaymentService.retrievePspPayment(pspRef);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /** 다음 PSP Ref 미리보기 */
    @RequestMapping(value = "/nextRef", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet nextPspRef(HttpServletRequest request) {
        try {
            String ref = pspPaymentService.generateNextPspRef();
            return new JsonResultSet(ResultStatus.SUCCESS, ref);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 결제 인증 등록 (AUTHORIZED) */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet registerPspPayment(@RequestBody PspPayment payment, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/psp, POST, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(payment));
        try {
            payment.setWriter(new Writer(loginUser.getUserId()));
            PspPayment result = pspPaymentService.registerPspPayment(payment);
            return new JsonResultSet(ResultStatus.SUCCESS, result.getPspRef());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 정산 처리 (수동) */
    @RequestMapping(value = "/{pspRef}/settle", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet settle(@PathVariable("pspRef") String pspRef,
                                @RequestBody Map<String, Object> body,
                                HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/psp/{}/settle, PUT, DATA : {}",
                loginUser.getUserId(), accessIp, pspRef, new Gson().toJson(body));
        try {
            BigDecimal settledAmount = toBigDecimal(body.get("settledAmount"));
            String receiptUrl = (String) body.get("receiptUrl");
            String receiptId  = (String) body.get("receiptId");
            pspPaymentService.settle(pspRef, settledAmount, receiptUrl, receiptId, loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 취소 처리 */
    @RequestMapping(value = "/{pspRef}/cancel", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet cancel(@PathVariable("pspRef") String pspRef,
                                @RequestBody Map<String, Object> body,
                                HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/psp/{}/cancel, PUT, DATA : {}",
                loginUser.getUserId(), accessIp, pspRef, new Gson().toJson(body));
        try {
            String reasonCd = body == null ? null : (String) body.get("reasonCd");
            String remark   = body == null ? null : (String) body.get("remark");
            pspPaymentService.cancel(pspRef, reasonCd, remark, loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 상태 이력 목록 */
    @RequestMapping(value = "/his", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<PspPaymentHisDto> searchPspPaymentHisList(PspPaymentHisSearchCond searchCond,
                                                          HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER ID :{}, URL : ws/payment/psp/his, GET, DATA : {}",
                loginUser.getUserId(), new Gson().toJson(searchCond));
        try {
            return pspPaymentService.retrievePspPaymentHisBySearchCond(searchCond);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    private BigDecimal toBigDecimal(Object o) {
        if (o == null) return null;
        if (o instanceof BigDecimal) return (BigDecimal) o;
        if (o instanceof Number) return new BigDecimal(o.toString());
        return new BigDecimal(String.valueOf(o));
    }

    @SuppressWarnings("unused")
    private Map<String, Object> emptyMap() {
        return new HashMap<>();
    }
}
