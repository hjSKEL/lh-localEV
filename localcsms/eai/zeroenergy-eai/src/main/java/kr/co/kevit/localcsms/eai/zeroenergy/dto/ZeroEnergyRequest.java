package kr.co.kevit.localcsms.eai.zeroenergy.dto;

/**
 * Zero Energy 충전량 조회 요청.
 *
 * <pre>
 * {"stationid":"1"}
 * </pre>
 */
public class ZeroEnergyRequest {

    private String stationid;

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }
}
