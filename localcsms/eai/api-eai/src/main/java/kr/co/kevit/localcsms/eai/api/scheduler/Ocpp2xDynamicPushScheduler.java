package kr.co.kevit.localcsms.eai.api.scheduler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.process.ChargingProfileService;
import kr.co.kevit.localcsms.smartcharging.process.DynamicScheduleManager;
import kr.co.kevit.localcsms.system.process.DaemonAccessService;
import kr.co.kevit.ocpp201.domain.ChargingScheduleUpdateType;
import kr.co.kevit.ocpp201.request.UpdateDynamicSchedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * OCPP 2.1 K28 (Dynamic CSMS setpoint - push) 자동 트리거.
 *
 * <p>주기적으로 {@code smartcharging.dynamic.auto-push.interval-ms} 마다
 * {@link ChargingProfileService#findAutoPushTargets()} 로 4중 필터(Dynamic kind +
 * Central operationMode + 도래시각 경과 + EVSE 단위 최상위 stack) 통과 대상을 조회하고,
 * 각 대상에 대해 UpdateDynamicSchedule CALL 을 daemon REST 로 위임 전송.</p>
 *
 * <p>운영 시: {@code smartcharging.dynamic.auto-push.enabled=false} 로 외부 EMS 트리거에 위임.<br>
 * 테스트(OCTT Q_109/Q_117): {@code enabled=true} 로 활성화.</p>
 *
 * <p>External / Local / Idle operationMode 는 {@code findAutoPushTargets()} 의
 * SQL 필터에서 제외되므로 Q_111(External), Q_120(AFRR), Q_121(LocalFrequency) 등과
 * 충돌하지 않는다.</p>
 */
@Component
@ConditionalOnProperty(name = "smartcharging.dynamic.auto-push.enabled", havingValue = "true")
public class Ocpp2xDynamicPushScheduler {

    private static final Logger log = LoggerFactory.getLogger(Ocpp2xDynamicPushScheduler.class);

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final ChargingProfileService chargingProfileService;
    private final DynamicScheduleManager dynamicScheduleManager;
    private final DaemonAccessService daemonAccessService;
    private final Daemon2xClient daemonClient;

    public Ocpp2xDynamicPushScheduler(ChargingProfileService chargingProfileService,
                                      DynamicScheduleManager dynamicScheduleManager,
                                      DaemonAccessService daemonAccessService,
                                      Daemon2xClient daemonClient) {
        this.chargingProfileService = chargingProfileService;
        this.dynamicScheduleManager = dynamicScheduleManager;
        this.daemonAccessService = daemonAccessService;
        this.daemonClient = daemonClient;
        log.info("[OCPP2X DYN PUSH] Ocpp2xDynamicPushScheduler 활성 — Dynamic + Central operationMode 자동 push 모드");
    }

    @Scheduled(fixedDelayString = "${smartcharging.dynamic.auto-push.interval-ms:5000}",
               initialDelayString = "${smartcharging.dynamic.auto-push.interval-ms:5000}")
    public void tick() {
        List<ChargingProfile> targets;
        try {
            targets = chargingProfileService.findAutoPushTargets();
        } catch (Exception e) {
            log.warn("[OCPP2X DYN PUSH] 대상 조회 실패: {}", e.getMessage());
            return;
        }
        if (targets == null || targets.isEmpty()) {
            return;
        }
        for (ChargingProfile profile : targets) {
            try {
                pushOne(profile);
            } catch (Exception e) {
                log.warn("[OCPP2X DYN PUSH] push 실패 cpId={} csId={} profileId={}: {}",
                        profile.getCpId(), profile.getCsId(), profile.getProfileId(), e.getMessage());
            }
        }
    }

    private void pushOne(ChargingProfile profile) throws Exception {
        String cpCsId = profile.getCpId() + "-" + profile.getCsId();

        // CS 연결 미확인 시 다음 주기 재시도
        if (daemonAccessService.retrieveDaemonAccessByCpCsId(cpCsId) == null) {
            return;
        }

        // 현재 setpoint/limit 결정 — DynamicScheduleManager 재활용
        ChargingScheduleUpdateType update = dynamicScheduleManager.computeUpdate(
                profile.getProfileId(), profile.getCpId(), profile.getCsId());
        if (update == null) {
            return;
        }

        UpdateDynamicSchedule req = new UpdateDynamicSchedule();
        req.setChargingProfileId(profile.getProfileId());
        req.setScheduleUpdate(update);

        Map<String, Object> payload = toCleanMap(req);
        daemonClient.send(cpCsId, "UpdateDynamicSchedule", payload, null);

        // 다음 주기까지 대기 — DYN_UPDATE_TIME 갱신
        chargingProfileService.touchDynUpdateTime(profile.getProfileId());

        log.info("[OCPP2X DYN PUSH] UpdateDynamicSchedule push cpCsId={} profileId={}",
                cpCsId, profile.getProfileId());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> toCleanMap(Object typedReq) throws Exception {
        String json = objectMapper.writeValueAsString(typedReq);
        return objectMapper.readValue(json, Map.class);
    }
}
