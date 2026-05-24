/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation.
 * All rights reserved. This software is the proprietary information of
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.resource.cmd;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
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
import kr.co.kevit.localcsms.adminweb.share.ParamVo2;
import kr.co.kevit.localcsms.adminweb.share.ResultStatus;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusInfoHisService;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 7. 4.
 */
@RestController
@RequestMapping("ws/cmd/ocpp20")
public class Ocpp20CmdResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ocpp20CmdResource.class);

    @Autowired
    private ChargerStatusService chargerStatusService;

    @Autowired
    private ChargerStatusInfoHisService hisService;

    @Autowired
    private ApiEaiClient apiEaiClient;

    private final String REMOTESTART = "EVT017";

    private final String REMOTESTARTFAIL = "EVT018";

    private final String REMOTESTOP = "EVT019";

    private final String REMOTESTOPFAIL = "EVT020";

    private final String REMOTERESET = "EVT021";

    private final String REMOTERESETFAIL = "EVT022";

    private final String REMOTE = "EVT023";

    private final String REMOTEFAIL = "EVT024";

    @RequestMapping(value = "/{csId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER", "ROLE_MS", "ROLE_MM" })
    public JsonResultSet command(@PathVariable("csId") String csId, @RequestBody ParamVo2 paramVo,
            HttpServletRequest request) {
        //
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/cmd/ocpp20/{}, PUT, DATA : {}", loginUser.getUserId(),
                accessIp, csId, new Gson().toJson(paramVo));
        try {
            String[] csIds = csId.split(StringConstants.DASH);
            List<ChargerStatusInfo> chargerStatusList = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],
                    csIds[1]);

            JSONObject jObject = new JSONObject(paramVo.getParam2());
            ChargerStatusInfo chargerStatus = chargerStatusList.get(0);
            if (jObject.has("evseId")) {
                String evseIdStr = jObject.getString("evseId");
                if (evseIdStr != null && !evseIdStr.isEmpty()) {
                    int evseId = Integer.parseInt(evseIdStr);
                    for (ChargerStatusInfo chargerStatusInfo : chargerStatusList) {
                        if (chargerStatusInfo.getEvseId() == evseId) {
                            chargerStatus = chargerStatusInfo;
                        }
                    }
                }
            }
            chargerStatus.setInfoCollDate(new Date());
            chargerStatus.setUpdateDate(new Date());

            chargerStatus.setEvseId(0);
            chargerStatus.setEventCode(REMOTE);
            if ("RequestStartTransaction".equals(paramVo.getParam1())) {
                if (jObject.has("idTag")) {
                    String idTag = jObject.getString("idTag");
                    chargerStatus.setCutCardNo(idTag);
                }
                chargerStatus.setEvseId(1);
                chargerStatus.setEventCode(REMOTESTART);
            } else if ("RequestStopTransaction".equals(paramVo.getParam1())) {
                chargerStatus.setEvseId(1);
                chargerStatus.setEventCode(REMOTESTOP);
            } else if ("Reset".equals(paramVo.getParam1())) {
                chargerStatus.setEvseId(0);
                chargerStatus.setEventCode(REMOTERESET);
            }

            // api-eai 경유하여 ocpp20-daemon 호출 (payload를 Map으로 변환)
            @SuppressWarnings("unchecked")
            Map<String, Object> payloadMap = new Gson().fromJson(paramVo.getParam2(), Map.class);
            Map<String, Object> result = apiEaiClient.send2x(csId, paramVo.getParam1(), payloadMap);

            String status = (String) result.get("status");
            if ("accepted".equals(status)) {
                hisService.registerChargerStatusHis(chargerStatus);
                LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/cmd/ocpp20/{}, PUT, SUCCESS",
                        loginUser.getUserId(), accessIp, csId);
                return new JsonResultSet(ResultStatus.SUCCESS);
            } else {
                LOGGER.info("RESULT : {}", result.get("message"));
            }

            chargerStatus.setEvseId(0);
            chargerStatus.setEventCode(REMOTEFAIL);
            if ("RequestStartTransaction".equals(paramVo.getParam1())) {
                chargerStatus.setEvseId(1);
                chargerStatus.setEventCode(REMOTESTARTFAIL);
            } else if ("RequestStopTransaction".equals(paramVo.getParam1())) {
                chargerStatus.setEvseId(1);
                chargerStatus.setEventCode(REMOTESTOPFAIL);
            } else if ("Reset".equals(paramVo.getParam1())) {
                chargerStatus.setEvseId(0);
                chargerStatus.setEventCode(REMOTERESETFAIL);
            }
            hisService.registerChargerStatusHis(chargerStatus);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/cmd/ocpp20/{}, PUT, FAIL", loginUser.getUserId(),
                    accessIp, csId);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/cmd/ocpp20/{}, PUT, FAIL", loginUser.getUserId(),
                    accessIp, csId);
            LOGGER.error(ex.getMessage(), ex);
        }
        return new JsonResultSet(ResultStatus.FAIL);
    }

    /**
     * Bypass — api-eai 경유하여 ocpp20-daemon 으로 명령 전달.
     */
    @RequestMapping(value = "/bypass/{csId}", method = RequestMethod.PUT)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public JsonResultSet bypassCommand(@PathVariable("csId") String csId,
            @RequestBody ParamVo2 paramVo,
            HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/cmd/ocpp20/bypass/{}, PUT, DATA : {}",
                loginUser.getUserId(),
                accessIp, csId, new Gson().toJson(paramVo));
        try {
            String[] csIds = csId.split(StringConstants.DASH);
            List<ChargerStatusInfo> chargerStatusList = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0],
                    csIds[1]);

            JSONObject jObject = new JSONObject(paramVo.getParam2());
            ChargerStatusInfo chargerStatus = chargerStatusList.get(0);
            if (jObject.has("evseId")) {
                Integer evseId = jObject.getInt("evseId");
                if (evseId != null && evseId > 0) {
                    for (ChargerStatusInfo chargerStatusInfo : chargerStatusList) {
                        if (chargerStatusInfo.getEvseId() == evseId) {
                            chargerStatus = chargerStatusInfo;
                        }
                    }
                }
            }
            chargerStatus.setInfoCollDate(new Date());
            chargerStatus.setUpdateDate(new Date());

            chargerStatus.setEvseId(0);
            chargerStatus.setEventCode(REMOTE);
            if ("RequestStartTransaction".equals(paramVo.getParam1())) {
                if (jObject.has("idTag")) {
                    String idTag = jObject.getString("idTag");
                    chargerStatus.setCutCardNo(idTag);
                }
                chargerStatus.setEvseId(1);
                chargerStatus.setEventCode(REMOTESTART);
            } else if ("RequestStopTransaction".equals(paramVo.getParam1())) {
                chargerStatus.setEvseId(1);
                chargerStatus.setEventCode(REMOTESTOP);
            } else if ("Reset".equals(paramVo.getParam1())) {
                chargerStatus.setEvseId(0);
                chargerStatus.setEventCode(REMOTERESET);
            }

            // api-eai 경유하여 ocpp20-daemon 호출 (payload를 Map으로 변환)
            @SuppressWarnings("unchecked")
            Map<String, Object> payloadMap = new Gson().fromJson(paramVo.getParam2(), Map.class);
            Map<String, Object> result = apiEaiClient.send2x(csId, paramVo.getParam1(), payloadMap);

            String status = (String) result.get("status");
            if ("accepted".equals(status)) {
                hisService.registerChargerStatusHis(chargerStatus);
                LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/cmd/ocpp20/{}, PUT, SUCCESS",
                        loginUser.getUserId(), accessIp, csId);
                return new JsonResultSet(ResultStatus.SUCCESS);
            } else {
                LOGGER.info("RESULT : {}", result.get("message"));
            }

            chargerStatus.setEvseId(0);
            chargerStatus.setEventCode(REMOTEFAIL);
            if ("RequestStartTransaction".equals(paramVo.getParam1())) {
                chargerStatus.setEvseId(1);
                chargerStatus.setEventCode(REMOTESTARTFAIL);
            } else if ("RequestStopTransaction".equals(paramVo.getParam1())) {
                chargerStatus.setEvseId(1);
                chargerStatus.setEventCode(REMOTESTOPFAIL);
            } else if ("Reset".equals(paramVo.getParam1())) {
                chargerStatus.setEvseId(0);
                chargerStatus.setEventCode(REMOTERESETFAIL);
            }
            hisService.registerChargerStatusHis(chargerStatus);
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/cmd/ocpp20/{}, PUT, FAIL", loginUser.getUserId(),
                    accessIp, csId);
        } catch (Exception ex) {
            LOGGER.info("[RES] USER ID :{}, ACCESS_IP:{}, URL : ws/cmd/ocpp20/{}, PUT, FAIL", loginUser.getUserId(),
                    accessIp, csId);
            LOGGER.error(ex.getMessage(), ex);
        }
        return new JsonResultSet(ResultStatus.FAIL);
    }

    /**
     * 등록된 충전기 세션 목록 조회.
     */
    @RequestMapping(value = "/bypass/sessions", method = RequestMethod.GET)
    @Secured({ "ROLE_ADMIN", "ROLE_OPER" })
    public java.util.List<String> bypassSessions(HttpServletRequest request) {
        User loginUser = SessionManager.getLoginUser();
        String accessIp = getAccessIp(request);
        LOGGER.info("[REQ] USER ID :{}, ACCESS_IP:{}, URL : ws/cmd/ocpp20/bypass/sessions, GET",
                loginUser.getUserId(), accessIp);
        return apiEaiClient.getSessions();
    }
}
