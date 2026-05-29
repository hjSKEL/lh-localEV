package kr.co.kevit.localcsms.smartcharging.process;

import kr.co.kevit.ocpp201.domain.ChargingProfileType;
import kr.co.kevit.ocpp201.enumtype.NotifyEVChargingNeedsStatusEnumType;

/**
 * NotifyEVChargingNeeds 협상 처리 결과.
 *
 * <p>{@code status} 는 NotifyEVChargingNeedsResponse 에 사용, {@code profileToPush} 가 non-null 이면
 * 응답 송신 직후 SetChargingProfile CALL 로 push.</p>
 */
public class NegotiationResult {

    private final NotifyEVChargingNeedsStatusEnumType status;
    private final ChargingProfileType profileToPush;

    public NegotiationResult(NotifyEVChargingNeedsStatusEnumType status, ChargingProfileType profileToPush) {
        this.status = status;
        this.profileToPush = profileToPush;
    }

    public NotifyEVChargingNeedsStatusEnumType getStatus() {
        return status;
    }

    public ChargingProfileType getProfileToPush() {
        return profileToPush;
    }

    public boolean hasProfileToPush() {
        return profileToPush != null;
    }
}
