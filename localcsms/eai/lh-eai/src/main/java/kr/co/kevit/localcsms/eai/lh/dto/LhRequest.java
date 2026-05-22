package kr.co.kevit.localcsms.eai.lh.dto;

/**
 * LH 외부 호출 요청 (빈 로직 — 필드는 실제 연계 명세 확정 시 채운다).
 *
 * <pre>
 * {"stationid": "1", "payload": "..."}
 * </pre>
 */
public class LhRequest {

    private String stationid;
    private String payload;

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
