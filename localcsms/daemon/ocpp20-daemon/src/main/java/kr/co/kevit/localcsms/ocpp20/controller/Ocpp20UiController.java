package kr.co.kevit.localcsms.ocpp20.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * /ocpp20/control20.html 정적 페이지 매핑.
 *
 * WebSocket 핸들러 패턴 /ocpp20/{cpId} 와 충돌하므로 컨트롤러로 가로채어 정적 리소스 반환한다.
 * (RequestMappingHandlerMapping order=0 가 WebSocketHandlerMapping order=1 보다 우선)
 */
@Controller
public class Ocpp20UiController {

    @GetMapping(value = "/ocpp20/control20.html", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public Resource control20() {
        return new ClassPathResource("static/ocpp20/control20.html");
    }
}
