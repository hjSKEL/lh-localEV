package kr.co.kevit.localcsms.eai.lh.dto;

/**
 * LH 외부 호출 정상 응답 (HTTP 200).
 *
 * <pre>
 * {"result": "200", "message": "OK", "sendTime": "2026-05-22T05:24:25.699Z"}
 * </pre>
 */
public class LhResponse {

    private String result;
    private String message;
    private String sendTime;

    public LhResponse() {
    }

    public LhResponse(String result, String message, String sendTime) {
        this.result = result;
        this.message = message;
        this.sendTime = sendTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
