package com.spring.boot.smartcontact.controller.test;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shamim")
@PreAuthorize("hasRole('ROLE_USER')")
public class TestController {

    @GetMapping("/google")
    public String googleLoader() {
        return "redirect:https://www.google.com";
    }
}
