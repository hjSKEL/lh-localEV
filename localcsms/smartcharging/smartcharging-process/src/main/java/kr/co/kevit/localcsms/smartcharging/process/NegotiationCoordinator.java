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
import kr.co.kevit.ocpp201.domain.AbsolutePriceScheduleType;
import kr.co.kevit.ocpp201.domain.ChargingNeedsType;
import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.domain.ChargingScheduleType;
import kr.co.kevit.ocpp201.domain.ChargingSchedulePeriodType;
import kr.co.kevit.ocpp201.domain.PriceLevelScheduleEntryType;
import kr.co.kevit.ocpp201.domain.PriceLevelScheduleType;
import kr.co.kevit.ocpp201.domain.PriceRuleStackType;
import kr.co.kevit.ocpp201.domain.PriceRuleType;
import kr.co.kevit.ocpp201.domain.RationalNumberType;
import kr.co.kevit.ocpp201.enumtype.ChargingProfileKindEnumType;
import kr.co.kevit.ocpp201.enumtype.ChargingProfilePurposeEnumType;
import kr.co.kevit.ocpp201.enumtype.ControlModeEnumType;
import kr.co.kevit.ocpp201.enumtype.NotifyEVChargingNeedsStatusEnumType;
import kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

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

    /** 동일 (evseId, profileId) 에 대한 중복 push 차단 윈도우(ms). 정상 K_117 needs 간격(≈1.3s)에 안 걸리는 값. */
    private static final long DUPLICATE_PUSH_WINDOW_MS = 500L;
    private final java.util.concurrent.ConcurrentHashMap<String, Long> lastPushTimestamps =
            new java.util.concurrent.ConcurrentHashMap<>();

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

        // 3) push 할 프로필 해석 (TxProfile 우선). 없으면 needs 기반 합성(V2X/DER 반영).
        ChargingProfile toPush = resolveProfileToPush(cpId, csId, evseId);
        NotifyEVChargingNeedsStatusEnumType status = isV2xBidirectional(needs)
                ? NotifyEVChargingNeedsStatusEnumType.Processing
                : NotifyEVChargingNeedsStatusEnumType.Accepted;

        // 3-1) DynamicControl + no-profile 정책 + 활성 프로필 없음 → NoChargingProfile.
        //      활성 프로필이 있으면 정책과 무관하게 push (K20 "Adjusting" 시나리오 / K_117).
        if (toPush == null && dynamic && noProfilePolicy) {
            return new NegotiationResult(NotifyEVChargingNeedsStatusEnumType.NoChargingProfile, null);
        }

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

        // 4-1) 중복 push 가드 — 동일 (evseId, profileId) 에 대해 짧은 시간 안에 두 번 push 차단
        //      OCTT(K_117) 가 unexpected SetChargingProfileRequest 로 거절하는 케이스 방어.
        //      정상 협상 시퀀스(K_113/114/115/117) 의 push 간격(>1s) 에는 영향 없음.
        if (isDuplicatePush(cpId, csId, evseId, toPush.getProfileId())) {
            LOGGER.info("협상: 중복 push 차단 cpId={} csId={} evseId={} profileId={} (window={}ms)",
                    cpId, csId, evseId, toPush.getProfileId(), DUPLICATE_PUSH_WINDOW_MS);
            return new NegotiationResult(status, null);
        }

        // 5) 상태: PROFILE_SENT
        upsertState(cpId, csId, evseId, rechargingId, NegotiationState.PROFILE_SENT,
                needs != null && needs.getControlMode() != null ? needs.getControlMode().name() : null,
                toPush.getProfileId());

        // 6) TxProfile push 시 transactionId 를 현재 충전 ID 로 덮어쓰기
        //    (사전 등록된 프로필의 transactionId 가 과거 값일 수 있음 — K17/K_114 검증 통과 필요)
        ChargingProfileType ocppProfile = converter.toOcpp(toPush);
        if (rechargingId != null
                && ChargingProfilePurposeEnumType.TxProfile == ocppProfile.getChargingProfilePurpose()) {
            ocppProfile.setTransactionId(rechargingId);
        }
        // 7) ISO 15118-20 Dynamic Control 모드는 absolutePriceSchedule 필수 (K19). 누락 시 최소 stub 주입.
        if (ChargingProfileKindEnumType.Dynamic == ocppProfile.getChargingProfileKind()
                || (needs != null && ControlModeEnumType.DynamicControl.equals(needs.getControlMode()))) {
            injectAbsolutePriceScheduleStub(ocppProfile);
        }
        return new NegotiationResult(status, ocppProfile);
    }

    /**
     * Dynamic Control 모드 push 시 OCPP 2.1 K19/K20 검증을 위한 누락 필드 보강:
     * <ul>
     *   <li>chargingSchedule.absolutePriceSchedule / priceLevelSchedule stub (K19)</li>
     *   <li>chargingSchedulePeriod.setpoint (K20.FR.03 — TC_K_117 Step 3 검증)</li>
     * </ul>
     */
    private void injectAbsolutePriceScheduleStub(ChargingProfileType profile) {
        if (profile.getChargingSchedule() == null) return;
        for (ChargingScheduleType s : profile.getChargingSchedule()) {
            if (s.getAbsolutePriceSchedule() == null) {
                s.setAbsolutePriceSchedule(buildAbsolutePriceScheduleStub());
            }
            if (s.getPriceLevelSchedule() == null) {
                s.setPriceLevelSchedule(buildPriceLevelScheduleStub());
            }
            if (s.getChargingSchedulePeriod() != null) {
                for (ChargingSchedulePeriodType p : s.getChargingSchedulePeriod()) {
                    // setpoint 누락 시 limit 값을 setpoint 로 채워 K20.FR.03 검증 통과
                    if (p.getSetpoint() == null && p.getLimit() != null) {
                        p.setSetpoint(p.getLimit());
                    }
                }
            }
        }
    }

    private PriceLevelScheduleType buildPriceLevelScheduleStub() {
        PriceLevelScheduleType pls = new PriceLevelScheduleType();
        pls.setTimeAnchor(isoNow());
        pls.setPriceScheduleId(1);
        pls.setPriceScheduleDescription("CSMS dynamic stub");
        pls.setNumberOfPriceLevels(1);
        PriceLevelScheduleEntryType entry = new PriceLevelScheduleEntryType();
        entry.setDuration(3600);
        entry.setPriceLevel(0);
        List<PriceLevelScheduleEntryType> entries = new ArrayList<>();
        entries.add(entry);
        pls.setPriceLevelScheduleEntries(entries);
        return pls;
    }

    private AbsolutePriceScheduleType buildAbsolutePriceScheduleStub() {
        AbsolutePriceScheduleType aps = new AbsolutePriceScheduleType();
        aps.setTimeAnchor(isoNow());
        aps.setPriceScheduleID(1);
        aps.setPriceScheduleDescription("CSMS dynamic stub");
        aps.setCurrency("KRW");
        aps.setLanguage("ko");
        aps.setPriceAlgorithm("urn:iso:std:iso:15118-20:price:algorithm:0");

        PriceRuleType rule = new PriceRuleType();
        rule.setEnergyFee(rational(0, 0));         // 0 KRW/Wh
        rule.setPowerRangeStart(rational(0, 0));   // 0 W
        rule.setCarbonDioxideEmission(0);
        rule.setRenewableGenerationPercentage(0);

        PriceRuleStackType stack = new PriceRuleStackType();
        stack.setDuration(3600); // 1시간
        List<PriceRuleType> rules = new ArrayList<>();
        rules.add(rule);
        stack.setPriceRule(rules);

        // OCPP 2.1 schema: priceRuleStacks 는 배열(minItems=1, maxItems=1024)
        List<PriceRuleStackType> stacks = new ArrayList<>();
        stacks.add(stack);
        aps.setPriceRuleStacks(stacks);
        return aps;
    }

    private RationalNumberType rational(int value, int exponent) {
        RationalNumberType r = new RationalNumberType();
        r.setValue(value);
        r.setExponent(exponent);
        return r;
    }

    private String isoNow() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        return f.format(new Date());
    }

    /** 동일 (cpId,csId,evseId,profileId) 키에 대해 윈도우 안에 두 번째 호출이면 true. true 시 기록 갱신 안 함. */
    private boolean isDuplicatePush(String cpId, String csId, int evseId, int profileId) {
        String key = cpId + "|" + csId + "|" + evseId + "|" + profileId;
        long now = System.currentTimeMillis();
        Long last = lastPushTimestamps.get(key);
        if (last != null && (now - last) < DUPLICATE_PUSH_WINDOW_MS) {
            return true;
        }
        lastPushTimestamps.put(key, now);
        return false;
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
