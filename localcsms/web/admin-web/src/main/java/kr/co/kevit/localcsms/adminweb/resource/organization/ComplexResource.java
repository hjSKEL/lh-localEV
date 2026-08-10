/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.organization;

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
import kr.co.kevit.localcsms.organization.entity.domain.Complex;
import kr.co.kevit.localcsms.organization.entity.shared.ComplexSearchCond;
import kr.co.kevit.localcsms.organization.process.ComplexService;

/**
 * 조직 - 단지 Resource
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 8. 10.
 */
@RestController
@RequestMapping("ws/organization/complex")
public class ComplexResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComplexResource.class);

    @Autowired
    private ComplexService complexService;

    /**
     * 단지 목록 검색(페이징)
     * @param searchCond
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public Page<Complex> searchComplexBySearchCond(ComplexSearchCond searchCond, HttpServletRequest request) {
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/search, GET, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(searchCond));
        Page<Complex> resultSet = null;
        try {
            resultSet = complexService.retrieveComplexBySearchCond(searchCond);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/search, GET, SUCCESS", loginUser.getUserId(), accessIp);
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/search, GET, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
        }
        return resultSet;
    }

    /**
     * 단지 상세조회
     * @param complexId
     * @return
     */
    @RequestMapping(value = "/retrieve/{complexId}", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public Complex searchComplexDetail(@PathVariable("complexId") String complexId, HttpServletRequest request) {
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/retrieve/{}, GET", loginUser.getUserId(), accessIp, complexId);
        Complex result = null;
        try {
            result = complexService.retrieveComplexById(complexId);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/retrieve/{}, GET, SUCCESS", loginUser.getUserId(), accessIp, complexId);
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/retrieve/{}, GET, FAIL", loginUser.getUserId(), accessIp, complexId);
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 단지 등록
     * @param complex
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet registerComplex(@RequestBody Complex complex, HttpServletRequest request) {
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/register, POST, DATA : {}", loginUser.getUserId(), accessIp, new Gson().toJson(complex));
        try {
            complex.setWriter(new Writer(loginUser.getUserId()));
            int result = complexService.registerComplex(complex);
            JsonResultSet resultSet = new JsonResultSet(result > 0 ? ResultStatus.SUCCESS : ResultStatus.FAIL);
            resultSet.setResult(complex);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/register, POST, SUCCESS", loginUser.getUserId(), accessIp);
            return resultSet;
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/register, POST, FAIL", loginUser.getUserId(), accessIp);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }

    /**
     * 단지 수정
     * @param complexId
     * @param complex
     * @return
     */
    @RequestMapping(value = "/modify/{complexId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet updateComplex(@PathVariable("complexId") String complexId, @RequestBody Complex complex, HttpServletRequest request) {
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/modify/{}, PUT, DATA : {}", loginUser.getUserId(), accessIp, complexId, new Gson().toJson(complex));
        try {
            complex.setComplexId(complexId);
            complex.setWriter(new Writer(loginUser.getUserId()));
            boolean result = complexService.modifyComplex(complex);
            JsonResultSet resultSet = new JsonResultSet(result ? ResultStatus.SUCCESS : ResultStatus.FAIL);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/modify/{}, PUT, SUCCESS", loginUser.getUserId(), accessIp, complexId);
            return resultSet;
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/modify/{}, PUT, FAIL", loginUser.getUserId(), accessIp, complexId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }

    /**
     * 단지 삭제
     * @param complexId
     * @return
     */
    @RequestMapping(value = "/remove/{complexId}", method = RequestMethod.DELETE)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet deleteComplex(@PathVariable("complexId") String complexId, HttpServletRequest request) {
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/remove/{}, DELETE", loginUser.getUserId(), accessIp, complexId);
        try {
            boolean result = complexService.removeComplex(complexId);
            JsonResultSet resultSet = new JsonResultSet(result ? ResultStatus.SUCCESS : ResultStatus.FAIL);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/remove/{}, DELETE, SUCCESS", loginUser.getUserId(), accessIp, complexId);
            return resultSet;
        } catch (Exception e) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/organization/complex/remove/{}, DELETE, FAIL", loginUser.getUserId(), accessIp, complexId);
            LOGGER.error(e.getMessage(), e);
            return new JsonResultSet(ResultStatus.FAIL);
        }
    }
}
