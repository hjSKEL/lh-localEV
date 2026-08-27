package kr.co.kevit.localcsms.adminweb.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * ROLE_ROOT_ADMIN이 @Secured({"ROLE_ADMIN", ...}) 등 개별 목록을 일일이 고치지 않고도
 * 모든 화면/API에 접근 가능하도록 역할 계층(RoleHierarchy)을 @Secured 검사에 적용한다.
 * (SecurityConfig의 HttpSecurity.hasAnyRole()은 표현식 기반이라 RoleHierarchy를 별도 주입 없이 따르지만,
 *  @Secured는 RoleVoter를 RoleHierarchyVoter로 바꿔줘야 계층을 인식한다.)
 *
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 8. 27.
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        // 코드베이스 전체 @Secured에 등장하는 역할 문자열을 모두 나열 - 새 역할 문자열이 추가되면 여기도 같이 추가해야 함.
        roleHierarchy.setHierarchy(
            "ROLE_ROOT_ADMIN > ROLE_ADMIN\n" +
            "ROLE_ROOT_ADMIN > ROLE_OPER\n" +
            "ROLE_ROOT_ADMIN > ROLE_ADJUST\n" +
            "ROLE_ROOT_ADMIN > ROLE_CO_USER\n" +
            "ROLE_ROOT_ADMIN > ROLE_CP_MGT\n" +
            "ROLE_ROOT_ADMIN > ROLE_MS\n" +
            "ROLE_ROOT_ADMIN > ROLE_MM\n" +
            "ROLE_ROOT_ADMIN > ROLE_CHGR_BAILOR\n" +
            "ROLE_ROOT_ADMIN > ROLE_CM\n" +
            "ROLE_ROOT_ADMIN > ROLE_CS\n" +
            "ROLE_ROOT_ADMIN > ROLE_USER\n" +
            "ROLE_ROOT_ADMIN > ROLE_GUEST"
        );
        return roleHierarchy;
    }

    // @EnableGlobalMethodSecurity(securedEnabled = true) 만 켜진 상태의 기본 accessDecisionManager()는
    // RoleVoter + AuthenticatedVoter 조합뿐이라, RoleVoter 자리만 RoleHierarchyVoter로 바꿔 그대로 재현한다.
    @Override
    protected AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> voters = new ArrayList<>();
        voters.add(new RoleHierarchyVoter(roleHierarchy()));
        voters.add(new AuthenticatedVoter());
        return new AffirmativeBased(voters);
    }
}
