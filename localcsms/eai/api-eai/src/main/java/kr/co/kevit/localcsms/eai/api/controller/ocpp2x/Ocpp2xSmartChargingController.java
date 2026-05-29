package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.process.converter.ChargingProfileConverter;
import kr.co.kevit.localcsms.smartcharging.process.SmartChargingException;
import kr.co.kevit.localcsms.smartcharging.process.SmartChargingService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.domain.ChargingScheduleType;
import kr.co.kevit.ocpp201.request.SetChargingProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * NON_NULL: 2.1 전용 boolean(useLocalTime/evseSleep/preconditioningRequest) 은 도메인이 Boolean 으로
     *          선언되어 미지정 시 null → omit, true/false 명시 시 그대로 전송
     */
    private final ObjectMapper objectMapper = buildObjectMapper();

    private static ObjectMapper buildObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private final Daemon2xClient daemonClient;
    private final SequenceService sequenceService;
    private final SmartChargingService smartChargingService;
    private final ChargingProfileConverter chargingProfileConverter;

    public Ocpp2xSmartChargingController(Daemon2xClient daemonClient,
                                         SequenceService sequenceService,
                                         SmartChargingService smartChargingService,
                                         ChargingProfileConverter chargingProfileConverter) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.smartChargingService = smartChargingService;
        this.chargingProfileConverter = chargingProfileConverter;
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

        // 4) ChargingProfile 정규화 엔티티 트리 빌드 (converter)
        ChargingProfile profile = chargingProfileConverter.toEntity(cp, cpId, csId, req.getEvseId());
        profile.setWriter(new Writer(StringConstants.SYSTEM_EMPLOYEE));

        // 5) 엔진 검증 + 저장 (검증 실패 시 CS 로 보내지 않고 Rejected)
        try {
            smartChargingService.registerProfile(profile, isUpdate);
            log.info("[SetChargingProfile] 검증·저장 완료: cpId={} csId={} profileId={} update={}",
                    cpId, csId, profileId, isUpdate);
        } catch (SmartChargingException e) {
            log.warn("[SetChargingProfile] 프로파일 검증 실패: {}", e.getMessage());
            return ResponseEntity.ok(ApiResult.rejected("프로파일 검증 실패: " + e.getMessage()));
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

    /**
     * Dynamic 프로파일 수동 업데이트 push (K28).
     * body: { "chargingProfileId": 1001, "scheduleUpdate": { "limit": 7000, ... } }
     */
    @PostMapping("/updateDynamicSchedule")
    public ResponseEntity<ApiResult> updateDynamicSchedule(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> body) {

        if (body.get("chargingProfileId") == null) {
            return ResponseEntity.ok(ApiResult.rejected("chargingProfileId 누락"));
        }
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "UpdateDynamicSchedule", body, null));
    }
}
