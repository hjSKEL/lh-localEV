package kr.co.kevit.localcsms.eai.api.controller.ocpp16;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.charger.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.charger.process.ChargingProfileService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfileKind;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.type.ChargingProfilePurposeType;
import kr.co.kevit.localcsms.eai.api.dto.type.ChargingRateUnitType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Smart Charging 그룹 엔드포인트 (OCPP 1.6).
 *
 * POST /setChargingProfile
 * POST /setChargingProfiles
 * POST /clearChargingProfile
 * POST /getCompositeSchedule
 */
@RestController
@RequestMapping("/ocpp16")
public class SmartChargingController {

    private static final Logger log = LoggerFactory.getLogger(SmartChargingController.class);

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final Daemon16Client daemonClient;
    private final SequenceService sequenceService;
    private final ChargingProfileService chargingProfileService;

    public SmartChargingController(Daemon16Client daemonClient,
            SequenceService sequenceService,
            ChargingProfileService chargingProfileService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.chargingProfileService = chargingProfileService;
    }

    /**
     * Trigger a SetChargingProfile.req from the CSMS (OCPP 1.6).
     *
     * OCPP 1.6 payload 구조:
     * {
     * "connectorId": 1,
     * "chargingProfile": { ← 또는 "csChargingProfiles"
     * "chargingProfileKind": "Absolute",
     * "chargingProfilePurpose": "TxDefaultProfile",
     * "stackLevel": 0,
     * "chargingSchedule": { ... } ← OCPP 1.6: 단일 객체 (OCPP 2.0.1은 배열)
     * }
     * }
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/setChargingProfile")
    public ResponseEntity<ApiResult> setChargingProfile(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> payload) {
        int idx = chargingStationIdentity.lastIndexOf('-');
        if (idx < 0) {
            return ResponseEntity.ok(ApiResult.rejected("chargingStationIdentity 형식 오류"));
        }

        String cpId = chargingStationIdentity.substring(0, idx);
        String csId = chargingStationIdentity.substring(idx + 1);

        // chargingProfile 또는 csChargingProfiles 키 지원
        Map<String, Object> cp = (Map<String, Object>) payload.get("chargingProfile");
        if (cp == null) {
            cp = (Map<String, Object>) payload.get("csChargingProfiles");
        }
        if (cp == null) {
            return ResponseEntity.ok(ApiResult.rejected("chargingProfile 필드 누락"));
        }

        // chargingProfileId 처리 (0 또는 미입력 → 신규)
        int profileId = cp.containsKey("chargingProfileId") ? toInt(cp.get("chargingProfileId")) : 0;
        boolean isUpdate = profileId > 0;
        if (!isUpdate) {
            profileId = sequenceService.generateChargingProfileSeq();
        }
        cp.put("chargingProfileId", profileId);

        // connectorId (OCPP 1.6에서는 evseId 역할)
        int connectorId = payload.containsKey("connectorId") ? toInt(payload.get("connectorId")) : 0;

        // ChargingProfile 도메인 빌드 → DB 저장
        ChargingProfile profile = new ChargingProfile();
        profile.setProfileId(profileId);
        profile.setCpId(cpId);
        profile.setCsId(csId);
        profile.setEvseId(connectorId);
        profile.setStackLevel(cp.containsKey("stackLevel") ? toInt(cp.get("stackLevel")) : 0);
        profile.setValidFrom(parseDate((String) cp.get("validFrom")));
        profile.setValidTo(parseDate((String) cp.get("validTo")));

        // purpose
        String purposeStr = (String) cp.get("chargingProfilePurpose");
        if (purposeStr != null) {
            try {
                profile.setPurpose(ChargingProfilePurpose.valueOf(purposeStr));
            } catch (IllegalArgumentException e) {
                log.warn("[SetChargingProfile] unknown purpose: {}", purposeStr);
            }
        }

        // kind
        String kindStr = (String) cp.get("chargingProfileKind");
        if (kindStr != null) {
            try {
                profile.setKind(ChargingProfileKind.valueOf(kindStr));
            } catch (IllegalArgumentException e) {
                log.warn("[SetChargingProfile] unknown kind: {}", kindStr);
            }
        }

        // recurrencyKind
        String recurrency = (String) cp.get("recurrencyKind");
        if (recurrency != null) {
            profile.setRecurrencyKind("Daily".equals(recurrency) ? "D" : "W");
        }

        // chargingSchedule → JSON (OCPP 1.6: 단일 객체)
        Object scheduleObj = cp.get("chargingSchedule");
        if (scheduleObj != null) {
            try {
                profile.setScheduleListJson(objectMapper.writeValueAsString(scheduleObj));
            } catch (Exception e) {
                log.warn("[SetChargingProfile] schedule JSON 변환 실패: {}", e.getMessage());
            }
        }

        // DB 저장
        try {
            profile.setWriter(new Writer(StringConstants.SYSTEM_EMPLOYEE));
            if (isUpdate) {
                chargingProfileService.updateProfile(profile);
                log.info("[SetChargingProfile] DB 수정 완료: cpId={} csId={} profileId={}", cpId, csId, profileId);
            } else {
                chargingProfileService.saveProfile(profile);
                log.info("[SetChargingProfile] DB 저장 완료: cpId={} csId={} profileId={}", cpId, csId, profileId);
            }
        } catch (Exception e) {
            log.error("[SetChargingProfile] DB 저장 실패: cpId={} csId={} error={}", cpId, csId, e.getMessage());
        }

        // daemon 전송 (OCPP 1.6 포맷 그대로: connectorId + csChargingProfiles)
        Map<String, Object> daemonPayload = new LinkedHashMap<>();
        daemonPayload.put("connectorId", connectorId);
        daemonPayload.put("csChargingProfiles", cp);
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "SetChargingProfile", daemonPayload, null));
    }

    private java.util.Date parseDate(String isoStr) {
        if (isoStr == null || isoStr.isEmpty())
            return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(isoStr);
        } catch (Exception e) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(isoStr);
            } catch (Exception ex) {
                log.warn("[SetChargingProfile] 날짜 파싱 실패: {}", isoStr);
                return null;
            }
        }
    }

    private int toInt(Object val) {
        if (val instanceof Number)
            return ((Number) val).intValue();
        if (val instanceof String)
            return Integer.parseInt((String) val);
        return 0;
    }

    /**
     * Trigger multiple SetChargingProfile.req from the CSMS (OCPP 1.6).
     * Body: { "chargingProfiles": [ { "connectorId": 0, "chargingProfile": {...} },
     * ... ] }
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/setChargingProfiles")
    public ResponseEntity<ApiResult> setChargingProfiles(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> body) {
        List<Map<String, Object>> profiles = (List<Map<String, Object>>) body.get("chargingProfiles");
        if (profiles == null || profiles.isEmpty()) {
            return ResponseEntity.ok(ApiResult.rejected("chargingProfiles 없음"));
        }
        ApiResult last = null;
        for (Map<String, Object> item : profiles) {
            ResponseEntity<ApiResult> resp = setChargingProfile(chargingStationIdentity, item);
            last = resp.getBody();
            if (last != null && !"accepted".equals(last.getStatus())) {
                return ResponseEntity.ok(last);
            }
        }
        return ResponseEntity.ok(last != null ? last : ApiResult.accepted());
    }

    /** Trigger a ClearChargingProfile.req from the CSMS. */
    @PostMapping("/clearChargingProfile")
    public ResponseEntity<ApiResult> clearChargingProfile(
            @RequestParam String chargingStationIdentity,
            @RequestParam(required = false) Integer chargingProfileId,
            @RequestParam(required = false) Integer connectorId,
            @RequestParam(required = false) ChargingProfilePurposeType chargingProfilePurpose,
            @RequestParam(required = false) Integer stackLevel) {
        Map<String, Object> payload = new LinkedHashMap<>();
        if (chargingProfileId != null)
            payload.put("id", chargingProfileId);
        if (connectorId != null)
            payload.put("connectorId", connectorId);
        if (chargingProfilePurpose != null)
            payload.put("chargingProfilePurpose", chargingProfilePurpose.name());
        if (stackLevel != null)
            payload.put("stackLevel", stackLevel);
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ClearChargingProfile", payload, null));
    }

    /** Trigger a GetCompositeSchedule.req from the CSMS. */
    @PostMapping("/getCompositeSchedule")
    public ResponseEntity<ApiResult> getCompositeSchedule(
            @RequestParam String chargingStationIdentity,
            @RequestParam int connectorId,
            @RequestParam int duration,
            @RequestParam(required = false) ChargingRateUnitType chargingRateUnit) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("connectorId", connectorId);
        payload.put("duration", duration);
        if (chargingRateUnit != null) {
            payload.put("chargingRateUnit", chargingRateUnit.name());
        }
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetCompositeSchedule", payload, null));
    }
}
