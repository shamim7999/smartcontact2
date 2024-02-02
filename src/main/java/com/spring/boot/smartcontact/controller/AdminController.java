package com.spring.boot.smartcontact.controller;

import com.spring.boot.smartcontact.dao.AdminProductRepository;
import com.spring.boot.smartcontact.dao.UserRepository;
import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminProductRepository adminProductRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/index")
    public String adminHome(Model model, Principal principal) {
        User user = this.userRepository.getUserByUserName(principal.getName());
        model.addAttribute("title", "Admin Dashboard");
        model.addAttribute("user", user);
        return "admin/user_dashboard";
    }

    @GetMapping("/add-product")
    public String addProduct(Model model) {
//        AdminProduct adminProduct = new AdminProduct();
//        adminProduct.setAdminProductName("Dell");
//        this.adminProductRepository.save(adminProduct);
//        return "redirect:/admin/index";
        model.addAttribute("showLoginButton", 0);
        return "admin/add_product_form";
    }

    @PostMapping("/process-product")
    public String processProduct(@ModelAttribute("adminProduct") AdminProduct adminProduct) {
        this.adminProductRepository.save(adminProduct);
        return "admin/add_product_form";
    }

    @GetMapping("/show-products")
    public String showProducts() {
        System.out.println(this.adminProductRepository.findAll());
        return "redirect:/index";
    }
}
