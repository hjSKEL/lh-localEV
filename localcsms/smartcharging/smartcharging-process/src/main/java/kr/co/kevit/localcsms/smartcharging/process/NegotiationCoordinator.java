package kr.co.kevit.localcsms.smartcharging.process;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingNeedsSnapshot;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.NegotiationState;
import kr.co.kevit.localcsms.smartcharging.process.ChargingProfileService;
import kr.co.kevit.localcsms.smartcharging.process.converter.ChargingProfileConverter;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;
import kr.co.kevit.ocpp201.domain.ChargingNeedsType;
import kr.co.kevit.ocpp201.enumtype.ControlModeEnumType;
import kr.co.kevit.ocpp201.enumtype.NotifyEVChargingNeedsStatusEnumType;
import kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds;

/**
 * ISO 15118-20 충전 협상 코디네이터 (K16/K17/K19/K20).
 *
 * <p>NotifyEVChargingNeeds 수신 시: needs 영속 → 협상 상태 전이 → 응답 status 결정 +
 * push 할 ChargingProfile 해석. NotifyEVChargingSchedule 수신 시: SCHEDULE_CONFIRMED 전이.</p>
 *
 * <p>프로필 결정 정책(P2-3): 해당 EVSE 에 등록된 TxProfile(우선)/TxDefaultProfile 을 push.
 * needs 기반 신규 프로필 합성(최적화)은 후속 단계.</p>
 */
@Component
public class NegotiationCoordinator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NegotiationCoordinator.class);
    private static final String SYSTEM = "SYSTEM";

    private final ChargingProfileService chargingProfileService;
    private final SmartChargingService smartChargingService;
    private final ChargingProfileConverter converter;
    private final ProfileBuilder profileBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NegotiationCoordinator(ChargingProfileService chargingProfileService,
                                  SmartChargingService smartChargingService,
                                  ChargingProfileConverter converter,
                                  ProfileBuilder profileBuilder) {
        this.chargingProfileService = chargingProfileService;
        this.smartChargingService = smartChargingService;
        this.converter = converter;
        this.profileBuilder = profileBuilder;
    }

    /**
     * NotifyEVChargingNeeds 처리.
     *
     * @param hasActiveCharging 해당 EVSE 에 진행 중 충전 존재 여부
     * @param noProfilePolicy   DynamicControl 시 NoChargingProfile 응답 정책 (OCPP03)
     */
    public NegotiationResult handleNeeds(String cpId, String csId, String rechargingId,
                                         NotifyEVChargingNeeds req,
                                         boolean hasActiveCharging, boolean noProfilePolicy) {
        int evseId = req.getEvseId() != null ? req.getEvseId() : 0;

        if (!hasActiveCharging) {
            return new NegotiationResult(NotifyEVChargingNeedsStatusEnumType.Rejected, null);
        }

        ChargingNeedsType needs = req.getChargingNeeds();
        boolean dynamic = needs != null && ControlModeEnumType.DynamicControl.equals(needs.getControlMode());

        // 1) needs 스냅샷 영속
        persistNeeds(cpId, csId, evseId, rechargingId, req, needs);

        // 2) 협상 상태: NEEDS_RECEIVED
        upsertState(cpId, csId, evseId, rechargingId, NegotiationState.NEEDS_RECEIVED,
                needs != null && needs.getControlMode() != null ? needs.getControlMode().name() : null, null);

        // 3) DynamicControl + no-profile 정책 → NoChargingProfile (push 없음)
        if (dynamic && noProfilePolicy) {
            return new NegotiationResult(NotifyEVChargingNeedsStatusEnumType.NoChargingProfile, null);
        }

        // 4) push 할 프로필 해석 (TxProfile 우선). 없으면 needs 기반 합성(V2X/DER 반영).
        ChargingProfile toPush = resolveProfileToPush(cpId, csId, evseId);
        NotifyEVChargingNeedsStatusEnumType status = isV2xBidirectional(needs)
                ? NotifyEVChargingNeedsStatusEnumType.Processing
                : NotifyEVChargingNeedsStatusEnumType.Accepted;

        if (toPush == null) {
            try {
                ChargingProfile synthesized = profileBuilder.synthesize(cpId, csId, evseId, rechargingId, req);
                smartChargingService.registerProfile(synthesized, false);
                toPush = synthesized;
                LOGGER.info("협상: 활성 프로필 없음 → 합성 프로필 push profileId={}", synthesized.getProfileId());
            } catch (Exception e) {
                LOGGER.warn("프로필 합성/저장 실패 cpId={} csId={}: {}", cpId, csId, e.getMessage());
            }
        }

        if (toPush == null) {
            LOGGER.info("협상: push 할 프로필 없음(합성 실패) cpId={} csId={} evseId={} → status={}",
                    cpId, csId, evseId, status);
            return new NegotiationResult(status, null);
        }

        // 5) 상태: PROFILE_SENT
        upsertState(cpId, csId, evseId, rechargingId, NegotiationState.PROFILE_SENT,
                needs != null && needs.getControlMode() != null ? needs.getControlMode().name() : null,
                toPush.getProfileId());
        return new NegotiationResult(status, converter.toOcpp(toPush));
    }

    /** NotifyEVChargingSchedule 수신 → SCHEDULE_CONFIRMED 전이 */
    public void confirmSchedule(String cpId, String csId, int evseId) {
        NegotiationState existing = chargingProfileService.findNegotiation(cpId, csId, evseId);
        if (existing == null) {
            return;
        }
        upsertState(cpId, csId, evseId, existing.getRechargingId(), NegotiationState.SCHEDULE_CONFIRMED,
                existing.getControlMode(), existing.getLastProfileId());
    }

    /** TxProfile(우선) → TxDefaultProfile 순으로 push 대상 선택 */
    private ChargingProfile resolveProfileToPush(String cpId, String csId, int evseId) {
        List<ChargingProfile> active = smartChargingService.resolveActiveProfiles(cpId, csId, evseId);
        ChargingProfile tx = highestStack(active, ChargingProfilePurpose.TxProfile);
        if (tx != null) return tx;
        return highestStack(active, ChargingProfilePurpose.TxDefaultProfile);
    }

    private ChargingProfile highestStack(List<ChargingProfile> list, ChargingProfilePurpose purpose) {
        ChargingProfile best = null;
        for (ChargingProfile p : list) {
            if (purpose != p.getPurpose()) continue;
            if (best == null || p.getStackLevel() > best.getStackLevel()) best = p;
        }
        return best;
    }

    private boolean isV2xBidirectional(ChargingNeedsType needs) {
        return needs != null && needs.getRequestedEnergyTransfer() != null
                && needs.getRequestedEnergyTransfer().name().contains("BPT");
    }

    private void persistNeeds(String cpId, String csId, int evseId, String rechargingId,
                              NotifyEVChargingNeeds req, ChargingNeedsType needs) {
        try {
            ChargingNeedsSnapshot snap = new ChargingNeedsSnapshot();
            snap.setCpId(cpId);
            snap.setCsId(csId);
            snap.setEvseId(evseId);
            snap.setRechargingId(rechargingId);
            if (needs != null) {
                snap.setRequestedEnergyTransfer(needs.getRequestedEnergyTransfer() != null
                        ? needs.getRequestedEnergyTransfer().name() : null);
                snap.setControlMode(needs.getControlMode() != null ? needs.getControlMode().name() : null);
                snap.setNeedsJson(objectMapper.writeValueAsString(needs));
            } else {
                snap.setNeedsJson("{}");
            }
            snap.setMaxScheduleTuples(req.getMaxScheduleTuples());
            snap.setReceivedAt(new Date());
            snap.setWriter(new Writer(SYSTEM));
            chargingProfileService.saveNeeds(snap);
        } catch (Exception e) {
            LOGGER.warn("ChargingNeeds 영속 실패 cpId={} csId={} evseId={}: {}", cpId, csId, evseId, e.getMessage());
        }
    }

    private void upsertState(String cpId, String csId, int evseId, String rechargingId,
                             String state, String controlMode, Integer lastProfileId) {
        try {
            NegotiationState ns = new NegotiationState();
            ns.setCpId(cpId);
            ns.setCsId(csId);
            ns.setEvseId(evseId);
            ns.setRechargingId(rechargingId);
            ns.setState(state);
            ns.setControlMode(controlMode);
            ns.setLastProfileId(lastProfileId);
            ns.setWriter(new Writer(SYSTEM));
            chargingProfileService.saveNegotiation(ns);
        } catch (Exception e) {
            LOGGER.warn("협상 상태 저장 실패 cpId={} csId={} evseId={} state={}: {}",
                    cpId, csId, evseId, state, e.getMessage());
        }
    }
}
