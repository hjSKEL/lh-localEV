package kr.co.kevit.localcsms.smartcharging.process;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ExternalChargingLimit;
import kr.co.kevit.localcsms.smartcharging.process.ChargingProfileService;
import kr.co.kevit.localcsms.smartcharging.process.converter.ChargingProfileConverter;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;
import kr.co.kevit.ocpp201.domain.ChargingLimitType;
import kr.co.kevit.ocpp201.request.ClearedChargingLimit;
import kr.co.kevit.ocpp201.request.NotifyChargingLimit;

/**
 * 외부 충전 제약 관리 (OCPP 2.1 K15 — EMS/CSO/SO/grid).
 *
 * <p>NotifyChargingLimit → 활성 제약 저장(동일 source 교체), ClearedChargingLimit → 비활성화.
 * 활성 제약은 합성 스케줄 계산 시 ChargingStationExternalConstraints 레이어(최우선 상한)로 제공.</p>
 */
@Component
public class ExternalLimitManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalLimitManager.class);
    private static final String SYSTEM = "SYSTEM";

    private final ChargingProfileService chargingProfileService;
    private final ChargingProfileConverter converter;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public ExternalLimitManager(ChargingProfileService chargingProfileService,
                                ChargingProfileConverter converter) {
        this.chargingProfileService = chargingProfileService;
        this.converter = converter;
    }

    /** NotifyChargingLimit 수신 → 동일 source 기존 제약 비활성 후 신규 저장 */
    public void applyLimit(String cpId, String csId, NotifyChargingLimit req) {
        ChargingLimitType limit = req.getChargingLimit();
        String source = limit != null && limit.getChargingLimitSource() != null
                ? limit.getChargingLimitSource().name() : "Other";
        int evseId = req.getEvseId() != null ? req.getEvseId() : 0;

        chargingProfileService.deactivateExternalLimit(cpId, csId, evseId, source);

        ExternalChargingLimit e = new ExternalChargingLimit();
        e.setCpId(cpId);
        e.setCsId(csId);
        e.setEvseId(evseId);
        e.setLimitSource(source);
        if (limit != null) {
            e.setGridCritical(limit.getIsGridCritical());
            e.setLocalGeneration(limit.getIsLocalGeneration());
        }
        try {
            if (req.getChargingSchedule() != null) {
                e.setScheduleJson(objectMapper.writeValueAsString(req.getChargingSchedule()));
            }
        } catch (Exception ex) {
            LOGGER.warn("외부제약 schedule 직렬화 실패 cpId={} csId={}: {}", cpId, csId, ex.getMessage());
        }
        e.setWriter(new Writer(SYSTEM));
        chargingProfileService.saveExternalLimit(e);
        LOGGER.info("외부제약 적용 cpId={} csId={} evseId={} source={}", cpId, csId, evseId, source);
    }

    /** ClearedChargingLimit 수신 → 해당 source(+evse) 제약 비활성화 */
    public void clearLimit(String cpId, String csId, ClearedChargingLimit req) {
        String source = req.getChargingLimitSource() != null ? req.getChargingLimitSource().name() : null;
        Integer evseId = req.getEvseId();
        chargingProfileService.deactivateExternalLimit(cpId, csId, evseId, source);
        LOGGER.info("외부제약 해제 cpId={} csId={} evseId={} source={}", cpId, csId, evseId, source);
    }

    /** 활성 외부 제약을 합성 계산용 ChargingStationExternalConstraints 프로파일로 변환 */
    public List<ChargingProfile> activeLimitsAsProfiles(String cpId, String csId, int evseId) {
        List<ChargingProfile> result = new ArrayList<>();
        for (ExternalChargingLimit limit : chargingProfileService.findActiveExternalLimits(cpId, csId, evseId)) {
            ChargingProfile p = new ChargingProfile();
            p.setProfileId((int) limit.getSeq());
            p.setCpId(cpId);
            p.setCsId(csId);
            p.setEvseId(limit.getEvseId());
            p.setStackLevel(0);
            p.setPurpose(ChargingProfilePurpose.ChargingStationExternalConstraints);
            p.setSchedules(converter.schedulesFromOcppJson(limit.getScheduleJson()));
            result.add(p);
        }
        return result;
    }
}
