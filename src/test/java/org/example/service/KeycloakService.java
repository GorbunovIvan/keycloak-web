package org.example.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {

    public String getLoginURL() {
        return "http://test.com/login";
    }

    public String getRegistrationURL() {
        return "http://test.com/register";
    }

    public String getLogoutURL() {
        return "http://test.com/logout";
    }

    public List<String> getCurrentUserRoles() {
        return Collections.emptyList();
    }
}
