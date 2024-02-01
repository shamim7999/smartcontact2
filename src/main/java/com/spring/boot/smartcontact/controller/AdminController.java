package com.spring.boot.smartcontact.controller;

import com.spring.boot.smartcontact.dao.AdminProductRepository;
import com.spring.boot.smartcontact.model.AdminProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminProductRepository adminProductRepository;

    @GetMapping("/index")
    public String adminHome() {
        return "home";
    }

    @GetMapping("/add-product")
    public String addProduct() {
        AdminProduct adminProduct = new AdminProduct();
        adminProduct.setAdminProductName("Dell");
        this.adminProductRepository.save(adminProduct);
        return "redirect:/admin/index";
    }

    @GetMapping("/show-products")
    public String showProducts() {
        System.out.println(this.adminProductRepository.findAll());
        return "redirect:/index";
    }
}
