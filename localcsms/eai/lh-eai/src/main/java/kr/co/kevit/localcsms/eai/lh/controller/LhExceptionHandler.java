package kr.co.kevit.localcsms.eai.lh.controller;

import kr.co.kevit.localcsms.eai.lh.dto.LhErrorResponse;
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
 * LH EAI 전역 예외 핸들러.
 *
 * 잘못된 접속 주소(404), 잘못된 메서드(405), 잘못된 본문(400) 등을
 * 모두 {@link LhErrorResponse} 포맷(HTTP 400)으로 통일하여 반환한다.
 *
 * NoHandlerFoundException 을 받으려면 application.yml 의 다음 두 설정이 필요하다.
 *   spring.mvc.throw-exception-if-no-handler-found: true
 *   spring.web.resources.add-mappings: false
 */
@RestControllerAdvice
public class LhExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(LhExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<LhErrorResponse> handleNoHandler(NoHandlerFoundException e) {
        log.warn("[LH] No handler: {} {}", e.getHttpMethod(), e.getRequestURL());
        return badRequest("Bad request (invalid url)");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<LhErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("[LH] Method not supported: {}", e.getMethod());
        return badRequest("Bad request (method not allowed)");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<LhErrorResponse> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("[LH] Message not readable: {}", e.getMostSpecificCause().getMessage());
        return badRequest("Bad request (bad request param info)");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<LhErrorResponse> handleAll(Exception e) {
        log.error("[LH] Unhandled error", e);
        return badRequest("Bad request (bad request param info)");
    }

    private ResponseEntity<LhErrorResponse> badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new LhErrorResponse("400", message, null));
    }
}
