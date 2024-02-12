package com.spring.boot.smartcontact.controller.admin;

import com.spring.boot.smartcontact.model.User;
import com.spring.boot.smartcontact.service.AdminProductService;
import com.spring.boot.smartcontact.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
@Controller
@RequestMapping("/admin")
public class ProductShowController {

    private final UserService userService;
    private final AdminProductService adminProductService;

    public ProductShowController(UserService userService,
                                AdminProductService adminProductService) {
        this.userService = userService;
        this.adminProductService = adminProductService;
    }

    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        User user = userService.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("showSidebar", true);
    }

    @GetMapping("/show-products")
    public String showProducts() {
        System.out.println(adminProductService.findAll());
        return "redirect:/index";
    }
}
