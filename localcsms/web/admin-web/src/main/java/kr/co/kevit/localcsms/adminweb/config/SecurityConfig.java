package kr.co.kevit.localcsms.adminweb.config;

import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.security.WebAccessDeniedHandler;
import kr.co.kevit.localcsms.adminweb.security.WebAuthenticationFailHandler;
import kr.co.kevit.localcsms.adminweb.security.WebAuthenticationProvider;
import kr.co.kevit.localcsms.adminweb.security.WebAuthenticationSuccessHandler;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;

// @Secured 계층(ROLE_ROOT_ADMIN 등)은 MethodSecurityConfig에서 별도로 켠다(@EnableGlobalMethodSecurity 중복 방지).
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public WebAuthenticationProvider webAuthenticationProvider() {
        return new WebAuthenticationProvider(5);
    }

    @Bean
    public WebAuthenticationSuccessHandler webAuthenticationSuccessHandler() {
        return new WebAuthenticationSuccessHandler();
    }

    @Bean
    public WebAuthenticationFailHandler webAuthenticationFailHandler() {
        return new WebAuthenticationFailHandler();
    }

    @Bean
    public WebAccessDeniedHandler webAccessDeniedHandler() {
        return new WebAccessDeniedHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(webAuthenticationProvider());
        http
            .authorizeRequests()
                .antMatchers("/resources/**", "/**/*.js", "/**/*.css", "/**/*.xls", "/**/*.html", "/error", "/error/**").permitAll()
                .antMatchers("/ws/log/upload/**").permitAll()
                .antMatchers("/").hasAnyRole("ROOT_ADMIN", "ADMIN", "OPER")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("login-username")
                .passwordParameter("login-password")
                .successHandler(webAuthenticationSuccessHandler())
                .failureHandler(webAuthenticationFailHandler())
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .addLogoutHandler((request, response, authentication) -> {
                    if (authentication != null && authentication.getPrincipal() instanceof User) {
                        User user = (User) authentication.getPrincipal();
                        SessionManager.removeSession(user.getLoginId());
                    }
                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
            .exceptionHandling()
                .accessDeniedHandler(webAccessDeniedHandler())
                .and()
            .csrf().disable()
            .headers()
                .frameOptions().sameOrigin();

        return http.build();
    }
}
