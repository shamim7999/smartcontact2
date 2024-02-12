package com.spring.boot.smartcontact.controller.admin;

import com.spring.boot.smartcontact.model.User;
import com.spring.boot.smartcontact.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class UserManipulateController {
    private final UserService userService;

    public UserManipulateController(UserService userService) {
        this.userService = userService;
    }
    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        User user = userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("showSidebar", true);
    }

    @PostMapping("/suspend-user/{id}")
    public String suspendUser(@PathVariable int id) {
        userService.suspendUserById(id);
        return "redirect:/admin/show-users";
    }

    @PostMapping("/enable-user/{id}")
    public String enableUser(@PathVariable int id) {
        userService.enableUserById(id);
        return "redirect:/admin/show-users";
    }
}
