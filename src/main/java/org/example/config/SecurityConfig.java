package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, LogoutSuccessHandler oidcLogoutSuccessHandler) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/checking-roles/for-users").hasAnyRole("user")
                        .requestMatchers("/checking-roles/for-admins").hasAnyRole("admin")
                        .anyRequest().authenticated())
                .oauth2Login(withDefaults())
                .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler))
                .build();
    }

    @Bean
    protected LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        var successHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("{baseUrl}");
        return successHandler;
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {

        return authorities -> {

            var mappedAuthorities = new HashSet<GrantedAuthority>();

            for (var authority : authorities) {
                if (authority instanceof OidcUserAuthority oidcUserAuthority) {

                    var realmRoles = getRealmRolesOfAuthority(oidcUserAuthority);

                    for (var role : realmRoles) {
                        var authorityWithRole = new SimpleGrantedAuthority("ROLE_" + role);
                        mappedAuthorities.add(authorityWithRole);
                    }
                }
            }

            return mappedAuthorities;
        };
    }

    private Collection<String> getRealmRolesOfAuthority(OidcUserAuthority oidcUserAuthority) {

        OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

        Map<String, Object> realmAccess = userInfo.getClaim("realm_access");
        if (realmAccess == null) {
            return Collections.emptySet();
        }

        @SuppressWarnings("unchecked")
        Collection<String> realmRoles = (Collection<String>) realmAccess.get("roles");
        if (realmRoles == null) {
            return Collections.emptySet();
        }

        return realmRoles;
    }
}
