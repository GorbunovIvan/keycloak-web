package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
