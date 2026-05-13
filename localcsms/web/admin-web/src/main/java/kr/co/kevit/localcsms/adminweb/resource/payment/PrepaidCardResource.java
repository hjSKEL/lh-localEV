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
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardSearchCond;
import kr.co.kevit.localcsms.payment.process.PrepaidCardService;

/**
 * 선불카드 / 선불카드 거래이력 REST 엔드포인트
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
@RestController
@RequestMapping("ws/payment/prepaidCard")
public class PrepaidCardResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrepaidCardResource.class);

    @Autowired
    private PrepaidCardService prepaidCardService;

    /**
     * 선불카드 목록 조회
     */
    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<PrepaidCardDto> searchPrepaidCardList(PrepaidCardSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard, GET, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<PrepaidCardDto> resultSet = null;
        try {
            resultSet = prepaidCardService.retrievePrepaidCardBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard, GET, SUCCESS",
                    loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard, GET, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultSet;
    }

    /**
     * 선불카드 단건 조회
     */
    @RequestMapping(value = "/{cardNo}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public PrepaidCard searchPrepaidCard(@PathVariable("cardNo") String cardNo, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard/{}, GET",
                loginUser.getUserId(), accessIp, cardNo);
        try {
            PrepaidCard card = prepaidCardService.retrievePrepaidCard(cardNo);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard/{}, GET, SUCCESS",
                    loginUser.getUserId(), accessIp, cardNo);
            return card;
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard/{}, GET, FAIL",
                    loginUser.getUserId(), accessIp, cardNo);
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 카드번호 중복 체크
     *
     * @return SUCCESS = 사용 가능, FAIL = 이미 등록된 카드
     */
    @RequestMapping(value = "/check/{cardNo}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet checkCardNo(@PathVariable("cardNo") String cardNo, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard/check/{}, GET",
                loginUser.getUserId(), accessIp, cardNo);
        try {
            PrepaidCard card = prepaidCardService.retrievePrepaidCard(cardNo);
            if (card == null) {
                return new JsonResultSet(ResultStatus.SUCCESS);
            }
            return new JsonResultSet(ResultStatus.FAIL);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }

    /**
     * 선불카드 발급
     */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet issuePrepaidCard(@RequestBody PrepaidCard card, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard, POST, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(card));
        try {
            card.setWriter(new Writer(loginUser.getUserId()));
            prepaidCardService.issuePrepaidCard(card);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard, POST, SUCCESS",
                    loginUser.getUserId(), accessIp);
            return new JsonResultSet(ResultStatus.SUCCESS, card.getCardNo());
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard, POST, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /**
     * 선불카드 상태 변경 (활성/정지/만료 토글)
     */
    @RequestMapping(value = "/{cardNo}/status/{cardStatCode}", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet changeCardStatus(@PathVariable("cardNo") String cardNo,
                                          @PathVariable("cardStatCode") String cardStatCode,
                                          HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard/{}/status/{}, PUT",
                loginUser.getUserId(), accessIp, cardNo, cardStatCode);
        try {
            prepaidCardService.modifyCardStatus(cardNo, cardStatCode, loginUser.getUserId());
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard/{}/status/{}, PUT, SUCCESS",
                    loginUser.getUserId(), accessIp, cardNo, cardStatCode);
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard/{}/status/{}, PUT, FAIL",
                    loginUser.getUserId(), accessIp, cardNo, cardStatCode);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /**
     * 선불카드 거래이력 목록 조회
     */
    @RequestMapping(value = "/his", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<PrepaidCardHisDto> searchPrepaidCardHisList(PrepaidCardHisSearchCond searchCond, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard/his, GET, DATA : {}",
                loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<PrepaidCardHisDto> resultSet = null;
        try {
            resultSet = prepaidCardService.retrievePrepaidCardHisBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard/his, GET, SUCCESS",
                    loginUser.getUserId(), accessIp);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/payment/prepaidCard/his, GET, FAIL",
                    loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
        }
        return resultSet;
    }

}
