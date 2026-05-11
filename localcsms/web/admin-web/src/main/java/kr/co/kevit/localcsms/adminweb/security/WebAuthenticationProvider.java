/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import kr.co.kevit.localcsms.authority.entity.domain.User;
import kr.co.kevit.localcsms.authority.entity.domain.UserRole;
import kr.co.kevit.localcsms.authority.process.RoleAuthorityService;
import kr.co.kevit.localcsms.authority.process.UserService;
import kr.co.kevit.localcsms.common.util.security.PasswordUtil;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * 로그인 인증
  * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 10. 15.
 */
public class WebAuthenticationProvider implements AuthenticationProvider {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WebAuthenticationProvider.class);

    private final int MAX_FAIL_COUNT;

    public WebAuthenticationProvider(int maxFailCount) {
        MAX_FAIL_COUNT = maxFailCount;
    }

    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleAuthorityService roleAuthorityService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //
        // 로그인한 유저정보 확인 name = 사용자id, credentials = 사용자 password
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        if (StringUtils.isEmpty(authToken.getName()) || StringUtils.isEmpty(authToken.getCredentials().toString())) {
            throw new UsernameNotFoundException(encoding("아이디와 비밀번호를 입력하세요."));
        }

        // 사용자 아이디로 사용자 정보 찾기
        User user = userService.retrieveUserByIdNType(authToken.getName(), "EMPLOYEE");

        // 사용자 아이디로 사용자 검색이 안될시
        if (user == null) {
            //
            LOGGER.error("LOGIN ID ({})IS NOT EXIST ERROR", authToken.getName());
            throw new UsernameNotFoundException(encoding("아이디와 비밀번호가 일치하지 않습니다. 다시 로그인 바랍니다."));
        }
        
        if (user.getPwFailCount() >= MAX_FAIL_COUNT) {
            LOGGER.error("LOGIN ID ({})IS LOCK. {} over.", authToken.getName(), MAX_FAIL_COUNT);
            throw new BadCredentialsException(encoding(MAX_FAIL_COUNT + "회 이상 로그인 실패로 계정이 잠겼습니다. 관리자에게 문의바랍니다."));
        }
        
        String password = authToken.getCredentials().toString();
        PasswordUtil passwordUtil = PasswordUtil.getInstance();
        String passwordEncode = StringConstants.BLANK;
        try {
            passwordEncode = passwordUtil.encryptPassword(password, user.getSalt());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        if (!user.getUserPwd().equals(passwordEncode)) {
            //
           user.setPwFailCount(user.getPwFailCount() + 1);
           userService.modifyUser(user);
           LOGGER.error("LOGIN ID ({}) PW is missmatch", authToken.getName());
           throw new BadCredentialsException(encoding("아이디와 비밀번호가 일치하지 않습니다. 다시 로그인 바랍니다."));
        }
        user.setPwFailCount(0);
        user.setLastLoginDate(new Date());
        userService.modifyUser(user);
        
        // 권한 부여
        List<UserRole> userRoles = roleAuthorityService.retrieveUserRoleByUserId(authToken.getName());
        user.setRoles(userRoles);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(UserRole userRole : user.getRoles()){
        	authorities.add(new SimpleGrantedAuthority(userRole.getRoleType().getCode()));
        }
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }
    
    private String encoding(String message) {
        try {
            return URLEncoder.encode(message, StringConstants.UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return StringConstants.BLANK;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
