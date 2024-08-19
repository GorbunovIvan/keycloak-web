package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checking-roles")
public class CheckingRolesController {

    @GetMapping("/for-users")
    public String onlyUserCanAccess() {
        return "For users";
    }

    @GetMapping("/for-admins")
    public String onlyAdminCanAccess() {
        return "For admins";
    }
}
