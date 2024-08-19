package org.example.service;

import org.keycloak.common.util.KeycloakUriBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    @Value("${server.url}")
    private String serverURl;

    @Value("${keycloak.server-url}")
    private String kcServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.web-registration-name}")
    private String registrationName;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    public String getLoginURL() {
        return KeycloakUriBuilder.fromUri(kcServerUrl)
                .path("/realms/{realm-name}/protocol/openid-connect/auth")
                .queryParam("client_id", clientId)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile")
                .queryParam("redirect_uri", String.format("%s/oauth2/authorization/%s", serverURl, registrationName))
                .build(realm)
                .toString();
    }

    public String getRegistrationURL() {
        return KeycloakUriBuilder.fromUri(kcServerUrl)
                .path("/realms/{realm-name}/protocol/openid-connect/registrations")
                .queryParam("client_id", clientId)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile")
                .queryParam("redirect_uri", String.format("%s/auth/after-registration", serverURl))
                .build(realm)
                .toString();
    }

    public String getLogoutURL() {
        return KeycloakUriBuilder.fromUri(kcServerUrl)
                .path("/realms/{realm-name}/protocol/openid-connect/logout")
                .queryParam("client_id", clientId)
                .queryParam("post_logout_redirect_uri", String.format("%s/logout", serverURl))
                .build(realm)
                .toString();
    }

    public List<String> getCurrentUserRoles() {

//        var authentication = (OAuth2AuthenticationToken) principal; // you could also autowire principal in the params
        var authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return Collections.emptyList();
        }

        var principal = authentication.getPrincipal();

        @SuppressWarnings("unchecked")
        var realmAccess = (Map<String, Collection<String>>) principal.getAttribute("realm_access");
        if (realmAccess == null) {
            return Collections.emptyList();
        }

        return (List<String>) realmAccess.get("roles");
    }
}
