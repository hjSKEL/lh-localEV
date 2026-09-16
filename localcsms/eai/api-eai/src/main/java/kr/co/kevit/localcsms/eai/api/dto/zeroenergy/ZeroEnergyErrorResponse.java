package kr.co.kevit.localcsms.eai.api.dto.zeroenergy;

/**
 * Zero Energy 충전량 조회 비정상 응답 (결과코드 400).
 *
 * <pre>
 * {"result": "400", "message": "Bad request (bad request param info)", "data": null}
 * </pre>
 */
public class ZeroEnergyErrorResponse {

    private String result;
    private String message;
    private Object data;

    public ZeroEnergyErrorResponse() {
    }

    public ZeroEnergyErrorResponse(String result, String message, Object data) {
        this.result = result;
        this.message = message;
        this.data = data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
