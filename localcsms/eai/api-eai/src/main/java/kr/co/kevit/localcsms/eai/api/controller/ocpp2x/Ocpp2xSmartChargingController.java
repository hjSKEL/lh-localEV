package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.domain.ChargingSchedulePeriodType;
import kr.co.kevit.ocpp201.domain.ChargingScheduleType;
import kr.co.kevit.ocpp201.enumtype.RecurrencyKindEnumType;
import kr.co.kevit.ocpp201.request.SetChargingProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OCPP 2.0.1 Smart Charging 그룹 엔드포인트.
 *
 * POST     /ocpp2x/setChargingProfile?chargingStationIdentity=  (body: { evseId, chargingProfile })
 * GET/POST /ocpp2x/clearChargingProfile?chargingStationIdentity=&chargingProfileId=&evseId=&...
 * POST     /ocpp2x/getChargingProfiles?chargingStationIdentity=  (body: { evseId, chargingProfile })
 * GET/POST /ocpp2x/getCompositeSchedule?chargingStationIdentity=&duration=&evseId=&chargingRateUnit=
 */
@RestController
@RequestMapping("/ocpp2x")
public class Ocpp2xSmartChargingController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp2xSmartChargingController.class);

    private final AtomicInteger requestIdSeq = new AtomicInteger(1);

    /**
     * FAIL_ON_UNKNOWN_PROPERTIES=false: chargingProfileId 등 OCPP 1.6 비표준 필드를 자동 무시
     * NON_NULL(전역) + MixIn @JsonIgnore(개별): OCPP 2.1 전용 boolean 필드를 항상 제외
     *   - MixIn으로 stopAfterOffline/useLocalTime/evseSleep/preconditioningRequest 를 @JsonIgnore 처리
     *   - 값(true/false)에 무관하게 항상 직렬화 제외 → OCPP 2.0.1 스키마 위반 방지
     */
    private final ObjectMapper objectMapper = buildObjectMapper();

    private static ObjectMapper buildObjectMapper() {
        ObjectMapper om = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.addMixIn(ChargingProfileType.class, ChargingProfileMixIn.class);
        om.addMixIn(ChargingScheduleType.class, ChargingScheduleMixIn.class);
        om.addMixIn(ChargingSchedulePeriodType.class, ChargingSchedulePeriodMixIn.class);
        return om;
    }

    abstract static class ChargingProfileMixIn {
        @JsonIgnore abstract boolean isStopAfterOffline();
    }
    abstract static class ChargingScheduleMixIn {
        @JsonIgnore abstract boolean isUseLocalTime();
    }
    abstract static class ChargingSchedulePeriodMixIn {
        @JsonIgnore abstract boolean isEvseSleep();
        @JsonIgnore abstract boolean isPreconditioningRequest();
    }

    private final Daemon2xClient daemonClient;
    private final SequenceService sequenceService;
    private final ChargingProfileService chargingProfileService;

    public Ocpp2xSmartChargingController(Daemon2xClient daemonClient,
                                         SequenceService sequenceService,
                                         ChargingProfileService chargingProfileService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.chargingProfileService = chargingProfileService;
    }

    /**
     * body: { "evseId": 1, "chargingProfile": { ... } }
     * 1) payload → SetChargingProfile(typed) 역직렬화 (비표준 필드 자동 제거)
     * 2) chargingProfile.id == 0 이면 Sequence 채번
     * 3) chargingSchedule[].id 순번(1,2,...) 채번
     * 4) ChargingProfile DB 저장 (신규/수정)
     * 5) typed 객체로 ocpp20-daemon 호출 (OCPP 2.0.1 필드만 직렬화됨)
     */
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

        // 1) Map → SetChargingProfile typed 역직렬화
        //    FAIL_ON_UNKNOWN_PROPERTIES=false 이므로 chargingProfileId 등 비표준 필드는 무시됨
        SetChargingProfile req;
        try {
            req = objectMapper.convertValue(payload, SetChargingProfile.class);
        } catch (Exception e) {
            log.error("[SetChargingProfile] payload 변환 실패: {}", e.getMessage());
            return ResponseEntity.ok(ApiResult.rejected("payload 형식 오류: " + e.getMessage()));
        }

        ChargingProfileType cp = req.getChargingProfile();
        if (cp == null) {
            return ResponseEntity.ok(ApiResult.rejected("chargingProfile 필드 누락"));
        }

        // 2) chargingProfile.id 처리 (0 = 미입력 → 신규)
        boolean isUpdate = cp.getId() > 0;
        int profileId;
        if (isUpdate) {
            profileId = cp.getId();
        } else {
            profileId = sequenceService.generateChargingProfileSeq();
            cp.setId(profileId);
        }

        // 3) chargingSchedule[].id 순번 채번 (1, 2, ...)
        List<ChargingScheduleType> schedules = cp.getChargingSchedule();
        if (schedules != null) {
            int seq = 1;
            for (ChargingScheduleType s : schedules) {
                s.setId(seq++);
            }
        }

        // 4) ChargingProfile 도메인 빌드
        ChargingProfile profile = new ChargingProfile();
        profile.setProfileId(profileId);
        profile.setCpId(cpId);
        profile.setCsId(csId);
        profile.setEvseId(req.getEvseId());
        profile.setStackLevel(cp.getStackLevel());
        profile.setValidFrom(parseDate(cp.getValidFrom()));
        profile.setValidTo(parseDate(cp.getValidTo()));
        profile.setRechargingId(cp.getTransactionId());

        // purpose: ChargingProfilePurposeEnumType(이름) → ChargingProfilePurpose(코드)
        if (cp.getChargingProfilePurpose() != null) {
            try {
                profile.setPurpose(ChargingProfilePurpose.valueOf(cp.getChargingProfilePurpose().name()));
            } catch (IllegalArgumentException e) {
                log.warn("[SetChargingProfile] unknown purpose: {}", cp.getChargingProfilePurpose());
            }
        }

        // kind: ChargingProfileKindEnumType(이름) → ChargingProfileKind(코드)
        if (cp.getChargingProfileKind() != null) {
            try {
                profile.setKind(ChargingProfileKind.valueOf(cp.getChargingProfileKind().name()));
            } catch (IllegalArgumentException e) {
                log.warn("[SetChargingProfile] unknown kind: {}", cp.getChargingProfileKind());
            }
        }

        // recurrencyKind: Daily→D, Weekly→W
        if (cp.getRecurrencyKind() != null) {
            profile.setRecurrencyKind(RecurrencyKindEnumType.Daily == cp.getRecurrencyKind() ? "D" : "W");
        }

        // scheduleListJson: chargingSchedule 목록 → JSON (id 채번 후 직렬화)
        if (schedules != null) {
            try {
                profile.setScheduleListJson(objectMapper.writeValueAsString(schedules));
            } catch (Exception e) {
                log.warn("[SetChargingProfile] schedule JSON 변환 실패: {}", e.getMessage());
            }
        }

        // 5) DB 저장 (신규/수정)
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

        // 6) ocpp20-daemon 호출
        //    convertValue가 아닌 writeValueAsString→readValue 경로를 사용:
        //    JSON 문자열 직렬화 시 NON_DEFAULT 필터가 확실히 적용되어
        //    false 기본값 primitive 필드(stopAfterOffline, useLocalTime, evseSleep 등)가 제거됨
        try {
            String filteredJson = objectMapper.writeValueAsString(req);
            @SuppressWarnings("unchecked")
            Map<String, Object> daemonPayload = objectMapper.readValue(filteredJson, Map.class);
            return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "SetChargingProfile", daemonPayload, null));
        } catch (Exception e) {
            log.error("[SetChargingProfile] daemon payload 직렬화 실패: {}", e.getMessage());
            return ResponseEntity.ok(ApiResult.rejected("daemon payload 직렬화 실패: " + e.getMessage()));
        }
    }

    private java.util.Date parseDate(String isoStr) {
        if (isoStr == null || isoStr.isEmpty()) return null;
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

    /**
     * chargingProfileId (optional), evseId (optional), chargingProfilePurpose (optional), stackLevel (optional)
     */
    @RequestMapping(value = "/clearChargingProfile", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> clearChargingProfile(
            @RequestParam String chargingStationIdentity,
            @RequestParam(required = false) Integer chargingProfileId,
            @RequestParam(required = false) Integer evseId,
            @RequestParam(required = false) String chargingProfilePurpose,
            @RequestParam(required = false) Integer stackLevel) {

        Map<String, Object> payload = new HashMap<>();
        if (chargingProfileId != null) payload.put("chargingProfileId", chargingProfileId);
        if (evseId != null || chargingProfilePurpose != null || stackLevel != null) {
            Map<String, Object> criteria = new HashMap<>();
            if (evseId != null) criteria.put("evseId", evseId);
            if (chargingProfilePurpose != null) criteria.put("chargingProfilePurpose", chargingProfilePurpose);
            if (stackLevel != null) criteria.put("stackLevel", stackLevel);
            payload.put("chargingProfileCriteria", criteria);
        }

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "ClearChargingProfile", payload, null));
    }

    /**
     * body: { "evseId": ..., "chargingProfile": { "chargingProfilePurpose": ..., "stackLevel": ... } }
     * requestId 는 CSMS 가 자동 생성하여 payload 에 추가한다.
     */
    @PostMapping("/getChargingProfiles")
    public ResponseEntity<ApiResult> getChargingProfiles(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> body) {

        body.put("requestId", requestIdSeq.getAndIncrement());

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetChargingProfiles", body, null));
    }

    /**
     * duration (required), evseId (required), chargingRateUnit (optional)
     */
    @RequestMapping(value = "/getCompositeSchedule", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> getCompositeSchedule(
            @RequestParam String chargingStationIdentity,
            @RequestParam Integer duration,
            @RequestParam Integer evseId,
            @RequestParam String chargingRateUnit) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("duration", duration);
        payload.put("evseId", evseId);
        payload.put("chargingRateUnit", chargingRateUnit);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetCompositeSchedule", payload, null));
    }
}
