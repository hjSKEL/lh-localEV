package kr.co.kevit.localcsms.eai.api.dto.model;

import java.time.OffsetDateTime;

/** OCPP 1.6 Security LogParameters (GetLog) */
public class LogParameters {

    /** 로그 업로드 대상 URL */
    private String remoteLocation;
    /** 로그 수집 시작 시각 (옵션) */
    private OffsetDateTime oldestTimestamp;
    /** 로그 수집 종료 시각 (옵션) */
    private OffsetDateTime latestTimestamp;

    public String getRemoteLocation()                   { return remoteLocation; }
    public void setRemoteLocation(String v)             { this.remoteLocation = v; }

    public OffsetDateTime getOldestTimestamp()          { return oldestTimestamp; }
    public void setOldestTimestamp(OffsetDateTime v)    { this.oldestTimestamp = v; }

    public OffsetDateTime getLatestTimestamp()          { return latestTimestamp; }
    public void setLatestTimestamp(OffsetDateTime v)    { this.latestTimestamp = v; }
}
