package com.spring.boot.smartcontact.controller.user;

import com.spring.boot.smartcontact.service.AdminProductService;

import com.spring.boot.smartcontact.service.UserService;
import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AdminProductService adminProductService;
    public UserController(UserService userService, AdminProductService adminProductService) {
        this.userService = userService;
        this.adminProductService = adminProductService;
    }

    @ModelAttribute
    public User addCommonAttribute(Principal principal) {
        return userService.getUserByUserName(principal.getName());
    }
    @ModelAttribute
    public Principal sendPrincipal(Principal principal) {
        return principal;
    }
    @GetMapping("/index")
    public String dashBoard(Model model) {

        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("title", "User Dashboard");
        List<AdminProduct> adminProductList = adminProductService.findAllByAdminStatusProductSetToZero();
        model.addAttribute("adminProductList", adminProductList);
        model.addAttribute("showSidebar", true);
        return "user_dashboard";
    }
    @GetMapping("/profile")
    public String profileDetails(Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("showSidebar", true);
        return "profile_details";
    }
}