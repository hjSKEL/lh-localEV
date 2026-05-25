/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.derctrl;

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

import kr.co.kevit.localcsms.adminweb.client.ApiEaiClient;
import kr.co.kevit.localcsms.adminweb.resource.AbstractResource;
import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.share.JsonResultSet;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.derctrl.entity.domain.DerControl;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerAlarmDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerAlarmSearchCond;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlHisDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlHisSearchCond;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerControlSearchCond;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerStartStopDto;
import kr.co.kevit.localcsms.derctrl.entity.shared.DerStartStopSearchCond;
import kr.co.kevit.localcsms.derctrl.process.DerControlService;

/**
 * DER Control REST.
 *
 * <p>/ws/derctrl                — master CRUD<br>
 * /ws/derctrl/{id}/{origin}/push  — SetDERControl push<br>
 * /ws/derctrl/{id}/{origin}/clear — ClearDERControl push<br>
 * /ws/derctrl/get                — GetDERControl push<br>
 * /ws/derctrl/his                — history<br>
 * /ws/derctrl/alarm              — alarm list<br>
 * /ws/derctrl/startstop          — start/stop list</p>
 *
 * @author bckim
 * @since 2026. 5. 26.
 */
@RestController
@RequestMapping("ws/derctrl")
public class DerControlResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DerControlResource.class);

    @Autowired
    private DerControlService derControlService;

    @Autowired
    private ApiEaiClient apiEaiClient;

    private final Gson gson = new Gson();

    // ============================= MASTER =============================

    @RequestMapping(method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<DerControlDto> searchList(DerControlSearchCond cond, HttpServletRequest request) {
        try {
            return derControlService.retrieveDerControlBySearchCond(cond);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    @RequestMapping(value = "/{controlId}/{originCd}", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public DerControl searchOne(@PathVariable("controlId") String controlId,
                                @PathVariable("originCd") String originCd,
                                HttpServletRequest request) {
        try {
            return derControlService.retrieveDerControl(controlId, originCd);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    @RequestMapping(value = "/nextId", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet nextControlId(HttpServletRequest request) {
        try {
            return new JsonResultSet(ResultStatus.SUCCESS, derControlService.generateControlId());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet register(@RequestBody DerControl ctrl, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        LOGGER.info("[REQ] USER ID :{}, URL: ws/derctrl, POST, DATA: {}",
                loginUser.getUserId(), gson.toJson(ctrl));
        try {
            DerControl out = derControlService.registerDerControl(ctrl, loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS, out.getControlId());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    @RequestMapping(value = "/{controlId}/{originCd}/deprecate", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet deprecate(@PathVariable("controlId") String controlId,
                                   @PathVariable("originCd") String originCd,
                                   @RequestBody(required = false) Map<String, Object> body,
                                   HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        try {
            String newStatus = body == null ? null : (String) body.get("newStatus");
            String reason    = body == null ? null : (String) body.get("reason");
            if (newStatus == null || newStatus.isEmpty()) newStatus = DerControl.STATUS_REPLACED;
            derControlService.deprecate(controlId, originCd, newStatus, reason, loginUser.getUserId());
            return new JsonResultSet(ResultStatus.SUCCESS);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    // ============================= PUSH =============================

    /** SetDERControlRequest 송신 (CSMS → CS). assignment.controlId 의 paramJson 으로 송신. */
    @RequestMapping(value = "/{controlId}/push", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet pushSet(@PathVariable("controlId") String controlId, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        try {
            DerControl c = derControlService.retrieveDerControl(controlId, DerControl.ORIGIN_CSMS);
            if (c == null) return new JsonResultSet(ResultStatus.FAIL, "controlId 없음: " + controlId);

            Map<String, Object> payload = buildSetPayload(c);
            String cpCsId = c.getCpId() + "-" + c.getCsId();
            LOGGER.info("[DER-PUSH] cpCsId={} controlId={} payload={}",
                    cpCsId, controlId, gson.toJson(payload));

            Map<String, Object> result = apiEaiClient.send2x(cpCsId, "SetDERControl", payload);
            String csStatus = result == null ? "rejected" : String.valueOf(result.get("status"));
            String csMessage = result == null ? null : (String) result.get("message");

            derControlService.acknowledgeSetPush(controlId,
                    "accepted".equalsIgnoreCase(csStatus) ? "Accepted" : "Rejected",
                    csMessage, loginUser.getUserId());

            return "accepted".equalsIgnoreCase(csStatus)
                    ? new JsonResultSet(ResultStatus.SUCCESS)
                    : new JsonResultSet(ResultStatus.FAIL, csMessage);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** ClearDERControlRequest 송신. body: { isDefault, controlType, controlId(옵션) } */
    @RequestMapping(value = "/clear", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet pushClear(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        try {
            String cpId   = (String) body.get("cpId");
            String csId   = (String) body.get("csId");
            String cpCsId = cpId + "-" + csId;
            Map<String, Object> payload = new HashMap<>();
            if (body.get("isDefault") != null)   payload.put("isDefault",   Boolean.valueOf(body.get("isDefault").toString()));
            if (body.get("controlType") != null) payload.put("controlType", body.get("controlType"));
            if (body.get("controlId") != null)   payload.put("controlId",   body.get("controlId"));

            LOGGER.info("[DER-CLEAR] cpCsId={} payload={}", cpCsId, gson.toJson(payload));
            Map<String, Object> result = apiEaiClient.send2x(cpCsId, "ClearDERControl", payload);
            String csStatus = result == null ? "rejected" : String.valueOf(result.get("status"));
            String csMessage = result == null ? null : (String) result.get("message");

            if (body.get("controlId") != null) {
                derControlService.acknowledgeClear((String) body.get("controlId"),
                        "accepted".equalsIgnoreCase(csStatus) ? "Accepted" : "Rejected",
                        csMessage, loginUser.getUserId());
            }
            return "accepted".equalsIgnoreCase(csStatus)
                    ? new JsonResultSet(ResultStatus.SUCCESS)
                    : new JsonResultSet(ResultStatus.FAIL, csMessage);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    /** GetDERControlRequest 송신. body: { cpId, csId, isDefault, controlType, controlId, requestId } */
    @RequestMapping(value = "/get", method = RequestMethod.PUT)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public JsonResultSet pushGet(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            String cpId   = (String) body.get("cpId");
            String csId   = (String) body.get("csId");
            String cpCsId = cpId + "-" + csId;
            Map<String, Object> payload = new HashMap<>();
            if (body.get("requestId") != null)
                payload.put("requestId", toInt(body.get("requestId")));
            if (body.get("isDefault") != null)
                payload.put("isDefault", Boolean.valueOf(body.get("isDefault").toString()));
            if (body.get("controlType") != null) payload.put("controlType", body.get("controlType"));
            if (body.get("controlId") != null)   payload.put("controlId",   body.get("controlId"));

            LOGGER.info("[DER-GET] cpCsId={} payload={}", cpCsId, gson.toJson(payload));
            Map<String, Object> result = apiEaiClient.send2x(cpCsId, "GetDERControl", payload);
            String csStatus = result == null ? "rejected" : String.valueOf(result.get("status"));
            String csMessage = result == null ? null : (String) result.get("message");
            return "accepted".equalsIgnoreCase(csStatus)
                    ? new JsonResultSet(ResultStatus.SUCCESS)
                    : new JsonResultSet(ResultStatus.FAIL, csMessage);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new JsonResultSet(ResultStatus.FAIL, ex.getMessage());
        }
    }

    // ============================= HISTORY / ALARM / STARTSTOP =============================

    @RequestMapping(value = "/his", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<DerControlHisDto> searchHis(DerControlHisSearchCond cond, HttpServletRequest request) {
        try {
            return derControlService.retrieveHisBySearchCond(cond);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    @RequestMapping(value = "/alarm", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<DerAlarmDto> searchAlarm(DerAlarmSearchCond cond, HttpServletRequest request) {
        try {
            return derControlService.retrieveAlarmBySearchCond(cond);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    @RequestMapping(value = "/startstop", method = RequestMethod.GET)
    @Secured({ "ROLE_OPER", "ROLE_ADMIN" })
    public Page<DerStartStopDto> searchStartStop(DerStartStopSearchCond cond, HttpServletRequest request) {
        try {
            return derControlService.retrieveStartStopBySearchCond(cond);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    // ===== helpers =====

    /** SetDERControlRequest payload 빌드: { isDefault, controlId, controlType, <subKey>: paramJson } */
    @SuppressWarnings("unchecked")
    private Map<String, Object> buildSetPayload(DerControl c) {
        Map<String, Object> out = new HashMap<>();
        out.put("isDefault", DerControl.YES.equals(c.getIsDefault()));
        out.put("controlId", c.getControlId());
        out.put("controlType", c.getControlType());

        // paramJson 은 sub-object 1개 또는 다중 키를 포함할 수 있음
        // 형식 A: { "freqDroop": {...} } → 그대로 merge
        // 형식 B: { "priority":6, "overFreq":50.5, ... } → controlType 으로 key 추정해서 wrap
        if (c.getParamJson() != null && !c.getParamJson().isEmpty()) {
            Map<String, Object> parsed = gson.fromJson(c.getParamJson(), Map.class);
            if (parsed != null) {
                if (containsAnySubKey(parsed)) {
                    out.putAll(parsed);
                } else {
                    String key = deriveSubKey(c.getControlType());
                    out.put(key, parsed);
                }
            }
        }
        return out;
    }

    private boolean containsAnySubKey(Map<String, Object> map) {
        return map.containsKey("enterService") || map.containsKey("freqDroop")
                || map.containsKey("fixedPFAbsorb") || map.containsKey("fixedPFInject")
                || map.containsKey("fixedVar") || map.containsKey("gradient")
                || map.containsKey("limitMaxDischarge") || map.containsKey("curve");
    }

    private String deriveSubKey(String controlType) {
        switch (controlType) {
            case "EnterService":      return "enterService";
            case "FreqDroop":         return "freqDroop";
            case "FixedPFAbsorb":     return "fixedPFAbsorb";
            case "FixedPFInject":     return "fixedPFInject";
            case "FixedVar":          return "fixedVar";
            case "Gradients":         return "gradient";
            case "LimitMaxDischarge": return "limitMaxDischarge";
            default:                  return "curve";
        }
    }

    private Integer toInt(Object o) {
        if (o == null) return null;
        if (o instanceof Number) return ((Number) o).intValue();
        try { return Integer.parseInt(o.toString()); } catch (Exception e) { return null; }
    }
}
