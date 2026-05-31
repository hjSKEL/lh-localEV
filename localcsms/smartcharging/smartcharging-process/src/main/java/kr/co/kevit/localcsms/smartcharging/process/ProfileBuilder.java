package kr.co.kevit.localcsms.smartcharging.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingProfile;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedule;
import kr.co.kevit.localcsms.smartcharging.entity.domain.ChargingSchedulePeriod;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.process.SequenceService;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfileKind;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;
import kr.co.kevit.ocpp201.domain.ACChargingParametersType;
import kr.co.kevit.ocpp201.domain.ChargingNeedsType;
import kr.co.kevit.ocpp201.domain.DCChargingParametersType;
import kr.co.kevit.ocpp201.domain.V2XChargingParametersType;
import kr.co.kevit.ocpp201.enumtype.ControlModeEnumType;
import kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds;

/**
 * EV ChargingNeeds 기반 TxProfile 합성 (OCPP 2.1 K17/K19/K20, V2X/DER).
 *
 * <p>등록된 Tx 프로파일이 없을 때 NegotiationCoordinator 가 호출 → 합성된 프로파일을 push.
 * 우선순위: V2X(maxChargePower/maxDischargePower) → DC.evMaxPower → AC.evMaxCurrent×V×phases → 기본값.</p>
 */
@Component
public class ProfileBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileBuilder.class);
    private static final String SYSTEM = "SYSTEM";
    private static final double DEFAULT_LIMIT_W = 7400.0;
    private static final double DEFAULT_AC_VOLT = 230.0;
    private static final int DEFAULT_AC_PHASES = 3;

    private final SequenceService sequenceService;

    public ProfileBuilder(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /**
     * needs → TxProfile 합성. id 는 SequenceService 채번.
     *
     * @param transactionId 진행 중 충전 transactionId (TxProfile 필수)
     */
    public ChargingProfile synthesize(String cpId, String csId, int evseId, String transactionId,
                                      NotifyEVChargingNeeds req) {
        ChargingNeedsType needs = req != null ? req.getChargingNeeds() : null;
        boolean dynamic = needs != null && ControlModeEnumType.DynamicControl.equals(needs.getControlMode());
        boolean bpt = needs != null && needs.getRequestedEnergyTransfer() != null
                && needs.getRequestedEnergyTransfer().name().contains("BPT");

        double limitW = deriveLimit(needs);
        Double dischargeW = bpt ? deriveDischargeLimit(needs) : null;

        ChargingProfile p = new ChargingProfile();
        p.setProfileId(sequenceService.generateChargingProfileSeq());
        p.setCpId(cpId);
        p.setCsId(csId);
        p.setEvseId(evseId);
        p.setStackLevel(0);
        p.setPurpose(ChargingProfilePurpose.TxProfile);
        p.setKind(dynamic ? ChargingProfileKind.Dynamic : ChargingProfileKind.Absolute);
        p.setValidFrom(new Date());
        p.setRechargingId(transactionId);
        if (dynamic) {
            p.setDynUpdateInterval(60); // 기본 1분, 운영 시 조정
        }
        p.setWriter(new Writer(SYSTEM));

        ChargingSchedule sched = new ChargingSchedule();
        sched.setScheduleId(1);  // OCPP 2.1 chargingSchedule.id 필수
        sched.setScheduleSeq(1);
        sched.setStartSchedule(new Date());
        sched.setRateUnit("W");
        sched.setPeriods(buildPeriods(limitW, dischargeW, req != null ? req.getMaxScheduleTuples() : null));

        List<ChargingSchedule> list = new ArrayList<>();
        list.add(sched);
        p.setSchedules(list);

        LOGGER.info("ProfileBuilder.synthesize cpId={} csId={} evseId={} txId={} limit={}W discharge={} dynamic={} bpt={}",
                cpId, csId, evseId, transactionId, limitW, dischargeW, dynamic, bpt);
        return p;
    }

    /** 단일 period (startPeriod=0). maxScheduleTuples 가 있어도 1로 제한 — 합성은 단순 정책 */
    private List<ChargingSchedulePeriod> buildPeriods(double limitW, Double dischargeW, Integer maxTuples) {
        List<ChargingSchedulePeriod> periods = new ArrayList<>();
        ChargingSchedulePeriod p = new ChargingSchedulePeriod();
        p.setStartPeriod(0);
        p.setLimit(limitW);
        if (dischargeW != null) {
            p.setDischargeLimit(-Math.abs(dischargeW)); // OCPP: 음수가 방전
        }
        periods.add(p);
        if (maxTuples != null && maxTuples < 1) {
            LOGGER.warn("maxScheduleTuples={} 무시 (최소 1개 period 필요)", maxTuples);
        }
        return periods;
    }

    private double deriveLimit(ChargingNeedsType needs) {
        if (needs == null) return DEFAULT_LIMIT_W;
        V2XChargingParametersType v2x = needs.getV2xChargingParameters();
        if (v2x != null && v2x.getMaxChargePower() > 0) {
            return v2x.getMaxChargePower();
        }
        DCChargingParametersType dc = needs.getDcChargingParameters();
        if (dc != null) {
            if (dc.getEvMaxPower() > 0) return dc.getEvMaxPower();
            if (dc.getEvMaxVoltage() > 0 && dc.getEvMaxCurrent() > 0) {
                return dc.getEvMaxVoltage() * dc.getEvMaxCurrent();
            }
        }
        ACChargingParametersType ac = needs.getAcChargingParameters();
        if (ac != null && ac.getEvMaxCurrent() > 0) {
            double v = ac.getEvMaxVoltage() > 0 ? ac.getEvMaxVoltage() : DEFAULT_AC_VOLT;
            return ac.getEvMaxCurrent() * v * DEFAULT_AC_PHASES;
        }
        return DEFAULT_LIMIT_W;
    }

    private Double deriveDischargeLimit(ChargingNeedsType needs) {
        if (needs == null || needs.getV2xChargingParameters() == null) return null;
        double v = needs.getV2xChargingParameters().getMaxDischargePower();
        return v > 0 ? v : null;
    }
}
