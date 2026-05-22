package kr.co.kevit.localcsms.eai.zeroenergy.controller;

import kr.co.kevit.localcsms.eai.zeroenergy.dto.ZeroEnergyErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Zero Energy EAI 전역 예외 핸들러.
 *
 * 잘못된 접속 주소(404), 잘못된 메서드(405), 잘못된 본문(400) 등을
 * 모두 {@link ZeroEnergyErrorResponse} 포맷(HTTP 400)으로 통일하여 반환한다.
 *
 * NoHandlerFoundException 을 받으려면 application.yml 의 다음 두 설정이 필요하다.
 *   spring.mvc.throw-exception-if-no-handler-found: true
 *   spring.web.resources.add-mappings: false
 */
@RestControllerAdvice
public class ZeroEnergyExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ZeroEnergyExceptionHandler.class);

    /** 잘못된 접속 주소 (매핑 없음). */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ZeroEnergyErrorResponse> handleNoHandler(NoHandlerFoundException e) {
        log.warn("[ZEROENERGY] No handler: {} {}", e.getHttpMethod(), e.getRequestURL());
        return badRequest("Bad request (invalid url)");
    }

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
