package kr.co.kevit.localcsms.adminweb.config;

import kr.co.kevit.localcsms.common.util.enumtype.PropertyKey;
import kr.co.kevit.localcsms.common.util.loader.PropertyLoader;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CommonModelAdvice {

    @ModelAttribute("loginCoName")
    public String loginCoName() {
        return PropertyLoader.getInstance().getProperty(PropertyKey.KEVIT_LOGIN_CO);
    }
}
