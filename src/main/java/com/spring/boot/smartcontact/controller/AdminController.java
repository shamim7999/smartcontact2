package com.spring.boot.smartcontact.controller;

import com.spring.boot.smartcontact.Repository.AdminProductRepository;
import com.spring.boot.smartcontact.Repository.ContactRepository;
import com.spring.boot.smartcontact.Repository.UserRepository;
import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.Contact;
import com.spring.boot.smartcontact.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminProductRepository adminProductRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;
    @ModelAttribute
    public void addCommonAttribute(Model model, Principal principal) {
        User user = this.userRepository.getUserByUserName(principal.getName());
        model.addAttribute("user", user);


    }
    @GetMapping("/index")
    public String adminHome(Model model, Principal principal) {
        User user = this.userRepository.getUserByUserName(principal.getName());
        model.addAttribute("title", "Admin Dashboard");
        model.addAttribute("user", user);
        return "admin/user_dashboard";
    }

    // Show Contacts Handler
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
        User user = this.userRepository.getUserByUserName(principal.getName());

        Pageable pageable = PageRequest.of(page, 2);

        Page <Contact> contactList = this.contactRepository.findAll(pageable);
        System.out.println(contactList);
        model.addAttribute("title", "All Contacts");
        model.addAttribute("contactList", contactList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contactList.getTotalPages());
        model.addAttribute("userRole", "ADMIN");
        return "admin/show_contacts";
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
        String productName = adminProduct.getAdminProductName() + "-" + LocalTime.now();
        adminProduct.setAdminProductName(productName);
        this.adminProductRepository.save(adminProduct);
        return "admin/add_product_form";
    }

    @GetMapping("/show-products")
    public String showProducts() {
        System.out.println(this.adminProductRepository.findAll());
        return "redirect:/index";
    }
}
