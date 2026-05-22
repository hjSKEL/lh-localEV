package kr.co.kevit.localcsms.eai.zeroenergy.dto;

/**
 * Zero Energy 충전량 조회 정상 응답 (HTTP 200).
 *
 * <pre>
 * {"totalCharge": "117.30", "sendTime": "2023-12-27T05:24:25.699Z"}
 * </pre>
 */
public class ZeroEnergyResponse {

    private String totalCharge;
    private String sendTime;

    public ZeroEnergyResponse() {
    }

    public ZeroEnergyResponse(String totalCharge, String sendTime) {
        this.totalCharge = totalCharge;
        this.sendTime = sendTime;
    }

    public String getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(String totalCharge) {
        this.totalCharge = totalCharge;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
