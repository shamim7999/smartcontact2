package com.spring.boot.smartcontact.controller.admin;


import com.spring.boot.smartcontact.service.UserService;
import com.spring.boot.smartcontact.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        User user = userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("showSidebar", true);
    }

    @ModelAttribute
    public Principal sendPrincipal(Principal principal) {
        return principal;
    }
    @GetMapping("/index")
    public String adminHome(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        return "user_dashboard";
    }

    @GetMapping("/profile")
    public String profileDetails() {
        return "profile_details";
    }
}
