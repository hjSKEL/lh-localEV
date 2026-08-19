/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.customer;

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
import kr.co.kevit.localcsms.customer.entity.domain.CustomerCard;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardDto;
import kr.co.kevit.localcsms.customer.entity.shared.CustomerCardSearchCond;
import kr.co.kevit.localcsms.customer.process.CustomerCardService;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 5. 16.
 */

/**
 * 
 * @author hjchang <a href="mailto:hjchang@kevit.co.kr">hjchang@kevit.co.kr</a>
 * @since 2026. 8. 19.
 */
@RestController
@RequestMapping("ws/customer/card")
public class CustomerCardResource extends AbstractResource{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCardResource.class);

    @Autowired
    private CustomerCardService customerCardService;

    /**
     * 怨좉컼移대뱶 ?섏젙
     *
     * @param customerId
     * @param customerCard
     * @return
     */
    @RequestMapping(value = "/save/{customerId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet updateCustomerCard(@PathVariable("customerId") String customerId,@RequestBody CustomerCard customerCard, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card/save/{}, PUT, DATA : {}", loginUser.getUserId(), accessIp, customerId, new Gson().toJson(customerCard));
        try {
            customerCard.setWriter(new Writer(loginUser.getUserId()));
            // customerCardService.modifyCustomerCardCompanyUser(customerCard);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card/save/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, customerId);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card/save/{}, PUT, FAIL", loginUser.getUserId(), accessIp, customerId);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<CustomerCardDto> searchCustomerCardList(CustomerCardSearchCond searchCond, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<CustomerCardDto> resultSet = null;
        try {
            resultSet = customerCardService.retrieveMemberCardByMemberCardSearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card, GET, SUCCESS", loginUser.getUserId(), accessIp);
        }catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card, GET, FAIL", loginUser.getUserId(), accessIp);
        }
        return resultSet;
    }

    /**
     * 移대뱶踰덊샇 泥댄겕
     *
     * @param cutCardNo
     * @return
     */
    @RequestMapping(value = "/check/{cutCardNo}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet checkCardNo(@PathVariable("cutCardNo") String cutCardNo, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card/check/{}, GET", loginUser.getUserId(), accessIp, cutCardNo);
        
        try {
            CustomerCard card = customerCardService.retrieveMemberCard(cutCardNo);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card, GET, SUCCESS", loginUser.getUserId(), accessIp);
            if(card == null) {
                return new JsonResultSet(ResultStatus.SUCCESS, "?ъ슜媛?ν븳 移대뱶踰덊샇 ?낅땲??");
            }
            return new JsonResultSet(ResultStatus.FAIL, "?깅줉??移대뱶踰덊샇 ?낅땲??");
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, "愿由ъ옄?먭쾶 臾몄쓽 ?섏꽭??");
        }
    }

    /**
     * 怨좉컼移대뱶 ?곹깭蹂寃?
     *
     * @param cutCardNo
     * @param custStatCode
     * @return
     */
    @RequestMapping(value = "/changeCustStatCode/{cutCardNo}/status/{custStatCode}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet changeCustStatCode(@PathVariable("cutCardNo") String cutCardNo,@PathVariable("custStatCode") String custStatCode, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card/changeCustStatCode/{}/status/{}, PUT", loginUser.getUserId(), accessIp, cutCardNo, custStatCode);
        try {
            CustomerCard customerCard = new CustomerCard();
            customerCard.setCutCardNo(cutCardNo);
            customerCard.setCustStatCode(custStatCode);
            customerCard.setWriter(new Writer(loginUser.getUserId()));
            customerCardService.modifyMemberCard(customerCard);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card/changeCustStatCode/{}/status/{}, PUT, FAIL", loginUser.getUserId(), accessIp, cutCardNo, custStatCode);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL);
        }
        LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card/changeCustStatCode/{}/status/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, cutCardNo, custStatCode);
        return new JsonResultSet(ResultStatus.SUCCESS);
    }

    /**
     * 怨좉컼移대뱶 ?깅줉
     *
     * @param customerCard
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet registerCustomerCard(@RequestBody CustomerCard customerCard, HttpServletRequest request){
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card, POST", loginUser.getUserId(), accessIp, new Gson().toJson(customerCard));
        try {
            customerCard.setWriter(new Writer(loginUser.getUserId()));
            customerCard.setCustStatCode("MEML01");
            customerCardService.registerMemberCard(customerCard);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
        LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/customer/card, POST, SUCCESS", loginUser.getUserId(), accessIp);
        return new JsonResultSet(ResultStatus.SUCCESS);
    }
}
