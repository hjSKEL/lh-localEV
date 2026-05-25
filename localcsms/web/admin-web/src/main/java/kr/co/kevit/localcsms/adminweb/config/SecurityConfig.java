package kr.co.kevit.localcsms.adminweb.config;

import kr.co.kevit.localcsms.adminweb.security.SessionManager;
import kr.co.kevit.localcsms.adminweb.security.WebAccessDeniedHandler;
import kr.co.kevit.localcsms.adminweb.security.WebAuthenticationFailHandler;
import kr.co.kevit.localcsms.adminweb.security.WebAuthenticationProvider;
import kr.co.kevit.localcsms.adminweb.security.WebAuthenticationSuccessHandler;
import kr.co.kevit.localcsms.authority.entity.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
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
                .antMatchers("/").hasAnyRole("ADMIN", "OPER")
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
