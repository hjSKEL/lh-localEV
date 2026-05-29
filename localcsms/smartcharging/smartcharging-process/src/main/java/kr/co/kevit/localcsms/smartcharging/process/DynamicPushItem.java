package kr.co.kevit.localcsms.smartcharging.process;

import kr.co.kevit.ocpp201.domain.ChargingScheduleUpdateType;

/**
 * Dynamic 프로파일 주기 push 1건 (UpdateDynamicSchedule CALL 송신 입력).
 */
public class DynamicPushItem {

    private final String cpId;
    private final String csId;
    private final int profileId;
    private final ChargingScheduleUpdateType scheduleUpdate;

    public DynamicPushItem(String cpId, String csId, int profileId, ChargingScheduleUpdateType scheduleUpdate) {
        this.cpId = cpId;
        this.csId = csId;
        this.profileId = profileId;
        this.scheduleUpdate = scheduleUpdate;
    }

    public String getCpId() { return cpId; }
    public String getCsId() { return csId; }
    public int getProfileId() { return profileId; }
    public ChargingScheduleUpdateType getScheduleUpdate() { return scheduleUpdate; }
}
