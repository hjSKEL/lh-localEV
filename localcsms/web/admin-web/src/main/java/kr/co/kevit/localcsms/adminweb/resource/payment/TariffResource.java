/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.payment;

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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import kr.co.kevit.localcsms.adminweb.client.ApiEaiClient;
import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.Tariff;
import kr.co.kevit.localcsms.payment.entity.domain.TariffAssignment;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffAssignmentSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.TariffDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.TariffHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.TariffSearchCond;
import kr.co.kevit.localcsms.payment.process.TariffService;

/**
 * Tariff REST 엔드포인트.
 *
 * <p>경로:<br>
 * /ws/payment/tariff           — master CRUD<br>
 * /ws/payment/tariff/assignment — assignment CRUD<br>
 * /ws/payment/tariff/his        — history</p>
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
@RestController
@RequestMapping("ws/payment/tariff")
public class TariffResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffResource.class);

    @Autowired
    private TariffService tariffService;

    @Autowired
    private ApiEaiClient apiEaiClient;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private static final DateTimeFormatter ISO_SECONDS_UTC =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);

    // ============================= MASTER =============================

    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<TariffDto> searchTariffList(TariffSearchCond searchCond, HttpServletRequest request) {
        try {
            return tariffService.retrieveTariffBySearchCond(searchCond);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    @RequestMapping(value = "/{tariffId}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Tariff searchTariff(@PathVariable("tariffId") String tariffId, HttpServletRequest request) {
        try {
            return tariffService.retrieveTariff(tariffId);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    @RequestMapping(value = "/nextId/{kind}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet nextTariffId(@PathVariable("kind") String kind, HttpServletRequest request) {
        try {
            return new JsonResultSet(ResultStatus.SUCCESS, tariffService.generateNextTariffId(kind));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet registerTariff(@RequestBody Tariff tariff, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER ID :{}, URL: ws/payment/tariff, POST, DATA: {}",
                loginUser.getUserId(), new Gson().toJson(tariff));
        try {
            Tariff result = tariffService.registerTariff(tariff, loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS, result.getTariffId());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** 폐기 (REPLACED/CLEARED 등). body: { newStatus, reason } */
    @RequestMapping(value = "/{tariffId}/deprecate", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet deprecateTariff(@PathVariable("tariffId") String tariffId,
                                         @RequestBody Map<String, Object> body, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        try {
            String newStatus = (String) body.get("newStatus");
            String reason    = (String) body.get("reason");
            if (newStatus == null || newStatus.isEmpty()) newStatus = Tariff.STATUS_REPLACED;
            tariffService.deprecateTariff(tariffId, newStatus, reason, loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    // ============================= ASSIGNMENT =============================

    @RequestMapping(value = "/assignment", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<TariffAssignmentDto> searchAssignmentList(TariffAssignmentSearchCond cond, HttpServletRequest request) {
        try {
            return tariffService.retrieveAssignmentBySearchCond(cond);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /** Driver Tariff 매핑. body: { tariffId, customerId } */
    @RequestMapping(value = "/assignment/driver", method = RequestMethod.POST)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet assignDriver(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        try {
            String tariffId   = (String) body.get("tariffId");
            String customerId = (String) body.get("customerId");
            TariffAssignment a = tariffService.assignDriverTariff(tariffId, customerId, loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS, String.valueOf(a.getSeq()));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** Driver Tariff 해제. */
    @RequestMapping(value = "/assignment/driver/{customerId}/clear", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet clearDriver(@PathVariable("customerId") String customerId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        try {
            tariffService.clearDriverTariff(customerId, loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** Default Tariff 매핑(PENDING). body: { tariffId, cpId, csId, evseId } */
    @RequestMapping(value = "/assignment/default", method = RequestMethod.POST)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet assignDefault(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        try {
            String tariffId = (String) body.get("tariffId");
            String cpId     = (String) body.get("cpId");
            String csId     = (String) body.get("csId");
            Integer evseId  = body.get("evseId") == null ? null
                    : (body.get("evseId") instanceof Number
                            ? ((Number) body.get("evseId")).intValue()
                            : Integer.parseInt(body.get("evseId").toString()));
            TariffAssignment a = tariffService.assignDefaultTariffPending(tariffId, cpId, csId, evseId,
                    loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS, String.valueOf(a.getSeq()));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** Default Tariff push — SetDefaultTariffRequest 를 CS 로 전송 + 결과 ACK 반영 */
    @RequestMapping(value = "/assignment/{seq}/push", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet pushDefaultTariff(@PathVariable("seq") long seq, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        try {
            TariffAssignment a = tariffService.retrieveAssignment(seq);
            if (a == null) {
                return new JsonResultSet(ResultStatus.FAIL, "assignment 없음: " + seq);
            }
            if (!TariffAssignment.TYPE_DEFAULT_EVSE.equals(a.getAssignType())) {
                return new JsonResultSet(ResultStatus.FAIL, "DEFAULT_EVSE 만 push 가능");
            }
            Tariff t = tariffService.retrieveTariff(a.getTariffId());
            if (t == null || t.getTariffJson() == null) {
                return new JsonResultSet(ResultStatus.FAIL, "Tariff 마스터 없음 또는 JSON 비어있음");
            }

            // tariffJson → Map 파싱 후 tariffId/currency/validFrom 보강
            @SuppressWarnings("unchecked")
            Map<String, Object> tariffMap = objectMapper.readValue(t.getTariffJson(), Map.class);
            tariffMap.put("tariffId", t.getTariffId());
            tariffMap.put("currency", t.getCurrency());
            if (t.getValidFrom() != null) {
                tariffMap.put("validFrom", ISO_SECONDS_UTC.format(t.getValidFrom().toInstant()));
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("evseId", a.getEvseId() != null ? a.getEvseId() : 0);
            payload.put("tariff", tariffMap);

            String cpCsId = a.getCpId() + "-" + a.getCsId();
            LOGGER.info("[REQ] USER ID :{}, URL: ws/payment/tariff/assignment/{}/push, action=SetDefaultTariff, cpCsId={}, data={}",
                    loginUser.getUserId(), seq, cpCsId, new Gson().toJson(payload));

            Map<String, Object> result = apiEaiClient.send2x(cpCsId, "SetDefaultTariff", payload);
            String csStatus = result == null ? "rejected" : String.valueOf(result.get("status"));
            String csMessage = result == null ? null : (String) result.get("message");

            // accepted = CALL 전송 성공 (실제 CS 응답은 SetDefaultTariffBean 가 async 수신)
            // 운영 단순화: 전송 성공 시 optimistic ACTIVE 처리
            tariffService.acknowledgeDefaultTariffPush(seq,
                    "accepted".equalsIgnoreCase(csStatus) ? "Accepted" : "Rejected",
                    csMessage, loginUser.getUserId());

            if ("accepted".equalsIgnoreCase(csStatus)) {
                return new JsonResultSet(ResultStatus.SUCCESS);
            }
            return new JsonResultSet(ResultStatus.FAIL, csMessage);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    // ============================= HISTORY =============================

    @RequestMapping(value = "/his", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<TariffHisDto> searchHis(TariffHisSearchCond cond, HttpServletRequest request) {
        try {
            return tariffService.retrieveHisBySearchCond(cond);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }
}
