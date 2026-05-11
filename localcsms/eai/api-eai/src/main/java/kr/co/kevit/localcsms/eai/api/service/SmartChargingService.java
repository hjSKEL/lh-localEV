package kr.co.kevit.localcsms.eai.api.service;

import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.model.ChargingProfile;
import kr.co.kevit.localcsms.eai.api.dto.type.ChargingProfilePurposeType;
import kr.co.kevit.localcsms.eai.api.dto.type.ChargingRateUnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Smart Charging 관련 OCPP 1.6 액션 서비스.
 *
 * setChargingProfile / setChargingProfiles / clearChargingProfile / getCompositeSchedule
 */
@Service
public class SmartChargingService {

    private static final Logger log = LoggerFactory.getLogger(SmartChargingService.class);

    private final Daemon16Client daemonClient;

    public SmartChargingService(Daemon16Client daemonClient) {
        this.daemonClient = daemonClient;
    }

    /**
     * SetChargingProfile.req 전송.
     * body: {"connectorId": 0, "csChargingProfiles": {...}} 또는 {"connectorId": 0, "chargingProfile": {...}}
     */
    public ApiResult setChargingProfile(String csId, Map<String, Object> body) {
        log.info("[API] setChargingProfile csId={}", csId);
        Map<String, Object> payload = new LinkedHashMap<>(body);
        // chargingProfile → csChargingProfiles (OCPP 1.6 필드명)
        Object profile = payload.remove("chargingProfile");
        if (profile != null) {
            payload.put("csChargingProfiles", profile);
        }
        return daemonClient.send(csId, "SetChargingProfile", payload, null);
    }

    /** 복수 SetChargingProfile.req 순차 전송 */
    public ApiResult setChargingProfiles(String csId, List<ChargingProfile> chargingProfiles) {
        log.info("[API] setChargingProfiles csId={} count={}", csId, chargingProfiles == null ? 0 : chargingProfiles.size());
        if (chargingProfiles == null || chargingProfiles.isEmpty()) {
            return ApiResult.rejected("chargingProfiles 없음");
        }
        ApiResult last = null;
        for (ChargingProfile profile : chargingProfiles) {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("connectorId", 0);
            payload.put("csChargingProfiles", profile);
            last = daemonClient.send(csId, "SetChargingProfile", payload, null);
            if (!"accepted".equals(last.getStatus())) {
                return last;
            }
        }
        return last != null ? last : ApiResult.accepted();
    }

    /** ClearChargingProfile.req 전송 */
    public ApiResult clearChargingProfile(String csId, Integer id, Integer connectorId,
                                          ChargingProfilePurposeType chargingProfilePurpose,
                                          Integer stackLevel) {
        log.info("[API] clearChargingProfile csId={} id={}", csId, id);
        Map<String, Object> payload = new LinkedHashMap<>();
        if (id != null) payload.put("id", id);
        if (connectorId != null) payload.put("connectorId", connectorId);
        if (chargingProfilePurpose != null) payload.put("chargingProfilePurpose", chargingProfilePurpose.name());
        if (stackLevel != null) payload.put("stackLevel", stackLevel);
        return daemonClient.send(csId, "ClearChargingProfile", payload, null);
    }

    /** GetCompositeSchedule.req 전송 */
    public ApiResult getCompositeSchedule(String csId, int connectorId, int duration,
                                          ChargingRateUnitType chargingRateUnit) {
        log.info("[API] getCompositeSchedule csId={} connectorId={} duration={}", csId, connectorId, duration);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("connectorId", connectorId);
        payload.put("duration", duration);
        if (chargingRateUnit != null) {
            payload.put("chargingRateUnit", chargingRateUnit.name());
        }
        return daemonClient.send(csId, "GetCompositeSchedule", payload, null);
    }
}
