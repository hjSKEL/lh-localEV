package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

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
import kr.co.kevit.ocpp201.domain.ChargingScheduleType;
import kr.co.kevit.ocpp201.enumtype.RecurrencyKindEnumType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OCPP 2.0.1 Transaction 그룹 엔드포인트.
 *
 * GET/POST /ocpp2x/requestStartTransaction?chargingStationIdentity=&remoteStartId=&idToken=&idTokenType=&evseId=
 * GET/POST /ocpp2x/requestStopTransaction?chargingStationIdentity=&transactionId=
 * GET/POST /ocpp2x/getTransactionStatus?chargingStationIdentity=&transactionId=
 */
@RestController
@RequestMapping("/ocpp2x")
public class Ocpp2xTransactionController {

    private static final Logger log = LoggerFactory.getLogger(Ocpp2xTransactionController.class);

    /**
     * FAIL_ON_UNKNOWN_PROPERTIES=false: 비표준 필드 자동 무시
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
    private final ChargingProfileService chargingProfileService;

    public Ocpp2xTransactionController(Daemon2xClient daemonClient,
                                       SequenceService sequenceService,
                                       ChargingProfileService chargingProfileService) {
        this.daemonClient = daemonClient;
        this.sequenceService = sequenceService;
        this.chargingProfileService = chargingProfileService;
    }

    /**
     * body: { "idToken": { "idToken": "...", "type": "..." }, "evseId": 1, "chargingProfile": { ... }, "remoteStartId": 6 }
     *
     * chargingProfile 처리:
     * 1) Map → ChargingProfileType 역직렬화 (OCPP 2.1 전용 필드 자동 제거)
     * 2) chargingProfile.id == 0 이면 Sequence 채번
     * 3) chargingSchedule[].id 순번(1,2,...) 채번
     * 4) ChargingProfile DB 저장 (신규/수정)
     * 5) payload 내 chargingProfile 을 필터링된 버전으로 교체
     */
    @RequestMapping(value = "/requestStartTransaction", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> requestStartTransaction(
            @RequestParam String chargingStationIdentity,
            @RequestBody(required = false) Map<String, Object> payload) {

        if (payload == null) payload = new HashMap<>();
        if (!payload.containsKey("remoteStartId")) {
            payload.put("remoteStartId", sequenceService.generateRemoteStartSeq());
        }

        if (payload.get("chargingProfile") != null) {
            // cpId / csId 파싱
            String cpId = null, csId = null;
            int idx = chargingStationIdentity.lastIndexOf('-');
            if (idx >= 0) {
                cpId = chargingStationIdentity.substring(0, idx);
                csId = chargingStationIdentity.substring(idx + 1);
            }

            // 1) Map → ChargingProfileType 역직렬화
            ChargingProfileType cp;
            try {
                cp = objectMapper.convertValue(payload.get("chargingProfile"), ChargingProfileType.class);
            } catch (Exception e) {
                log.error("[RequestStartTransaction] chargingProfile 변환 실패: {}", e.getMessage());
                return ResponseEntity.ok(ApiResult.rejected("chargingProfile 형식 오류: " + e.getMessage()));
            }

            // 2) id 채번 (0 = 미입력 → 신규)
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

            // 4) DB 저장
            if (cpId != null) {
                ChargingProfile profile = new ChargingProfile();
                profile.setProfileId(profileId);
                profile.setCpId(cpId);
                profile.setCsId(csId);
                Object evseIdObj = payload.get("evseId");
                if (evseIdObj instanceof Integer) {
                    profile.setEvseId((Integer) evseIdObj);
                }
                profile.setStackLevel(cp.getStackLevel());
                profile.setValidFrom(parseDate(cp.getValidFrom()));
                profile.setValidTo(parseDate(cp.getValidTo()));
                profile.setRechargingId(cp.getTransactionId());

                if (cp.getChargingProfilePurpose() != null) {
                    try {
                        profile.setPurpose(ChargingProfilePurpose.valueOf(cp.getChargingProfilePurpose().name()));
                    } catch (IllegalArgumentException e) {
                        log.warn("[RequestStartTransaction] unknown purpose: {}", cp.getChargingProfilePurpose());
                    }
                }
                if (cp.getChargingProfileKind() != null) {
                    try {
                        profile.setKind(ChargingProfileKind.valueOf(cp.getChargingProfileKind().name()));
                    } catch (IllegalArgumentException e) {
                        log.warn("[RequestStartTransaction] unknown kind: {}", cp.getChargingProfileKind());
                    }
                }
                if (cp.getRecurrencyKind() != null) {
                    profile.setRecurrencyKind(RecurrencyKindEnumType.Daily == cp.getRecurrencyKind() ? "D" : "W");
                }
                if (schedules != null) {
                    try {
                        profile.setScheduleListJson(objectMapper.writeValueAsString(schedules));
                    } catch (Exception e) {
                        log.warn("[RequestStartTransaction] schedule JSON 변환 실패: {}", e.getMessage());
                    }
                }

                try {
                    profile.setWriter(new Writer(StringConstants.SYSTEM_EMPLOYEE));
                    if (isUpdate) {
                        chargingProfileService.updateProfile(profile);
                        log.info("[RequestStartTransaction] chargingProfile DB 수정: cpId={} csId={} profileId={}", cpId, csId, profileId);
                    } else {
                        chargingProfileService.saveProfile(profile);
                        log.info("[RequestStartTransaction] chargingProfile DB 저장: cpId={} csId={} profileId={}", cpId, csId, profileId);
                    }
                } catch (Exception e) {
                    log.error("[RequestStartTransaction] chargingProfile DB 저장 실패: cpId={} csId={} error={}", cpId, csId, e.getMessage());
                }
            }

            // 5) payload 내 chargingProfile 을 필터링된 버전으로 교체 (OCPP 2.1 필드 제거)
            try {
                String filteredJson = objectMapper.writeValueAsString(cp);
                @SuppressWarnings("unchecked")
                Map<String, Object> filteredCp = objectMapper.readValue(filteredJson, Map.class);
                payload.put("chargingProfile", filteredCp);
            } catch (Exception e) {
                log.error("[RequestStartTransaction] chargingProfile 직렬화 실패: {}", e.getMessage());
            }
        }

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "RequestStartTransaction", payload, null));
    }

    /** transactionId (required) */
    @RequestMapping(value = "/requestStopTransaction", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> requestStopTransaction(
            @RequestParam String chargingStationIdentity,
            @RequestParam String transactionId) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("transactionId", transactionId);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "RequestStopTransaction", payload, null));
    }

    /** transactionId (optional) */
    @RequestMapping(value = "/getTransactionStatus", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ApiResult> getTransactionStatus(
            @RequestParam String chargingStationIdentity,
            @RequestParam String transactionId) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("transactionId", transactionId);

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "GetTransactionStatus", payload, null));
    }

    private java.util.Date parseDate(String isoStr) {
        if (isoStr == null || isoStr.isEmpty()) return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(isoStr);
        } catch (Exception e) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(isoStr);
            } catch (Exception ex) {
                log.warn("[RequestStartTransaction] 날짜 파싱 실패: {}", isoStr);
                return null;
            }
        }
    }
}
