package kr.co.kevit.localcsms.eai.api.service;

import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.model.AuthorizationData;
import kr.co.kevit.localcsms.eai.api.dto.type.UpdateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * LocalList 관련 OCPP 1.6 액션 서비스.
 *
 * getLocalListVersion / sendLocalList
 */
@Service
public class LocalListService {

    private static final Logger log = LoggerFactory.getLogger(LocalListService.class);

    private final Daemon16Client daemonClient;

    public LocalListService(Daemon16Client daemonClient) {
        this.daemonClient = daemonClient;
    }

    /** GetLocalListVersion.req 전송 */
    public ApiResult getLocalListVersion(String csId) {
        log.info("[API] getLocalListVersion csId={}", csId);
        return daemonClient.send(csId, "GetLocalListVersion", null, null);
    }

    /** SendLocalList.req 전송 */
    public ApiResult sendLocalList(String csId, int listVersion,
                                   List<AuthorizationData> localAuthorizationList,
                                   UpdateType updateType) {
        log.info("[API] sendLocalList csId={} listVersion={} updateType={}", csId, listVersion, updateType);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("listVersion", listVersion);
        payload.put("updateType", updateType.name());
        if (localAuthorizationList != null) {
            payload.put("localAuthorizationList", localAuthorizationList);
        }
        return daemonClient.send(csId, "SendLocalList", payload, null);
    }
}
