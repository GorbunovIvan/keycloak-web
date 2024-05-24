package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class PageController {

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

    @ModelAttribute("currentUser")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }
}
