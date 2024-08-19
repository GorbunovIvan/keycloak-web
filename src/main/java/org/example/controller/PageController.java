package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.KeycloakService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final KeycloakService keycloakService;

    @GetMapping
    public String homePage(Model model) {
        model.addAttribute("message", "Welcome to the home page");
        return "home";
    }

    @GetMapping("/secured")
    public String securedPage(Model model) {
        model.addAttribute("message", "This page is secured");
        return "secured";
    }

    @GetMapping("/current-user")
    public String user() {
        // Info about the current user will be automatically taken by the Model from the getCurrentUser() method
        return "current-user";
    }

    @GetMapping("/current-user/roles")
    @ResponseBody
    public List<String> getUserRoles() {
        return keycloakService.getCurrentUserRoles();
    }

    @ModelAttribute("currentUser")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }
}
