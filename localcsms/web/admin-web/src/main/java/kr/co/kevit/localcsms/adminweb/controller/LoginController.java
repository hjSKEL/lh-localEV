/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.entity.domain.UserRole;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 9.
 */
@Controller
@RequestMapping("/login")
public class LoginController {
	//
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    /**
     * 로그인 페이지 이동
     * 
     * @return
     */
    @RequestMapping
    public String loginPage(HttpServletRequest req) {
    	//
        String contextPath = req.getContextPath();
        LOGGER.debug("CONTEXT_PATH : " + contextPath);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            User loginUser = (User) authentication.getPrincipal();
            List<UserRole> roles = loginUser.getRoles();
            switch (roles.get(0).getRoleType()) {
            case ADMIN:
                return "redirect:admin/main";
            case OPERATION:
                return "redirect:operation/main";
            default:
                return "common/login/loginPage";
            }
        }catch(ClassCastException ex){
            LOGGER.error(ex.getMessage());
        }
        return "common/login/loginPage";
    }

    /**
     * 사용자 권한 없을시 관련 페이지로 이동
     *
     * @param model
     * @param auth
     * @param req
     * @return
     */
    @RequestMapping("/denied")
    public String denied(Model model) {

        model.addAttribute("errMsg", "권한이 없습니다.");
        return "common/login/denied";
    }

    /**
     * 사용자 계정 검색
     * 
     * @return
     */
//    @RequestMapping(value = "/find/idpin")
//    public String findUserId() {
//        
//        return "/common/login/findIdPin";
//    }
}