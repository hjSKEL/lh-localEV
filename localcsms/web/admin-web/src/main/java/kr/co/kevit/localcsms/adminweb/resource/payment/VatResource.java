/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.payment;

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
import kr.co.kevit.localcsms.payment.entity.domain.Vat;
import kr.co.kevit.localcsms.payment.entity.shared.VatDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.VatHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.VatSearchCond;
import kr.co.kevit.localcsms.payment.process.VatService;

/**
 * 사업자(VAT) REST 엔드포인트
 *
 * @author bckim
 * @since 2026. 5. 24.
 */
@RestController
@RequestMapping("ws/payment/vat")
public class VatResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(VatResource.class);

    @Autowired
    private VatService vatService;

    /** 목록 조회 */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<VatDto> searchVatList(VatSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/vat, GET, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        try {
            Page<VatDto> resultSet = vatService.retrieveVatBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/vat, GET, SUCCESS",
                    loginUser.getUserId(), accessIp);
            return resultSet;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /** 단건 조회 */
    @RequestMapping(value = "/{vatNo}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Vat searchVat(@PathVariable("vatNo") String vatNo, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER ID :{}, URL : ws/payment/vat/{}, GET", loginUser.getUserId(), vatNo);
        try {
            return vatService.retrieveVat(vatNo);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /** 중복 체크 (사용 가능 = SUCCESS) */
    @RequestMapping(value = "/check/{vatNo}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet checkVatNo(@PathVariable("vatNo") String vatNo, HttpServletRequest request) {
        try {
            Vat existing = vatService.retrieveVat(vatNo);
            return existing == null ? new JsonResultSet(ResultStatus.SUCCESS) : new JsonResultSet(ResultStatus.FAIL);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }

    /** 등록 */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet registerVat(@RequestBody Vat vat, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/vat, POST, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(vat));
        try {
            vat.setWriter(new Writer(loginUser.getUserId()));
            if (vat.getVerifySource() == null || vat.getVerifySource().isEmpty()) {
                vat.setVerifySource("MANUAL");
            }
            vatService.registerVat(vat);
            return new JsonResultSet(ResultStatus.SUCCESS, vat.getVatNo());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 수정 (전체 필드 갱신) */
    @RequestMapping(value = "/{vatNo}", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet modifyVat(@PathVariable("vatNo") String vatNo,
                                   @RequestBody Vat vat,
                                   HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/vat/{}, PUT, DATA : {}",
                loginUser.getUserId(), accessIp, vatNo, new Gson().toJson(vat));
        try {
            vat.setVatNo(vatNo);
            Writer writer = new Writer(loginUser.getUserId());
            vat.setWriter(writer);
            vatService.modifyVat(vat);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 사용여부 토글 */
    @RequestMapping(value = "/{vatNo}/use/{useYn}", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet changeUseYn(@PathVariable("vatNo") String vatNo,
                                     @PathVariable("useYn") String useYn,
                                     HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/vat/{}/use/{}, PUT",
                loginUser.getUserId(), accessIp, vatNo, useYn);
        try {
            vatService.modifyUseYn(vatNo, useYn, loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 검증 이력 목록 */
    @RequestMapping(value = "/his", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<VatHisDto> searchVatHisList(VatHisSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER ID :{}, URL : ws/payment/vat/his, GET, DATA : {}",
                loginUser.getUserId(), new Gson().toJson(searchCond));
        try {
            return vatService.retrieveVatHisBySearchCond(searchCond);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }
}
