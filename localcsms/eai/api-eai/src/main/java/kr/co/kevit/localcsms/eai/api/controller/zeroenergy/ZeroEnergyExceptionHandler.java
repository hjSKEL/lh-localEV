package kr.co.kevit.localcsms.eai.api.controller.zeroenergy;

import kr.co.kevit.localcsms.eai.api.dto.zeroenergy.ZeroEnergyErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * {@link ZeroEnergyController} 전용 예외 핸들러 — 잘못된 메서드(405), 잘못된 본문(400),
 * 그 외 예외를 모두 {@link ZeroEnergyErrorResponse} 포맷(HTTP 400)으로 통일해서 반환한다.
 *
 * <p>
 * {@code assignableTypes} 로 {@link ZeroEnergyController} 에서 발생한 예외만 처리하도록 범위를
 * 한정했다 — api-eai 의 다른 26개 OCTT 엔드포인트에서 발생하는 예외에는 영향을 주지 않는다.
 * (구 zeroenergy-eai 는 단독 앱이라 {@code NoHandlerFoundException}(잘못된 접속 주소)도 처리했지만,
 * 그건 핸들러 매칭 자체가 안 된 요청이라 컨트롤러 단위로 범위를 좁힐 수 없어 — api-eai 전체의
 * 404 응답 형식을 바꾸는 부작용을 피하기 위해 통합 시 제외했다.)
 * </p>
 */
@RestControllerAdvice(assignableTypes = ZeroEnergyController.class)
public class ZeroEnergyExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ZeroEnergyExceptionHandler.class);

    /** 잘못된 HTTP 메서드. */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ZeroEnergyErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException e) {
        log.warn("[ZEROENERGY] Method not supported: {}", e.getMethod());
        return badRequest("Bad request (method not allowed)");
    }

    /** 본문 파싱 실패 / JSON 형식 오류. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ZeroEnergyErrorResponse> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("[ZEROENERGY] Message not readable: {}", e.getMostSpecificCause().getMessage());
        return badRequest("Bad request (bad request param info)");
    }

    /** 그 외 모든 예외. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ZeroEnergyErrorResponse> handleAll(Exception e) {
        log.error("[ZEROENERGY] Unhandled error", e);
        return badRequest("Bad request (bad request param info)");
    }

    private ResponseEntity<ZeroEnergyErrorResponse> badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ZeroEnergyErrorResponse("400", message, null));
    }
}
