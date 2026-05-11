package kr.co.kevit.localcsms.eai.api.dto;

/**
 * SUT API 공통 응답.
 * HTTP 200 OK 로 반환되며, status 로 성공/실패를 표시한다.
 */
public class ApiResult {

    private String status;   // "accepted" | "rejected"
    private Object data;     // 응답 데이터 (옵션)
    private String message;  // 오류 메시지 (옵션)

    public static ApiResult accepted() {
        ApiResult r = new ApiResult();
        r.status = "accepted";
        return r;
    }

    public static ApiResult accepted(Object data) {
        ApiResult r = new ApiResult();
        r.status = "accepted";
        r.data = data;
        return r;
    }

    public static ApiResult rejected(String message) {
        ApiResult r = new ApiResult();
        r.status = "rejected";
        r.message = message;
        return r;
    }

    public String getStatus()  { return status; }
    public Object getData()    { return data; }
    public String getMessage() { return message; }
}
